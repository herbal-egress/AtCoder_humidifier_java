package spring.mvc.hiber.solver;

import spring.mvc.hiber.domain.InputEvent;
import spring.mvc.hiber.parser.InputParser;
import spring.mvc.hiber.simulation.WaterSimulator;
import lombok.extern.slf4j.Slf4j;  // добавил: импорт для @Slf4j
import java.util.List;

/**
 * Оркестратор: собирает парсер и симулятор.
 * SRP: только композиция.
 */
@Slf4j  // добавил: аннотация @Slf4j для генерации log
public class HumidifierSolver {

    private final InputParser parser;
    private final WaterSimulator simulator;

    /**
     * Конструктор с DI.
     * Подробное объяснение: Инициализирует parser и simulator; логирует создание экземпляра (INFO).
     */
    public HumidifierSolver() {
        this.parser = new InputParser();
        this.simulator = new WaterSimulator();
        // изменил: LOGGER.info -> log.info
        log.info("Создан экземпляр HumidifierSolver");
    }

    /**
     * Основной метод: парсит и симулирует.
     * @param inputStr вход.
     * @return результат как строка.
     * Подробное объяснение: Вызывает parse, затем simulate; конвертит в String; логирует шаги; try-catch для предвидимых (IllegalArgumentException от подклассов).
     */
    public String solve(String inputStr) {
        // изменил: LOGGER.info -> log.info
        log.info("Начало решения для inputStr длиной " + (inputStr != null ? inputStr.length() : 0));
        try {
            List<InputEvent> events = parser.parse(inputStr);
            long water = simulator.simulate(events);
            String result = String.valueOf(water);
            // изменил: LOGGER.info -> log.info
            log.info("Решение завершено, результат=" + result);
            return result;
        } catch (IllegalArgumentException e) {
            // изменил: LOGGER.severe -> log.error
            log.error("Ошибка в solve: " + e.getMessage());
            throw e;  // rethrow для глобального handler
        }
    }
}