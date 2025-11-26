package spring.mvc.hiber.domain;

import lombok.extern.slf4j.Slf4j;

/**
 * Неизменяемая сущность для события долива (время и объем).
 */

@Slf4j
public record InputEvent(long time, long volume) {
    public void validate() {
        if (time <= 0 || volume < 0) {
            String errorMsg = "Время должно быть >0, объём >=0";
            log.warn("Ошибка валидации исходных данных: " + errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        log.info("Валидация события прошла успешно: time=" + time + ", volume=" + volume);
    }
}