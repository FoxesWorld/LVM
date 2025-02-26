package org.foxesworld.lvm.html;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;

/**
 * A utility class for processing FreeMarker templates.
 * <p>
 * This class compiles the provided template and renders it by replacing
 * placeholders (using FreeMarker syntax) with the actual values from the provided map.
 * </p>
 *
 * Note: The FreeMarker version used is 2.3.32.
 */
public class TemplateProcessor {

    private final Configuration configuration;

    /**
     * Constructs a new TemplateProcessor with default FreeMarker configuration.
     */
    public TemplateProcessor() {
        // Создаем конфигурацию FreeMarker для версии 2.3.32
        configuration = new Configuration(new Version("2.3.32"));
        configuration.setDefaultEncoding("UTF-8");
        // Дополнительные настройки конфигурации можно добавить здесь при необходимости
    }

    /**
     * Processes the provided FreeMarker template by replacing all placeholders with corresponding values
     * from the given map.
     * <p>
     * Placeholders in the template should use FreeMarker syntax, for example: ${containerId}.
     * </p>
     *
     * @param templateContent the template string containing FreeMarker placeholders; must not be {@code null}
     * @param values          a map of values for replacement; must not be {@code null}
     * @return a string with all placeholders replaced with their corresponding values
     * @throws NullPointerException if {@code templateContent} or {@code values} is {@code null}
     * @throws RuntimeException     if an error occurs during template processing
     */
    public String process(String templateContent, Map<String, Object> values) {
        Objects.requireNonNull(templateContent, "Template must not be null");
        Objects.requireNonNull(values, "Values map must not be null");

        try {
            Template template = new Template("template", new StringReader(templateContent), configuration);
            StringWriter writer = new StringWriter();
            template.process(values, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException("Error processing template", e);
        }
    }
}
