package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LeadTest {

    @Test
    void shouldReturnId_whenGetIdCalled() {
        // Given
        UUID expectedId = UUID.randomUUID();
        Lead lead = new Lead(expectedId, "test@example.com", "+1234567890", "ACME Inc", "NEW");

        // When
        UUID id = lead.id();

        // Then
        assertThat(id).isEqualTo(expectedId);
    }

    @Test
    void shouldReturnEmail_whenGetEmailCalled() {
        // Given
        Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+1234567890", "ACME Inc", "NEW");

        // When
        String email = lead.email();

        // Then
        assertThat(email).isEqualTo("test@example.com");
    }

    @Test
    void shouldReturnPhone_whenGetPhoneCalled() {
        // Given
        Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+1234567890", "ACME Inc", "NEW");

        // When
        String phone = lead.phone();

        // Then
        assertThat(phone).isEqualTo("+1234567890");
    }

    @Test
    void shouldReturnCompany_whenGetCompanyCalled() {
        // Given
        Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+1234567890", "ACME Inc", "NEW");

        // When
        String company = lead.company();

        // Then
        assertThat(company).isEqualTo("ACME Inc");
    }

    @Test
    void shouldReturnStatus_whenGetStatusCalled() {
        // Given
        Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+1234567890", "ACME Inc", "NEW");

        // When
        String status = lead.status();

        // Then
        assertThat(status).isEqualTo("NEW");
    }

    @Test
    void shouldReturnFormattedString_whenToStringCalled() {
        // Given
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, "test@example.com", "+1234567890", "ACME Inc", "NEW");

        // When
        String result = lead.toString();

        // Then
        assertThat(result).contains(id.toString(), "test@example.com", "+1234567890", "ACME Inc", "NEW");
    }

    @Test
    void shouldCreateLead_whenValidData() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        Lead lead = new Lead(id, "test@example.com", "+1234567890", "ACME Inc", "NEW");

        // Then
        assertThat(lead.id()).isEqualTo(id);
    }

    @Test
    void shouldGenerateUniqueIds_whenMultipleLeads() {
        // Given
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Lead lead1 = new Lead(id1, "test1@example.com", "+1234567890", "ACME1 Inc", "NEW");
        Lead lead2 = new Lead(id2, "test2@example.com", "+0234567890", "ACME2 Inc", "OLD");

        // Then
        assertThat(lead1.id()).isNotEqualTo(lead2.id());
    }
}