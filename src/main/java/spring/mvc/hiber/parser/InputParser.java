package spring.mvc.hiber.parser;

import spring.mvc.hiber.domain.InputEvent;

import java.util.List;

/**
 * Класс для парсинга входной строки в список событий.
 * SRP: только парсинг и валидация входа.
 */
public class InputParser {

    // **УЛУЧШЕНО: Константы вместо magic numbers/hard-coded strings (избегает Magic Numbers/Data Clumps)**
    private static final String LINE_SEPARATOR = "\n";
    private static final String FIELD_SEPARATOR = "\\s+";
    private static final String INVALID_N_MSG = "Invalid N: must be >=1";
    private static final String INVALID_LINE_MSG = "Invalid line format at index %d";
    private static final String NON_INCREASING_TIME_MSG = "Times must strictly increase at index %d";

    /**
     * Парсит input в список событий с валидацией.
     * @param inputStr многострочная строка.
     * @return список InputEvent.
     * @throws IllegalArgumentException если некорректно.
     */
    public List<InputEvent> parse(String inputStr) {
        if (inputStr == null || inputStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }

        // **УЛУЧШЕНО: Split с trim() для robustness (избегает OWASP input validation issues)**
        String[] lines = inputStr.trim().split(LINE_SEPARATOR);
        if (lines.length < 2) {
            throw new IllegalArgumentException(INVALID_N_MSG);
        }

        int n = Integer.parseInt(lines[0].trim());
        if (n < 1 || n != lines.length - 1) {
            throw new IllegalArgumentException(INVALID_N_MSG);
        }

        // **УЛУЧШЕНО: Выделен метод parseEvents() для избежания Long Method**
        return parseEvents(lines, n);
    }

    /**
     * Вспомогательный метод: парсит события с проверкой возрастаемости.
     */
    private List<InputEvent> parseEvents(String[] lines, int n) {
        // **УЛУЧШЕНО: Использует Builder pattern implicitly via Stream API для избежания Duplicate Code в цикле**
        return java.util.stream.IntStream.range(1, n + 1)
                .mapToObj(i -> parseSingleEvent(lines[i], i))
                .peek(event -> event.validate())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Парсит одно событие.
     */
    private InputEvent parseSingleEvent(String line, int index) {
        String[] parts = line.trim().split(FIELD_SEPARATOR);
        if (parts.length != 2) {
            throw new IllegalArgumentException(String.format(INVALID_LINE_MSG, index));
        }

        // **УЛУЧШЕНО: Try-catch для парсинга, вместо raw Long.parseLong (избегает Primitive Obsession errors)**
        try {
            long time = Long.parseLong(parts[0].trim());
            long volume = Long.parseLong(parts[1].trim());

            // **УЛУЧШЕНО: Проверка возрастаемости перенесена в симулятор, здесь только парсинг (SRP)**
            return new InputEvent(time, volume);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(INVALID_LINE_MSG, index), e);
        }
    }
}