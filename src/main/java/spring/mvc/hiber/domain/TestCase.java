package spring.mvc.hiber.domain;

/**
 * Неизменяемая запись для тестового случая.
 * Позволяет легко добавлять тесты без дублирования.
 */
public record TestCase(String input, String expectedOutput, String name) {
    /**
     * Проверяет прохождение теста.
     * @param actualOutput фактический результат.
     * @return true если passed.
     */
    public boolean passed(String actualOutput) {
        return expectedOutput.equals(actualOutput);
    }
}