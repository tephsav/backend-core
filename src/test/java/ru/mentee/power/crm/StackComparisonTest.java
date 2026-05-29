package ru.mentee.power.crm;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.servlet.LeadListServlet;
import ru.mentee.power.crm.spring.Application;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class StackComparisonTest {

    private static final int SERVLET_PORT = 8080;
    private static final int SPRING_PORT = 8081;

    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    @Test
    @DisplayName("Оба стека должны возвращать лидов в HTML таблице")
    void shouldReturnLeadsFromBothStacks() throws IOException, InterruptedException {
        HttpRequest servletRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + SERVLET_PORT + "/leads"))
                .GET()
                .build();

        HttpRequest springRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + SPRING_PORT + "/leads"))
                .GET()
                .build();

        HttpResponse<String> servletResponse = httpClient.send(
                servletRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> springResponse = httpClient.send(
                springRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(servletResponse.statusCode()).isEqualTo(200);
        assertThat(springResponse.statusCode()).isEqualTo(200);

        assertThat(servletResponse.body()).contains("<table");
        assertThat(springResponse.body()).contains("<table");

        int servletRows = countTableRows(servletResponse.body());
        int springRows = countTableRows(springResponse.body());

        assertThat(servletRows)
                .as("Количество лидов должно совпадать")
                .isEqualTo(springRows);

        System.out.printf("Servlet: %d лидов, Spring: %d лидов%n", servletRows, springRows);
    }

    private int countTableRows(String html) {
        return html.split("<tr class=\"border-t").length - 1;
    }

    @Test
    @DisplayName("Измерение времени старта обоих стеков")
    void shouldMeasureStartupTime() throws LifecycleException {
        long servletStartupMs = measureServletStartup();
        long springStartupMs = measureSpringBootStartup();

        System.out.println("=== Сравнение времени старта ===");
        System.out.printf("Servlet стек: %d ms%n", servletStartupMs);
        System.out.printf("Spring Boot: %d ms%n", springStartupMs);
        System.out.printf("Разница: Spring %s на %d ms%n",
                springStartupMs > servletStartupMs ? "медленнее" : "быстрее",
                Math.abs(springStartupMs - servletStartupMs));

        assertThat(servletStartupMs).isLessThan(10_000);
        assertThat(springStartupMs).isLessThan(50_000);
    }

    private long measureServletStartup() throws LifecycleException {
        LeadRepository leadRepository = new LeadRepository();
        LeadService leadService = new LeadService(leadRepository);
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(0);
        tomcat.getConnector();
        Context context = tomcat.addContext("", new File(".").getAbsolutePath());
        context.getServletContext().setAttribute("leadService", leadService);
        Tomcat.addServlet(context, "LeadListServlet", new LeadListServlet());
        context.addServletMappingDecoded("/leads", "LeadListServlet");

        long start = System.nanoTime();
        tomcat.start();
        long elapsed = System.nanoTime() - start;

        tomcat.stop();
        tomcat.destroy();

        return elapsed / 1_000_000;
    }

    private long measureSpringBootStartup() {
        long start = System.nanoTime();
        SpringApplication app = new SpringApplication(Application.class);
        ConfigurableApplicationContext context = app.run("--server.port=0");
        long elapsed = System.nanoTime() - start;

        context.close();

        return elapsed / 1_000_000;
    }
}