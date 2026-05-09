package ru.mentee.power.crm.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LeadRepositoryTest {

    @Test
    @DisplayName("Should automatically deduplicate leads by id")
    void shouldDeduplicateLeadsById() {
        LeadRepository repository = new LeadRepository();
        Address address = new Address("Moscow", "Lenina", "123456");
        Contact contact = new Contact("john@example.com", "+79001234567", address);
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, contact, "Acme Corp", "NEW");

        boolean result1 = repository.add(lead);
        boolean result2 = repository.add(lead);

        assertThat(repository.size()).isEqualTo(1);
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    @DisplayName("Should allow different leads with different ids")
    void shouldAllowDifferentLeads() {
        LeadRepository repository = new LeadRepository();
        Address address = new Address("Moscow", "Lenina", "123456");
        Contact contact = new Contact("john@example.com", "+79001234567", address);
        Lead lead1 = new Lead(UUID.randomUUID(), contact, "Acme Corp1", "NEW");
        Lead lead2 = new Lead(UUID.randomUUID(), contact, "Acme Corp2", "CONTACTED");

        boolean result1 = repository.add(lead1);
        boolean result2 = repository.add(lead2);

        assertThat(repository.size()).isEqualTo(2);
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
    }

    @Test
    @DisplayName("Should find existing lead through contains")
    void shouldFindExistingLead() {
        LeadRepository repository = new LeadRepository();
        Address address = new Address("Moscow", "Lenina", "123456");
        Contact contact = new Contact("john@example.com", "+79001234567", address);
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, contact, "Acme Corp", "NEW");
        repository.add(lead);

        boolean isExists = repository.contains(lead);

        assertThat(isExists).isTrue();
    }

    @Test
    @DisplayName("Should return unmodifiable set from findAll")
    void shouldReturnUnmodifiableSet() {
        LeadRepository repository = new LeadRepository();
        Address address = new Address("Moscow", "Lenina", "123456");
        Contact contact = new Contact("john@example.com", "+79001234567", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, "Acme Corp", "NEW");
        repository.add(lead);

        Set<Lead> leads = repository.findAll();

        assertThatThrownBy(() -> {
            Address address1 = new Address("Moscow", "Lenina", "123456");
            Contact contact1 = new Contact("john@example.com", "+79001234567", address1);
            UUID id = UUID.randomUUID();
            Lead lead1 = new Lead(id, contact1, "Acme Corp", "NEW");
            leads.add(lead1);
        }).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Should perform contains() faster than ArrayList")
    void shouldPerformFasterThanArrayList() {
        Set<Lead> setLeads = new HashSet<>();
        List<Lead> listLeads = new ArrayList<>();
        Address address = new Address("Moscow", "Lenina", "123456");
        Contact contact = new Contact("john@example.com", "+79001234567", address);
        for (int i = 0; i < 10_000; i++) {
            Lead lead = new Lead(UUID.randomUUID(), contact, "Acme Corp", "NEW");
            setLeads.add(lead);
            listLeads.add(lead);
        }
        Address address1 = new Address("Moscow", "Lenina", "123456");
        Contact contact1 = new Contact("john@example.com", "+79001234567", address1);
        UUID id1 = UUID.randomUUID();
        Lead findLead = new Lead(id1, contact1, "Acme Corp", "NEW");
        long startSet = System.nanoTime();
        for (int i = 0; i < 1_000; i++) {
            setLeads.contains(findLead);
        }
        long durationSet = System.nanoTime() - startSet;
        long startList = System.nanoTime();
        for (int i = 0; i < 1_000; i++) {
            listLeads.contains(findLead);
        }
        long durationList = System.nanoTime() - startList;

        assertThat(durationList).isGreaterThan(durationSet * 100);
    }
}