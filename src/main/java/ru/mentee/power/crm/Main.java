package ru.mentee.power.crm;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.servlet.LeadListServlet;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        LeadRepository leadRepository = new LeadRepository();
        LeadService leadService = new LeadService(leadRepository);

        for(int i = 0; i < 5; i++) {
            leadService.addLead("lead" + i + "@test.com", "+12345" + i, "Company" + i, "NEW");
        }
        leadService.addLead("<script>alert('XSS')</script>", "+000000", "SomeCompany", "NEW");

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();

        Context context = tomcat.addContext("", new File(".").getAbsolutePath());
        context.getServletContext().setAttribute("leadService", leadService);
        tomcat.addServlet(context, "LeadListServlet", new LeadListServlet());
        context.addServletMappingDecoded("/leads", "LeadListServlet");

        tomcat.start();
        System.out.println("Tomcat started on port 8080");
        System.out.println("Open http://localhost:8080/leads in browser");
        tomcat.getServer().await();
    }
}