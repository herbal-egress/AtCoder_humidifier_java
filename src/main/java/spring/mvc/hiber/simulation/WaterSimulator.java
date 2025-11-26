package spring.mvc.hiber.simulation;

import lombok.extern.slf4j.Slf4j;
import spring.mvc.hiber.domain.InputEvent;

import java.util.List;


/**
 * Класс для симуляции уровня воды.
 * SRP: только бизнес-логика протекания и доливов.
 */
@Slf4j  // добавил: аннотация @Slf4j для генерации log
public class WaterSimulator {

    /**
     * Симулирует уровень воды после всех событий.
     * @param events список событий (отсортировано по времени).
     * @return финальный уровень.
     * Подробное объяснение: Проверяет empty; вызывает validate; инициализирует water и prevTime; в цикле вычисляет delta, уменьшает water (не ниже 0), добавляет volume, обновляет prevTime; логирует каждый шаг; try-catch для предвидимых ошибок (e.g., ArithmeticException если overflow, но маловероятно для long).
     */
    public long simulate(List<InputEvent> events) {
        // изменил: LOGGER.info -> log.info
        log.info("Начало симуляции, событий: " + (events != null ? events.size() : 0));
        try {
            if (events == null) {
                String errorMsg = "Список событий не может быть null";
                // изменил: LOGGER.severe -> log.error
                log.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            if (events.isEmpty()) {
                // изменил: LOGGER.info -> log.info
                log.info("Пустой список событий, возвращаем 0");
                return 0;
            }

            validateIncreasingTimes(events);

            long water = 0;
            long prevTime = 0;

            for (InputEvent event : events) {
                // изменил: LOGGER.info -> log.info
                log.info("Текущий water=" + water + ", prevTime=" + prevTime + ", event=" + event);
                long deltaTime = event.time() - prevTime;
                water = Math.max(0, water - deltaTime) + event.volume();
                prevTime = event.time();
                // изменил: LOGGER.info -> log.info
                log.info("После долива: water=" + water);
            }

            // изменил: LOGGER.info -> log.info
            log.info("Симуляция завершена, финальный water=" + water);
            return water;
        } catch (ArithmeticException e) {
            String errorMsg = "Арифметическая ошибка в симуляции (переполнение)";
            // изменил: LOGGER.severe -> log.error
            log.error(errorMsg + ": " + e.getMessage());
            throw new IllegalArgumentException(errorMsg, e);
        }
    }

    /**
     * Валидация возрастаемости.
     * Подробное объяснение: Итерация по events; проверка time > prev; обновление prev; бросает исключение если нет; логирует проверку (INFO/WARNING).
     */
    private void validateIncreasingTimes(List<InputEvent> events) {
        // изменил: LOGGER.info -> log.info
        log.info("Валидация возрастаемости времени");
        long prev = 0;
        for (int i = 0; i < events.size(); i++) {
            InputEvent e = events.get(i);
            if (e.time() <= prev) {
                String errorMsg = "Не возрастающее время в индексе " + i;
                // изменил: LOGGER.warning -> log.warn
                log.warn(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            prev = e.time();
        }
        // изменил: LOGGER.info -> log.info
        log.info("Валидация времени прошла успешно");
    }
}