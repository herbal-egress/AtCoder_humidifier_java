package spring.mvc.hiber.simulation;

import spring.mvc.hiber.domain.InputEvent;

import java.util.List;

/**
 * Класс для симуляции уровня воды.
 * SRP: только бизнес-логика протекания и доливов.
 */
public class WaterSimulator {

    /**
     * Симулирует уровень воды после всех событий.
     * @param events список событий (отсортировано по времени).
     * @return финальный уровень.
     */
    public long simulate(List<InputEvent> events) {
        if (events.isEmpty()) return 0;
        validateIncreasingTimes(events);

        // ИСПРАВЛЕНО: используем массив для мутируемой переменной в лямбде
        long[] state = {0, 0}; // state[0] = water, state[1] = prevTime

        return events.stream()
                .reduce(0L, (water, event) -> {
                    long delta = event.time() - state[1];
                    state[1] = event.time();           // обновляем время
                    return Math.max(0, water - delta) + event.volume();
                }, Long::sum);
    }
    /**
     * Симулирует один долив.
     */
    private long simulateSinglePour(long currentWater, long prevTime, InputEvent event) {
        long deltaTime = event.time() - prevTime;
        return Math.max(0, currentWater - deltaTime) + event.volume();
    }

    /**
     * Валидация возрастаемости.
     */
    private void validateIncreasingTimes(List<InputEvent> events) {
        long prev = 0;
        for (int i = 0; i < events.size(); i++) {
            InputEvent e = events.get(i);
            if (e.time() <= prev) {
                throw new IllegalArgumentException("Non-increasing time at index " + i);
            }
            prev = e.time();
        }
    }
}