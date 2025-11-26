package spring.mvc.hiber.domain;

import lombok.extern.slf4j.Slf4j;  // добавил: импорт для @Slf4j

/**
 * Неизменяемая запись для события долива: время и объем.
 * Использует record для краткости и иммутабельности.
 */
@Slf4j  // добавил: аннотация @Slf4j для генерации log (SLF4J Logger)
public record InputEvent(long time, long volume) {

    /**
     * Валидация события: time > 0, volume >= 0.
     * @throws IllegalArgumentException если некорректно (сообщение на русском для консоли/логов).
     * Подробное объяснение: Метод проверяет положительность времени и неотрицательность объема; если нет - бросает исключение с русским текстом; логирует попытку валидации.
     */
    public void validate() {
        // изменил: LOGGER.info -> log.info (адаптация под SLF4J)
        log.info("Валидация события: time=" + time + ", volume=" + volume);
        if (time <= 0 || volume < 0) {
            String errorMsg = "Время должно быть >0, объём >=0";
            // изменил: LOGGER.warning -> log.warn
            log.warn("Ошибка валидации: " + errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        // изменил: LOGGER.info -> log.info
        log.info("Валидация прошла успешно");
    }
}