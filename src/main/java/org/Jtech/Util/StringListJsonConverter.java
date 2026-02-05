package org.Jtech.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
/**
 * String List JSON Converter
 *
 * <p><b>Purpose:</b><br>
 * Converts a {@code List&lt;String&gt;} into a JSON string for database persistence
 * and converts a JSON string back into a {@code List&lt;String&gt;} for entity usage.
 * </p>
 *
 * <p><b>Scope:</b></p>
 * <ul>
 *   <li>Used with JPA entity fields via a converter annotation</li>
 *   <li>Commonly applied for attributes such as allergies, tags, and preferences</li>
 * </ul>
 *
 * <p><b>Notes:</b></p>
 * <ul>
 *   <li>Uses Jackson {@link com.fasterxml.jackson.databind.ObjectMapper}</li>
 *   <li>Stores empty lists as {@code []} to avoid null issues</li>
 *   <li>Throws {@link IllegalArgumentException} for malformed JSON</li>
 * </ul>
 *
 * <p><b>Metadata:</b></p>
 * <ul>
 *   <li>Added on: 2026-02-06</li>
 *   <li>Author: Mohit Singh</li>
 * </ul>
 */


@Converter
public class StringListJsonConverter implements AttributeConverter<List<String>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converts a {@code List&lt;String&gt;} into a JSON string for database storage.
     *
     * @param attribute the list of strings to persist
     * @return JSON representation of the list, or {@code []} if null or empty
     */
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]"; // Return an empty JSON array for null/empty lists
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting list to JSON: " + e.getMessage(), e);
        }
    }
    /**
     * Converts a JSON string from the database back into a {@code List&lt;String&gt;}.
     *
     * @param dbData JSON string stored in the database
     * @return parsed list of strings, or an empty list if input is null or empty
     */
    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>(); // Return an empty list for null/empty JSON
        }
        try {
            return objectMapper.readValue(dbData, List.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to list: " + e.getMessage(), e);
        }
    }
}
