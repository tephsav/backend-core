package ru.mentee.power.crm.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.repository.LeadRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LeadService {
    private static final Logger log = LoggerFactory.getLogger(LeadService.class);
    private final LeadRepository repository;

    public LeadService(LeadRepository repository) {
        this.repository = repository;
        log.info("LeadService constructor called");
    }

    @PostConstruct
    void init() {
        log.info("LeadService @PostConstruct init() called - Bean lifecycle phase");
    }

    public Lead addLead(String email, String phone, String company, String status) {
        Optional<Lead> existing = repository.findByEmail(email);
        if (existing.isPresent()) {
            throw new IllegalStateException("Lead with email already exists: " + email);
        }

        Lead lead = new Lead(UUID.randomUUID(), email, phone, company, status);
        return repository.save(lead);
    }

    public List<Lead> findAll() {
        return repository.findAll();
    }

    public List<Lead> findByStatus(String status) {
        return repository.findAll().stream()
                .filter(lead -> lead.status().equals(status))
                .collect(Collectors.toList());
    }

    public Optional<Lead> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Lead> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Lead update(UUID id, Lead updatedLead) {
        Lead foundLead = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found"));
        Lead savingLead = new Lead(
                id,
                updatedLead.email(),
                updatedLead.phone(),
                updatedLead.company(),
                updatedLead.status()
        );

        return repository.save(savingLead);
    }
}