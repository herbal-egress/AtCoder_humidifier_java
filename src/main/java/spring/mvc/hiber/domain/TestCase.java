package spring.mvc.hiber.domain;

import lombok.extern.slf4j.Slf4j;

/**
 * Неизменяемая сущность для тестов. Проверяет прохождение теста.
 */

@Slf4j
public record TestCase(String input, String expectedOutput, String name) {
    public boolean passed(String actualOutput) {
        boolean isPassed = expectedOutput.equals(actualOutput);
        if (isPassed) {
            log.info("Тест прошёл успешно: expected=" + expectedOutput + ", actual=" + actualOutput);
        } else {
            log.warn("Тест не прошёл: expected=" + expectedOutput + ", actual=" + actualOutput);
        }
        return isPassed;
    }
}