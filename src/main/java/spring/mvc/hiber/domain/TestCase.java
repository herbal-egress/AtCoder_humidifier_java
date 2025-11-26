package spring.mvc.hiber.domain;

import lombok.extern.slf4j.Slf4j;  // добавил: импорт для @Slf4j

/**
 * Неизменяемая запись для тестового случая.
 * Позволяет легко добавлять тесты без дублирования.
 */
@Slf4j  // добавил: аннотация @Slf4j для генерации log
public record TestCase(String input, String expectedOutput, String name) {

    /**
     * Проверяет прохождение теста.
     * @param actualOutput фактический результат.
     * @return true если passed.
     * Подробное объяснение: Сравнивает expected и actual; логирует процесс сравнения (INFO для успеха, WARNING для неудачи); возвращает boolean для использования в Main.
     */
    public boolean passed(String actualOutput) {
        // изменил: LOGGER.info -> log.info
        log.info("Проверка теста: expected=" + expectedOutput + ", actual=" + actualOutput);
        boolean isPassed = expectedOutput.equals(actualOutput);
        if (isPassed) {
            // изменил: LOGGER.info -> log.info
            log.info("Тест прошёл успешно");
        } else {
            // изменил: LOGGER.warning -> log.warn
            log.warn("Тест не прошёл: expected=" + expectedOutput + ", actual=" + actualOutput);
        }
        return isPassed;
    }
}