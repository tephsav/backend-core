package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class LeadRepositoryTest {
    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new LeadRepository();
    }

    @Test
    void shouldSaveAndFindLeadById_whenLeadSaved() {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");

        repository.save(lead);

        Optional<Lead> foundLead = repository.findById(id);
        assertThat(foundLead).isNotNull();
    }

    @Test
    void shouldReturnNull_whenLeadNotFound() {
        Optional<Lead> foundLead = repository.findById(UUID.randomUUID());

        assertThat(foundLead).isEmpty();
    }

    @Test
    void shouldReturnAllLeads_whenMultipleLeadsSaved() {
        for (int i = 0; i < 3; i++) {
            Lead lead = new Lead(UUID.randomUUID(), "ivan" + i + "@mail.ru", "+7123" + i, "TechCorp", "NEW");
            repository.save(lead);
        }

        List<Lead> leads = repository.findAll();

        assertThat(leads).hasSize(3);
    }

    @Test
    void shouldDeleteLead_whenLeadExists() {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        repository.save(lead);

        repository.delete(id);

        assertThat(repository.findById(id)).isEmpty();
        assertThat(repository.size()).isZero();
    }

    @Test
    void shouldOverwriteLead_whenSaveWithSameId() {
        UUID sharedId = UUID.randomUUID();
        Lead lead1 = new Lead(sharedId, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        repository.save(lead1);

        Lead lead2 = new Lead(sharedId, "petya@mail.ru", "+7123", "TechCorp", "NEW");
        repository.save(lead2);

        assertThat(lead2).isEqualTo(repository.findById(sharedId).orElse(null));
        assertThat(repository.size()).isEqualTo(1);
    }

    @Test
    void shouldFindFasterWithMap_thanWithListFilter() {
        List<Lead> leadList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            UUID id = UUID.randomUUID();
            Lead lead = new Lead(id, "email" + i + "@test.com", "+7" + i, "Company" + i, "NEW");
            repository.save(lead);
            leadList.add(lead);
        }
        UUID targetId = leadList.get(500).id();

        long mapStart = System.nanoTime();
        Optional<Lead> foundInMap = repository.findById(targetId);
        long mapDuration = System.nanoTime() - mapStart;
        long listStart = System.nanoTime();
        Optional<Lead> foundInList = leadList.stream()
                .filter(lead -> lead.id().equals(targetId))
                .findFirst();
        long listDuration = System.nanoTime() - listStart;

        assertThat(foundInMap).isEqualTo(foundInList);
        assertThat(listDuration).isGreaterThan(mapDuration * 10);
    }

    @Test
    void shouldSaveBothLeads_evenWithSameEmailAndPhone_becauseRepositoryDoesNotCheckBusinessRules() {
        Lead originalLead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+79001234567", "Acme Corp", "NEW");
        Lead duplicateLead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+79001234567", "TechCorp", "HOT");
        repository.save(originalLead);
        repository.save(duplicateLead);

        assertThat(repository.size()).isEqualTo(2);
    }
}