package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

class CustomerTest {

    @Test
    void shouldReuseContact_whenCreatingCustomer() {
        // Given
        Address contactAddress = new Address("Moscow", "Lenina", "123456");
        Address billingAddress = new Address("SPb", "Nevsky", "654321");
        Contact contact = new Contact("john@example.com", "+79001234567", contactAddress);

        // When
        Customer customer = new Customer(UUID.randomUUID(), contact, billingAddress, "L3");

        // Then
        assertThat(customer.contact().address()).isNotEqualTo(customer.billingAddress());
    }

    @Test
    void shouldDemonstrateContactReuse_acrossLeadAndCustomer() {
        // Given
        Address address = new Address("Kazan", "Baumana", "111222");
        Contact sharedContact = new Contact("shared@example.com", "+78001234567", address);

        // When
        Lead lead = new Lead(UUID.randomUUID(), sharedContact, "TechCorp", "NEW");
        Customer customer = new Customer(UUID.randomUUID(), sharedContact, address, "L2");

        // Then
        assertThat(lead.contact()).isEqualTo(customer.contact());
    }
}