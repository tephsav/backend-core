package ru.mentee.power.crm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @ParameterizedTest
    @CsvSource({"NEW,3", "CONTACTED,5", "QUALIFIED,2"})
    void shouldReturnOnlyLeads_whenFindByStatus(String status, int count) {
        service.addLead("one@example.com", "+1234560", "Company 1", "NEW");
        service.addLead("two@example.com", "+1234561", "Company 2", "NEW");
        service.addLead("three@example.com", "+1234562", "Company 3", "NEW");
        service.addLead("four@example.com", "+1234563", "Company 4", "CONTACTED");
        service.addLead("five@example.com", "+1234564", "Company 5", "CONTACTED");
        service.addLead("six@example.com", "+1234565", "Company 6", "CONTACTED");
        service.addLead("seven@example.com", "+1234566", "Company 7", "CONTACTED");
        service.addLead("eight@example.com", "+1234567", "Company 8", "CONTACTED");
        service.addLead("nine@example.com", "+1234568", "Company 9", "QUALIFIED");
        service.addLead("ten@example.com", "+1234569", "Company 10", "QUALIFIED");

        List<Lead> result = service.findByStatus(status);

        assertThat(result).hasSize(count);
        assertThat(result).allMatch(lead -> lead.status().equals(status));
    }

    @Test
    void shouldReturnEmptyList_whenNoLeadsWithStatusNew() {
        service.addLead("one@example.com", "+1234560", "Company 1", "QUALIFIED");
        service.addLead("two@example.com", "+1234561", "Company 2", "QUALIFIED");
        service.addLead("three@example.com", "+1234562", "Company 3", "CONTACTED");
        service.addLead("four@example.com", "+1234563", "Company 4", "CONTACTED");
        service.addLead("five@example.com", "+1234564", "Company 5", "CONTACTED");
        service.addLead("six@example.com", "+1234565", "Company 6", "QUALIFIED");

        List<Lead> result = service.findByStatus("NEW");

        assertThat(result).hasSize(0);
    }

    @Test
    void shouldReturnEmptyList_whenNoLeadsWithStatusContacted() {
        service.addLead("one@example.com", "+1234560", "Company 1", "NEW");
        service.addLead("two@example.com", "+1234561", "Company 2", "NEW");
        service.addLead("three@example.com", "+1234562", "Company 3", "QUALIFIED");
        service.addLead("four@example.com", "+1234563", "Company 4", "QUALIFIED");
        service.addLead("five@example.com", "+1234564", "Company 5", "QUALIFIED");

        List<Lead> result = service.findByStatus("CONTACTED");

        assertThat(result).hasSize(0);
    }

    @Test
    void shouldReturnEmptyList_whenNoLeadsWithStatusQualified() {
        service.addLead("one@example.com", "+1234560", "Company 1", "NEW");
        service.addLead("two@example.com", "+1234561", "Company 2", "NEW");
        service.addLead("three@example.com", "+1234562", "Company 3", "CONTACTED");
        service.addLead("five@example.com", "+1234564", "Company 5", "CONTACTED");
        service.addLead("six@example.com", "+1234565", "Company 6", "NEW");

        List<Lead> result = service.findByStatus("QUALIFIED");

        assertThat(result).hasSize(0);
    }
}