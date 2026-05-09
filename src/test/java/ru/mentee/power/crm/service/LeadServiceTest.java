package ru.mentee.power.crm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.repository.LeadRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class LeadServiceTest {
    private LeadService service;
    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new LeadRepository();
        service = new LeadService(repository);
    }

    @Test
    void shouldCreateLead_whenEmailIsUnique() {
        String email = "test@example.com";
        String phone = "+123456";
        String company = "Test Company";
        String status = "NEW";

        Lead result = service.addLead(email, phone, company, status);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(email);
        assertThat(result.phone()).isEqualTo(phone);
        assertThat(result.company()).isEqualTo(company);
        assertThat(result.status()).isEqualTo(status);
        assertThat(result.id()).isNotNull();
    }

    @Test
    void shouldThrowException_whenEmailAlreadyExists() {
        String email = "duplicate@example.com";
        service.addLead(email, "+123456", "First Company", "NEW");

        assertThatThrownBy(() ->
                service.addLead(email, "+654321", "Second Company", "NEW")
        )
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Lead with email already exists");
    }

    @Test
    void shouldFindAllLeads() {
        service.addLead("one@example.com", "+123456", "Company 1", "NEW");
        service.addLead("two@example.com", "+213456", "Company 2", "CONTACTED");

        List<Lead> result = service.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldFindLeadById() {
        Lead created = service.addLead("find@example.com", "+123456", "Company", "NEW");

        Optional<Lead> result = service.findById(created.id());

        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo("find@example.com");
    }

    @Test
    void shouldFindLeadByEmail() {
        service.addLead("search@example.com", "+123456", "Company", "NEW");

        Optional<Lead> result = service.findByEmail("search@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().company()).isEqualTo("Company");
    }

    @Test
    void shouldReturnEmpty_whenLeadNotFound() {
        Optional<Lead> result = service.findByEmail("nonexistent@example.com");

        assertThat(result).isEmpty();
    }
}