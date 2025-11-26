package spring.mvc.hiber.parser;

import lombok.extern.slf4j.Slf4j;
import spring.mvc.hiber.domain.InputEvent;

import java.util.List;

/**
 * Парсинг входной строки в список событий с валидацией.
 */

@Slf4j
public class InputParser {
    private static final String LINE_SEPARATOR = "\n";
    private static final String FIELD_SEPARATOR = "\\s+";
    private static final String INVALID_N_MSG = "Неверное N: должно быть >=1";
    private static final String INVALID_LINE_MSG = "Неверный формат строки в индексе %d";

    public List<InputEvent> parse(String inputStr) {
        try {
            if (inputStr == null || inputStr.trim().isEmpty()) {
                String errorMsg = "Входные данные не могут быть null или пустыми";
                log.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            log.info("Входные данные валидны, продолжаем split");
            String[] lines = inputStr.trim().split(LINE_SEPARATOR);
            if (lines.length < 2) {
                log.error(INVALID_N_MSG);
                throw new IllegalArgumentException(INVALID_N_MSG);
            }
            int n = Integer.parseInt(lines[0].trim());
            if (n < 1 || n != lines.length - 1) {
                log.error(INVALID_N_MSG);
                throw new IllegalArgumentException(INVALID_N_MSG);
            }
            List<InputEvent> events = parseEvents(lines, n);
            return events;
        } catch (NullPointerException e) {
            String errorMsg = "Неожиданный null в парсинге";
            log.error(errorMsg + ": " + e.getMessage());
            throw new IllegalArgumentException(errorMsg, e);
        } catch (NumberFormatException e) {
            String errorMsg = "Ошибка формата числа в входных данных";
            log.error(errorMsg + ": " + e.getMessage());
            throw new IllegalArgumentException(errorMsg, e);
        }
    }

    // Вспомогательные методы, парсят события с проверкой возрастаемости.
    private List<InputEvent> parseEvents(String[] lines, int n) {
        List<InputEvent> events = java.util.stream.IntStream.range(1, n + 1)
                .mapToObj(i -> parseSingleEvent(lines[i], i))
                .peek(event -> event.validate())
                .collect(java.util.stream.Collectors.toList());
        log.info("Парсинг завершён успешно. Количество доливов N=" + events.size());
        return events;
    }

    private InputEvent parseSingleEvent(String line, int index) {
        String[] parts = line.trim().split(FIELD_SEPARATOR);
        if (parts.length != 2) {
            String errorMsg = String.format(INVALID_LINE_MSG, index);
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        try {
            long time = Long.parseLong(parts[0].trim());
            long volume = Long.parseLong(parts[1].trim());
            return new InputEvent(time, volume);
        } catch (NumberFormatException e) {
            String errorMsg = String.format("Ошибка формата числа в строке %d", index);
            log.error(errorMsg + ": " + e.getMessage());
            throw new IllegalArgumentException(errorMsg, e);
        }
    }
}