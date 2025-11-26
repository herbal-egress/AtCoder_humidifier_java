package spring.mvc.hiber.simulation;

import lombok.extern.slf4j.Slf4j;
import spring.mvc.hiber.domain.InputEvent;

import java.util.List;

/**
 * Класс для симуляции уровня воды - только бизнес-логика протекания и доливов.
 */

@Slf4j
public class WaterSimulator {
    // Симулирует уровень воды после всех событий.
    public long simulate(List<InputEvent> events) {
        log.info("Начало симуляции для " + (events != null ? events.size() : 0) + " доливов");
        try {
            if (events == null) {
                String errorMsg = "Список событий не может быть null";
                log.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            if (events.isEmpty()) {
                log.info("Пустой список событий, возвращаем 0");
                return 0;
            }
            validateIncreasingTimes(events);
            long water = 0;
            long prevTime = 0;
            for (InputEvent event : events) {
                log.info("Текущий объём воды V=" + water + ", prevTime=" + prevTime + ", event=" + event);
                long deltaTime = event.time() - prevTime;
                water = Math.max(0, water - deltaTime) + event.volume();
                prevTime = event.time();
                log.info("Объём воды V после долива=" + water);
            }
            log.info("Симуляция завершена, финальный объём воды=" + water);
            return water;
        } catch (ArithmeticException e) {
            String errorMsg = "Арифметическая ошибка в симуляции (переполнение)";
            log.error(errorMsg + ": " + e.getMessage());
            throw new IllegalArgumentException(errorMsg, e);
        }
    }

    // Валидация возрастаемости доливов.
    private void validateIncreasingTimes(List<InputEvent> events) {
        long prev = 0;
        for (int i = 0; i < events.size(); i++) {
            InputEvent e = events.get(i);
            if (e.time() <= prev) {
                String errorMsg = "Не возрастающее событие в индексе " + i;
                log.warn(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            prev = e.time();
        }
        log.info("Валидация событий прошла успешно. Приступаю к симуляции");
    }
}