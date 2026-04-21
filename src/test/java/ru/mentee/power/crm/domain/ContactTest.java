package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ContactTest {

    @Test
    void shouldCreateContact_whenValidData() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        String email = "john@example.com";

        // When
        Contact contact = new Contact(firstName, lastName, email);

        // Then
        assertThat(contact.firstName()).isEqualTo(firstName);
        assertThat(contact.lastName()).isEqualTo(lastName);
        assertThat(contact.email()).isEqualTo(email);
    }

    @Test
    void shouldBeEqual_whenSameData() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        String email = "john@example.com";
        Contact contact1 = new Contact(firstName, lastName, email);
        Contact contact2 = new Contact(firstName, lastName, email);

        // Then
        assertThat(contact1).isEqualTo(contact2);
        assertThat(contact1.hashCode()).isEqualTo(contact2.hashCode());
    }

    @Test
    void shouldNotBeEqual_whenDifferentData() {
        // Given
        Contact contact1 = new Contact("John", "Doe", "john@example.com");
        Contact contact2 = new Contact("Jane", "Liy", "jane@example.com");

        // Then
        assertThat(contact1).isNotEqualTo(contact2);
    }
}