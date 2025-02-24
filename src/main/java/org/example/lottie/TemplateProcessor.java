package org.example.lottie;

import java.util.Map;

/**
 * Класс для обработки шаблонов с использованием заданных плейсхолдеров.
 * Позволяет избежать хард-кодинга при замене значений.
 */
public class TemplateProcessor {

    /**
     * Заменяет все плейсхолдеры в шаблоне значениями из переданной карты.
     * Формат плейсхолдера: {{ключ}}
     *
     * @param template сходный шаблон с плейсхолдерами.
     * @param values   Карта значений для замены, где ключ – имя плейсхолдера без фигурных скобок.
     * @return Строка с произведённой заменой.
     */
    public String process(String template, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            template = template.replace(placeholder, entry.getValue());
        }
        return template;
    }
}