package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class LeadTest {

    @Test
    void shouldCreateLead_whenValidData() {
        // Given
        Address address = new Address("Moscow", "Lenina", "123456");
        Contact contact = new Contact("john@example.com", "+79001234567", address);
        UUID id = UUID.randomUUID();

        // When
        Lead lead = new Lead(id, contact, "Acme Corp", "NEW");

        // Then
        assertThat(lead.contact()).isEqualTo(contact);
    }

    @Test
    void shouldAccessEmailThroughDelegation_whenLeadCreated() {
        // Given
        Address address = new Address("SPb", "Nevsky", "654321");
        Contact contact = new Contact("lead@example.com", "+79009876543", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "TechCorp", "CONTACTED");

        // When
        String email = lead.contact().email();
        String city = lead.contact().address().city();

        // Then
        assertThat(email).isEqualTo("lead@example.com");
        assertThat(city).isEqualTo("SPb");
    }

    @Test
    void shouldBeEqual_whenSameIdButDifferentContact() {
        // Given
        UUID sharedId = UUID.fromString("12345678-1234-1234-1234-123456789abc");
        Address address1 = new Address("Moscow", "Lenina", "123456");
        Address address2 = new Address("SPb", "Nevsky", "654321");
        Contact contact1 = new Contact("john@example.com", "+79001234567", address1);
        Contact contact2 = new Contact("jane@example.com", "+79009876543", address2);

        // When
        Lead lead1 = new Lead(sharedId, contact1, "Acme", "NEW");
        Lead lead2 = new Lead(sharedId, contact2, "Corp", "QUALIFIED");

        // Then - equals по всем полям
        assertThat(lead1).isNotEqualTo(lead2);
    }

    @Test
    void shouldThrowException_whenContactIsNull() {
        assertThatThrownBy(() -> new Lead(UUID.randomUUID(), null, "Acme", "NEW"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Contact cannot be null");
    }

    @Test
    void shouldThrowException_whenInvalidStatus() {
        assertThatThrownBy(() -> new Lead(UUID.randomUUID(),
                new Contact("john@example.com", "+79001234567",
                    new Address("Moscow", "Lenina", "123456")), "Acme", "INVALID"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid status");
    }

    @Test
    void shouldDemonstrateThreeLevelComposition_whenAccessingCity() {
        // Given
        Address address = new Address("Kazan", "Baumana", "111222");
        Contact contact = new Contact("kazan@example.com", "+78001234567", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "KazanCorp", "QUALIFIED");

        // When
        String city = lead.contact().address().city();

        // Then
        assertThat(city).isEqualTo("Kazan");
    }
}