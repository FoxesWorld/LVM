package org.foxesworld.lvm;

import java.util.Map;
import java.util.Objects;

/**
 * A utility class for processing templates by replacing placeholders with provided values.
 * <p>
 * This class enables dynamic content generation by substituting placeholders in a template with actual values.
 * Placeholders should be in the format: {{key}}.
 * </p>
 */
public class TemplateProcessor {

    /**
     * Processes the provided template by replacing all placeholders with corresponding values from the given map.
     * <p>
     * Placeholders in the template should be in the format: {{key}}. If a value in the map is {@code null},
     * it will be replaced with an empty string.
     * </p>
     *
     * @param template the template string containing placeholders; must not be {@code null}
     * @param values   a map of values for replacement where the key corresponds to the placeholder name (without curly braces); must not be {@code null}
     * @return a string with all placeholders replaced with their corresponding values
     * @throws NullPointerException if {@code template} or {@code values} is {@code null}
     */
    public String process(String template, Map<String, String> values) {
        Objects.requireNonNull(template, "Template must not be null");
        Objects.requireNonNull(values, "Values map must not be null");

        for (Map.Entry<String, String> entry : values.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue() : "";
            template = template.replace(placeholder, value);
        }
        return template;
    }
}
