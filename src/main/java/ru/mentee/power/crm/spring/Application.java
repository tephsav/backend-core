package ru.mentee.power.crm.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.mentee.power.crm.service.LeadService;

@SpringBootApplication(scanBasePackages = "ru.mentee.power.crm")
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        LeadService leadService = context.getBean(LeadService.class);

        for(int i = 0; i < 5; i++) {
            leadService.addLead("lead" + i + "@test.com", "+12345" + i, "Company" + i, "NEW");
        }
        leadService.addLead("<script>alert('XSS')</script>", "+000000", "SomeCompany", "NEW");
    }
}