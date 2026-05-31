package ru.mentee.power.crm.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.service.LeadService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}