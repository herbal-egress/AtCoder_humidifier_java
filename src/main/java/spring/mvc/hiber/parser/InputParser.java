package spring.mvc.hiber.parser;

import lombok.extern.slf4j.Slf4j;  // добавил: импорт для @Slf4j
import spring.mvc.hiber.domain.InputEvent;

import java.util.List;

/**
 * Класс для парсинга входной строки в список событий.
 * SRP: только парсинг и валидация входа.
 */
@Slf4j  // добавил: аннотация @Slf4j для генерации log
public class InputParser {

    private static final String LINE_SEPARATOR = "\n";
    private static final String FIELD_SEPARATOR = "\\s+";
    private static final String INVALID_N_MSG = "Неверное N: должно быть >=1";
    private static final String INVALID_LINE_MSG = "Неверный формат строки в индексе %d";

    /**
     * Парсит input в список событий с валидацией.
     * @param inputStr многострочная строка.
     * @return список InputEvent.
     * @throws IllegalArgumentException если некорректно (тексты на русском).
     * Подробное объяснение: Проверяет input на null/пустоту; splits строки; парсит N; проверяет длину; вызывает parseEvents; логирует каждый шаг; обрабатывает предвидимые ошибки (NullPointerException внутри try-catch для безопасности OWASP).
     */
    public List<InputEvent> parse(String inputStr) {
        // изменил: LOGGER.info -> log.info
        log.info("Начало парсинга входных данных");
        try {
            if (inputStr == null || inputStr.trim().isEmpty()) {
                String errorMsg = "Входные данные не могут быть null или пустыми";
                // изменил: LOGGER.severe -> log.error
                log.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            // изменил: LOGGER.info -> log.info
            log.info("Входные данные валидны, продолжаем split");
            String[] lines = inputStr.trim().split(LINE_SEPARATOR);
            if (lines.length < 2) {
                // изменил: LOGGER.severe -> log.error
                log.error(INVALID_N_MSG);
                throw new IllegalArgumentException(INVALID_N_MSG);
            }

            int n = Integer.parseInt(lines[0].trim());
            if (n < 1 || n != lines.length - 1) {
                // изменил: LOGGER.severe -> log.error
                log.error(INVALID_N_MSG);
                throw new IllegalArgumentException(INVALID_N_MSG);
            }

            // изменил: LOGGER.info -> log.info
            log.info("N=" + n + ", начинаем парсинг событий");
            List<InputEvent> events = parseEvents(lines, n);
            // изменил: LOGGER.info -> log.info
            log.info("Парсинг завершён успешно, событий: " + events.size());
            return events;
        } catch (NullPointerException e) {
            String errorMsg = "Неожиданный null в парсинге";
            // изменил: LOGGER.severe -> log.error
            log.error(errorMsg + ": " + e.getMessage());
            throw new IllegalArgumentException(errorMsg, e);
        } catch (NumberFormatException e) {
            String errorMsg = "Ошибка формата числа в входных данных";
            // изменил: LOGGER.severe -> log.error
            log.error(errorMsg + ": " + e.getMessage());
            throw new IllegalArgumentException(errorMsg, e);
        }
    }

    /**
     * Вспомогательный метод: парсит события с проверкой возрастаемости.
     * Подробное объяснение: Использует Stream для парсинга каждой строки; вызывает parseSingleEvent; валидирует событие; собирает в List; логирует количество событий.
     */
    private List<InputEvent> parseEvents(String[] lines, int n) {
        // изменил: LOGGER.info -> log.info
        log.info("Парсинг " + n + " событий");
        List<InputEvent> events = java.util.stream.IntStream.range(1, n + 1)
                .mapToObj(i -> parseSingleEvent(lines[i], i))
                .peek(event -> event.validate())
                .collect(java.util.stream.Collectors.toList());
        // изменил: LOGGER.info -> log.info
        log.info("События спарсены: " + events.size());
        return events;
    }

    /**
     * Парсит одно событие.
     * Подробное объяснение: Trim и split строки; проверка на 2 части; парсинг long; создание InputEvent; try-catch для NumberFormatException (предвидимая ошибка); логирует парсинг.
     */
    private InputEvent parseSingleEvent(String line, int index) {
        // изменил: LOGGER.info -> log.info
        log.info("Парсинг события в строке " + index + ": " + line);
        String[] parts = line.trim().split(FIELD_SEPARATOR);
        if (parts.length != 2) {
            String errorMsg = String.format(INVALID_LINE_MSG, index);
            // изменил: LOGGER.severe -> log.error
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        try {
            long time = Long.parseLong(parts[0].trim());
            long volume = Long.parseLong(parts[1].trim());
            // изменил: LOGGER.info -> log.info
            log.info("Событие спарсено: time=" + time + ", volume=" + volume);
            return new InputEvent(time, volume);
        } catch (NumberFormatException e) {
            String errorMsg = String.format("Ошибка формата числа в строке %d", index);
            // изменил: LOGGER.severe -> log.error
            log.error(errorMsg + ": " + e.getMessage());
            throw new IllegalArgumentException(errorMsg, e);
        }
    }
}