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

        leadService.addLead("one@example.com", "+1234560", "Company 1", "NEW");
        leadService.addLead("two@example.com", "+1234561", "Company 2", "NEW");
        leadService.addLead("three@example.com", "+1234562", "Company 3", "NEW");
        leadService.addLead("four@example.com", "+1234563", "Company 4", "CONTACTED");
        leadService.addLead("five@example.com", "+1234564", "Company 5", "CONTACTED");
        leadService.addLead("six@example.com", "+1234565", "Company 6", "CONTACTED");
        leadService.addLead("seven@example.com", "+1234566", "Company 7", "CONTACTED");
        leadService.addLead("eight@example.com", "+1234567", "Company 8", "CONTACTED");
        leadService.addLead("nine@example.com", "+1234568", "Company 9", "QUALIFIED");
        leadService.addLead("ten@example.com", "+1234569", "Company 10", "QUALIFIED");
        leadService.addLead("<script>alert('XSS')</script>", "+000000", "SomeCompany", "NEW");
    }
}