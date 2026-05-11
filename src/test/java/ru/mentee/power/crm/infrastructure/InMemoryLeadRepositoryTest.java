package ru.mentee.power.crm.infrastructure;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class InMemoryLeadRepositoryTest {

    @Test
    void shouldAddLead_whenStorageIsEmpty() {
        Address address = new Address("Moscow", "Lenina", "123456");
        Contact contact = new Contact("john@example.com", "+79001234567", address);
        UUID id = UUID.randomUUID();
        Lead firstLead = new Lead(id, contact, "Acme Corp", "NEW");
        Repository<Lead> repository = new InMemoryLeadRepository();

        repository.add(firstLead);

        assertThat(repository.findAll()).hasSize(1);
        assertThat(repository.findById(id)).isEqualTo(Optional.of(firstLead));
    }

    @Test
    void shouldRejectAddLead_whenLeadIsExists() {
        Address address = new Address("Moscow", "Lenina", "123456");
        Contact contact = new Contact("john@example.com", "+79001234567", address);
        UUID id = UUID.randomUUID();
        Lead firstLead = new Lead(id, contact, "Acme Corp", "NEW");
        Lead secondLead = new Lead(id, contact, "Acme Corp", "NEW");
        Repository<Lead> repository = new InMemoryLeadRepository();

        repository.add(firstLead);
        repository.add(secondLead);

        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    void shouldReturnLead_whenIdIsExists() {
        Repository<Lead> repository = new InMemoryLeadRepository();
        UUID existingId = null;
        Lead existingLead = null;
        for (int i = 0; i < 10; i++) {
            Address address = new Address("city" + i, "Lenina" + i, "123456" + i);
            Contact contact = new Contact("contact" + i + "@example.com", "+79001234567" + i, address);
            UUID id = UUID.randomUUID();
            Lead lead = new Lead(id, contact, "Acme Corp" + i, List.of("NEW", "CONTACTED", "QUALIFIED").get(i % 3));
            repository.add(lead);

            if (i == 4) {
                existingId = id;
                existingLead = lead;
            }
        }

        Optional<Lead> foundLead = repository.findById(existingId);

        assertThat(foundLead).isPresent();
        assertThat(existingLead).isEqualTo(foundLead.orElse(null));
    }

    @Test
    void shouldReturnEmpty_whenIdNotExists() {
        Repository<Lead> repository = new InMemoryLeadRepository();
        for (int i = 0; i < 10; i++) {
            Address address = new Address("city" + i, "Lenina" + i, "123456" + i);
            Contact contact = new Contact("contact" + i + "@example.com", "+79001234567" + i, address);
            UUID id = UUID.randomUUID();
            Lead lead = new Lead(id, contact, "Acme Corp" + i, List.of("NEW", "CONTACTED", "QUALIFIED").get(i % 3));
            repository.add(lead);
        }

        Optional<Lead> foundLead = repository.findById(UUID.randomUUID());

        assertThat(foundLead).isEmpty();
    }

    @Test
    void shouldRemoveLead_whenIdExists() {
        Repository<Lead> repository = new InMemoryLeadRepository();
        UUID expectedId = null;
        for (int i = 0; i < 5; i++) {
            Address address = new Address("city" + i, "Lenina" + i, "123456" + i);
            Contact contact = new Contact("contact" + i + "@example.com", "+79001234567" + i, address);
            UUID id = UUID.randomUUID();
            Lead lead = new Lead(id, contact, "Acme Corp" + i, List.of("NEW", "CONTACTED", "QUALIFIED").get(i % 3));
            repository.add(lead);

            if (i == 2) {
                expectedId = id;
            }
        }

        repository.remove(expectedId);

        assertThat(repository.findAll()).hasSize(4);
        assertThat(repository.findById(expectedId)).isEmpty();
    }

    @Test
    void shouldReturnDefensiveCopy_whenFindAll() {
        Repository<Lead> repository = new InMemoryLeadRepository();
        int originalSize = 5;
        for (int i = 0; i < originalSize; i++) {
            Address address = new Address("city" + i, "Lenina" + i, "123456" + i);
            Contact contact = new Contact("contact" + i + "@example.com", "+79001234567" + i, address);
            UUID id = UUID.randomUUID();
            Lead lead = new Lead(id, contact, "Acme Corp" + i, List.of("NEW", "CONTACTED", "QUALIFIED").get(i % 3));
            repository.add(lead);
        }

        List<Lead> copiedStorage = repository.findAll();
        Address address = new Address("cityA", "streetA", "4586219");
        Contact contact = new Contact("contactA@example.com", "+72306373558", address);
        Lead newLead = new Lead(UUID.randomUUID(), contact, "Acme CorpA", "CONTACTED");
        copiedStorage.add(newLead);

        assertThat(repository.findAll()).hasSize(originalSize);
    }
}