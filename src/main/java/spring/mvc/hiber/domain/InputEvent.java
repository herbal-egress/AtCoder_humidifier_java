package spring.mvc.hiber.domain;

/**
 * Неизменяемая запись для события долива: время и объем.
 * Использует record для краткости и иммутабельности.
 */
public record InputEvent(long time, long volume) {
    /**
     * Валидация события: time > 0, volume >= 0.
     * @throws IllegalArgumentException если некорректно.
     */
    public void validate() {
        if (time <= 0 || volume < 0) {
            throw new IllegalArgumentException("Time must be >0, volume >=0");
        }
    }
}