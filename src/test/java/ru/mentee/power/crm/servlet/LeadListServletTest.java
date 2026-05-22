package ru.mentee.power.crm.servlet;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.service.LeadService;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;

@ExtendWith(MockitoExtension.class)
class LeadListServletTest {
    private LeadListServlet servlet;

    TemplateEngine templateEngine;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private ServletContext mockContext;

    @Mock
    private LeadService mockService;

    @BeforeEach
    void setUp() {
        servlet = new LeadListServlet() {
            @Override
            public ServletContext getServletContext() {
                return mockContext;
            }
        };
        Path templatePath = Path.of("src/main/jte");
        DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(templatePath);
        templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        servlet.templateEngine = templateEngine;
    }

    @Test
    void shouldReturnHtmlTable_whenDoGetCalled() throws IOException, ServletException {
        Lead lead1 = new Lead(UUID.randomUUID(), "lead1@test.com", "+1234560", "Company1", "NEW");
        Lead lead2 = new Lead(UUID.randomUUID(), "lead2@test.com", "+1234565", "Company2", "CONTACTED");
        List<Lead> leads = new ArrayList<>();
        leads.add(lead1);
        leads.add(lead2);

        when(mockContext.getAttribute("leadService")).thenReturn(mockService);
        when(mockService.findAll()).thenReturn(leads);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(printWriter);

        servlet.doGet(mockRequest, mockResponse);

        verify(mockService).findAll();

        String output = stringWriter.toString();
        assertTrue(output.contains("<table"));
        assertTrue(output.contains("<thead"));
        assertTrue(output.contains("<tbody>"));
        assertTrue(output.contains("lead1@test.com"));
        assertTrue(output.contains("lead2@test.com"));
        assertTrue(output.contains("+1234560"));
        assertTrue(output.contains("+1234565"));
        assertTrue(output.contains("Company1"));
        assertTrue(output.contains("Company2"));
        assertTrue(output.contains("NEW"));
        assertTrue(output.contains("CONTACTED"));
    }

    @Test
    void shouldSetContentTypeToHtml_whenDoGetCalled() throws IOException, ServletException {
        when(mockContext.getAttribute("leadService")).thenReturn(mockService);
        when(mockService.findAll()).thenReturn(new ArrayList<>());

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(printWriter);

        servlet.doGet(mockRequest, mockResponse);

        verify(mockResponse).setContentType("text/html; charset=UTF-8");
    }

    @Test
    void shouldInitializeTemplateEngine_whenInitCalled() throws Exception {
        LeadListServlet servlet = new LeadListServlet();

        servlet.init();

        assertThat(servlet.templateEngine).isNotNull();
    }
}