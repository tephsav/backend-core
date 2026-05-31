package ru.mentee.power.crm.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.service.LeadService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @GetMapping("/leads")
    public String showLeads(
            @RequestParam(required = false) String status,
            Model model
    ) {
        List<Lead> leads = (status == null)
                ? leadService.findAll()
                : leadService.findByStatus(status);
        model.addAttribute("leads", leads);
        model.addAttribute("currentFilter", status);
        return "leads/list";
    }
}