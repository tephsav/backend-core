package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ContactTest {

    @Test
    void shouldCreateContact_whenValidData() {
        // Given
        Address address = new Address("Moscow", "Lenina", "123456");
        String email = "john@example.com";
        String phone = "+79001234567";

        // When
        Contact contact = new Contact(email, phone, address);

        // Then
        assertThat(contact.address()).isEqualTo(address);
        assertThat(contact.address().city()).isEqualTo("Moscow");
    }

    @Test
    void shouldDelegateToAddress_whenAccessingCity() {
        // Given
        Address address = new Address("Moscow", "Lenina", "123456");
        String email = "john@example.com";
        String phone = "+79001234567";

        // When
        Contact contact = new Contact(email, phone, address);

        // Then
        assertThat(contact.address().city()).isEqualTo("Moscow");
        assertThat(contact.address().street()).isEqualTo("Lenina");
    }

    @Test
    void shouldThrowException_whenAddressIsNull() {
        assertThatThrownBy(() -> new Contact("john@example.com", "+79001234567", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Address cannot be null");
    }
}