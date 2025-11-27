package spring.mvc.hiber.domain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record TestCase(String input, String expectedOutput, String name) {

    public boolean passed(String actualOutput) {
        try {
            boolean isPassed = expectedOutput.equals(actualOutput);
            if (isPassed) {
                log.info("Тест прошёл успешно: expected=" + expectedOutput + ", actual=" + actualOutput);
            } else {
                log.warn("Тест не прошёл: expected=" + expectedOutput + ", actual=" + actualOutput);
            }
            return isPassed;
        } catch (NullPointerException e) {
            String errorMsg = "Null в сравнении результатов теста";
            log.error(errorMsg + ": " + e.getMessage());
            throw new IllegalArgumentException(errorMsg, e);
        } catch (Exception e) {
            String errorMsg = "Непредвиденная ошибка в проверке теста: " + e.getMessage();
            log.error(errorMsg, e);
            throw new IllegalArgumentException(errorMsg, e);
        }
    }
}