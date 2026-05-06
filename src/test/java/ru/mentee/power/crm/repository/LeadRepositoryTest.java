package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LeadRepositoryTest {
    private LeadRepository repository;

    @BeforeEach
    void setUp() {
        repository = new LeadRepository();
    }

    @Test
    void shouldSaveAndFindLeadById_whenLeadSaved() {
        // Given
        Lead lead = new Lead("lead-1", "ivan@mail.ru", "+7123", "TechCorp", "NEW");

        // When
        repository.save(lead);

        // Then
        Lead foundLead = repository.findById("lead-1");
        assertThat(foundLead).isNotNull();
    }

    @Test
    void shouldReturnNull_whenLeadNotFound() {
        // When
        Lead foundLead = repository.findById("lead-1");

        // Then
        assertThat(foundLead).isNull();
    }

    @Test
    void shouldReturnAllLeads_whenMultipleLeadsSaved() {
        // Given
        for (int i = 0; i < 3; i++) {
            Lead lead = new Lead("lead-" + i, "ivan" + i + "@mail.ru", "+7123" + i, "TechCorp", "NEW");
            repository.save(lead);
        }

        // When
        List<Lead> leads = repository.findAll();

        // Then
        assertThat(leads).hasSize(3);
    }

    @Test
    void shouldDeleteLead_whenLeadExists() {
        // Given
        String id = "lead-1";
        Lead lead = new Lead(id, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        repository.save(lead);

        // When
        repository.delete(id);

        // Then
        assertThat(repository.findById(id)).isNull();
        assertThat(repository.size()).isEqualTo(0);
    }

    @Test
    void shouldOverwriteLead_whenSaveWithSameId() {
        // Given
        Lead lead1 = new Lead("lead-1", "ivan@mail.ru", "+7123", "TechCorp", "NEW");
        repository.save(lead1);

        // When
        Lead lead2 = new Lead("lead-1", "petya@mail.ru", "+7123", "TechCorp", "NEW");
        repository.save(lead2);

        // Then
        assertThat(lead2).isEqualTo(repository.findById("lead-1"));
        assertThat(repository.size()).isEqualTo(1);
    }

    @Test
    void shouldFindFasterWithMap_thanWithListFilter() {
        // Given: Создать 1000 лидов
        List<Lead> leadList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Lead lead = new Lead("lead-" + i, "email" + i + "@test.com", "+7" + i, "Company" + i, "NEW");
            repository.save(lead);
            leadList.add(lead);
        }

        String targetId = "lead-500";  // Средний элемент

        // When: Поиск через Map
        long mapStart = System.nanoTime();
        Lead foundInMap = repository.findById(targetId);
        long mapDuration = System.nanoTime() - mapStart;

        // When: Поиск через List.stream().filter()
        long listStart = System.nanoTime();
        Lead foundInList = leadList.stream()
                .filter(lead -> lead.id().equals(targetId))
                .findFirst()
                .orElse(null);
        long listDuration = System.nanoTime() - listStart;

        // Then: Map должен быть минимум в 10 раз быстрее
        assertThat(foundInMap).isEqualTo(foundInList);
        assertThat(listDuration).isGreaterThan(mapDuration * 10);

        System.out.println("Map поиск: " + mapDuration + " ns");
        System.out.println("List поиск: " + listDuration + " ns");
        System.out.println("Ускорение: " + (listDuration / mapDuration) + "x");
    }

    @Test
    void shouldSaveBothLeads_evenWithSameEmailAndPhone_becauseRepositoryDoesNotCheckBusinessRules() {
        // Given: два lead с разными id, но одинаковыми контактами
        Lead originalLead = new Lead("lead-1", "ivan@mail.ru", "+79001234567", "Acme Corp", "NEW");
        Lead duplicateLead = new Lead("lead-2", "ivan@mail.ru", "+79001234567", "TechCorp", "HOT");

        // When: сохраняем оба
        repository.save(originalLead);
        repository.save(duplicateLead);

        // Then: Repository сохранил оба (это технически правильно!)
        assertThat(repository.size()).isEqualTo(2);

        // But: Бизнес недоволен — в CRM два контакта на одного человека
        // Решение: Service Layer в Sprint 5 будет проверять бизнес-правила
        // перед вызовом repository.save()
    }
}