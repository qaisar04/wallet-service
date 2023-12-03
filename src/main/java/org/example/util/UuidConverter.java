package org.example.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;
import java.util.UUID;

/**
 * The converter class. Autoconverting uuid to string for database columns
 */
@Converter
public class UuidConverter implements AttributeConverter<UUID, String> {

    /**
     * @param entityValue entity uuid
     * @return converted string
     */
    @Override
    public String convertToDatabaseColumn(final UUID entityValue) {
        return Objects.requireNonNull(entityValue).toString();
    }

    /**
     * @param databaseValue database value
     * @return converted uuid
     */
    @Override
    public UUID convertToEntityAttribute(final String databaseValue) {
        return UUID.fromString(Objects.requireNonNull(databaseValue));
    }
}
