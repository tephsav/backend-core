package ru.mentee.power.crm.storage;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class LeadStorageTest {

    @Test
    void shouldAddLead_whenLeadIsUnique() {
        // Given
        LeadStorage storage = new LeadStorage();
        UUID id = UUID.randomUUID();
        Lead uniqueLead = new Lead(id, new Contact("ivan@mail.ru", "+7123", new Address("Moscow", "Lenina", "123456")), "TechCorp", "NEW");

        // When
        boolean added = storage.add(uniqueLead);

        // Then
        assertThat(added).isTrue();
        assertThat(storage.size()).isEqualTo(1);
        assertThat(storage.findAll()).containsExactly(uniqueLead);
    }

    @Test
    void shouldRejectDuplicate_whenEmailAlreadyExists() {
        // Given
        LeadStorage storage = new LeadStorage();
        UUID existingId = UUID.randomUUID();
        Lead existingLead = new Lead(existingId, new Contact("ivan@mail.ru", "+7123", new Address("Moscow", "Lenina", "123456")), "TechCorp", "NEW");
        Lead duplicateLead = new Lead(UUID.randomUUID(), new Contact("ivan@mail.ru", "+7456", new Address("SPb", "Nevsky", "654321")), "Other", "NEW");
        storage.add(existingLead);

        // When
        boolean added = storage.add(duplicateLead);

        // Then
        assertThat(added).isFalse();
        assertThat(storage.size()).isEqualTo(1);
        assertThat(storage.findAll()).containsExactly(existingLead);
    }

    @Test
    void shouldThrowException_whenStorageIsFull() {
        // Given
        LeadStorage storage = new LeadStorage();
        for (int i = 0; i < 100; i++) {
            Lead lead = new Lead(UUID.randomUUID(), new Contact("lead" + i + "@mail.ru", "+7000", new Address("Moscow", "Lenina", "123456")), "Company", "NEW");
            storage.add(lead);
        }

        // When
        Lead hundredFirstLead = new Lead(UUID.randomUUID(), new Contact("lead101@mail.ru", "+7001", new Address("SPb", "Nevsky", "654321")), "Company", "NEW");

        // Then
        assertThatThrownBy(() -> storage.add(hundredFirstLead))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Storage is full");
    }

    @Test
    void shouldReturnOnlyAddedLeads_whenFindAllCalled() {
        // Given
        LeadStorage storage = new LeadStorage();
        UUID firstId = UUID.randomUUID();
        Lead firstLead = new Lead(firstId, new Contact("ivan@mail.ru", "+7123", new Address("Moscow", "Lenina", "123456")), "TechCorp", "NEW");
        Lead secondLead = new Lead(UUID.randomUUID(), new Contact("maria@startup.io", "+7456", new Address("SPb", "Nevsky", "654321")), "StartupLab", "NEW");
        storage.add(firstLead);
        storage.add(secondLead);

        // When
        Lead[] result = storage.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(firstLead, secondLead);
    }
}