package spring.mvc.hiber.domain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record InputEvent(long time, long volume) {

    public void validate() {
        try {
            log.info("Валидация события: time=" + time + ", volume=" + volume);
            if (time <= 0 || volume < 0) {
                String errorMsg = "Время должно быть >0, объём >=0";
                log.warn("Ошибка валидации исходных данных: " + errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            log.info("Валидация события прошла успешно: time=" + time + ", volume=" + volume);
        } catch (Exception e) {
            String errorMsg = "Непредвиденная ошибка в валидации события: " + e.getMessage();
            log.error(errorMsg, e);
            throw new IllegalArgumentException(errorMsg, e);
        }
    }
}