package ru.mentee.power.crm.spring;

import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.service.LeadService;

import java.util.List;
import java.util.UUID;

public class MockLeadService extends LeadService {
    private final List<Lead> mockLeads;

    public MockLeadService() {
        super(null);
        this.mockLeads  = List.of(
                new Lead(UUID.randomUUID(), "test1@example.com", "+1234567890", "Company1", "NEW"),
                new Lead(UUID.randomUUID(), "test2@example.com", "+0987654321", "Company2", "CONTACTED")
        );
    }

    @Override
    public List<Lead> findAll() {
        return mockLeads;
    }
}
