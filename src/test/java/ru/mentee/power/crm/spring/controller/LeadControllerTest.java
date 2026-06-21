package ru.mentee.power.crm.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.service.LeadService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class LeadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeadService leadService;

    @Test
    void shouldReturnStatus200OK_whenGetLeads() throws Exception {
        mockMvc.perform(get("/leads"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Email")));
    }

    @Test
    void shouldReturnFilteredLeads_whenStatusParam() throws Exception {
        when(leadService.findByStatus("NEW")).thenReturn(List.of(
                new Lead(UUID.randomUUID(), "one@example.com", "+1234560", "Company 1", "NEW")
        ));

        mockMvc.perform(get("/leads").param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Показаны лиды со статусом: NEW")))
                .andExpect(content().string(containsString("one@example.com")));
    }

    @Test
    void shouldReturnAddForm_whenGetLeadsNew() throws Exception {
        mockMvc.perform(get("/leads/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("leads/create"));
    }

    @Test
    void shouldRedirect_whenLeadAdded() throws Exception {
        mockMvc.perform(post("/leads")
                .param("email", "user1@test.com")
                .param("phone", "+123456789")
                .param("company", "Test Company name")
                .param("status", "NEW"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));
    }

    @Test
    void shouldShowEditForm() throws Exception {
        UUID id = UUID.randomUUID();
        Lead lead = new Lead(id, "testLead@test.com", "+123789456","Test Company", "NEW");
        when(leadService.findById(id)).thenReturn(Optional.of(lead));

        mockMvc.perform(get("/leads/" + id + "/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("lead", lead))
                .andExpect(view().name("leads/edit"));
    }

    @Test
    void shouldUpdateLead() throws Exception {
        UUID id = UUID.randomUUID();
        Lead updatedLead = new Lead(id, "newLead@test.com", "+111999666", "Microsoft", "CONTACTED");

        when(leadService.update(id, updatedLead)).thenReturn(updatedLead);

        mockMvc.perform(post("/leads/" + id)
                .param("email", updatedLead.email())
                .param("phone", updatedLead.phone())
                .param("company", updatedLead.company())
                .param("status", updatedLead.status()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));

        verify(leadService).update(id, updatedLead);
    }

    @Test
    void shouldReturn404ForNonexistentId() throws Exception {
        mockMvc.perform(get("/leads/" + UUID.randomUUID() + "/edit"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteLeadAndRedirect() throws Exception {
        UUID id = UUID.randomUUID();
        Lead deletedLead = new Lead(id, "deleteLead@test.com", "+123456789", "Test Company", "CONTACTED");

        when(leadService.findById(id)).thenReturn(Optional.of(deletedLead));

        mockMvc.perform(post("/leads/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"));

        verify(leadService).delete(id);
    }

    @Test
    void shouldReturn404ForNonexistentLead() throws Exception {
        UUID id = UUID.randomUUID();

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found"))
                .when(leadService).delete(id);

        mockMvc.perform(post("/leads/{id}/delete", id))
                .andExpect(status().isNotFound());

        verify(leadService).delete(id);
    }
}