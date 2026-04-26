package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class AddressTest {

    @Test
    void shouldCreateAddress_whenValidData() {
        // Given
        Address address = new Address("Moscow", "Lenina", "123456");

        // Then
        assertThat(address.city()).isEqualTo("Moscow");
        assertThat(address.street()).isEqualTo("Lenina");
        assertThat(address.zip()).isEqualTo("123456");
    }

    @Test
    void shouldBeEqual_whenSameData() {
        // Given
        Address address1 = new Address("Moscow", "Lenina", "123456");
        Address address2 = new Address("Moscow", "Lenina", "123456");

        // Then
        assertThat(address1).isEqualTo(address2);
        assertThat(address1.hashCode()).isEqualTo(address2.hashCode());
    }

    @Test
    void shouldThrowException_whenCityIsNull() {
        assertThatThrownBy(() -> new Address(null, "Lenina", "123456"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("City cannot be blank");
    }

    @Test
    void shouldThrowException_whenZipIsBlank() {
        assertThatThrownBy(() -> new Address("Moscow", "Lenina", "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Zip cannot be blank");
    }
}