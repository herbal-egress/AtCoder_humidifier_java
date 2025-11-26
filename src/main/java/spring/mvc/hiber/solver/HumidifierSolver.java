package spring.mvc.hiber.solver;

import lombok.extern.slf4j.Slf4j;
import spring.mvc.hiber.domain.InputEvent;
import spring.mvc.hiber.parser.InputParser;
import spring.mvc.hiber.simulation.WaterSimulator;

import java.util.List;

/**
 * Оркестратор: собирает парсер и симулятор.
 */

@Slf4j
public class HumidifierSolver {
    private final InputParser parser;
    private final WaterSimulator simulator;

    public HumidifierSolver() {
        this.parser = new InputParser();
        this.simulator = new WaterSimulator();
    }

    // метод парсит исходные данные и симулирует события на их основе.
    public String solve(String inputStr) {
        try {
            List<InputEvent> events = parser.parse(inputStr);
            long water = simulator.simulate(events);
            String result = String.valueOf(water);
            log.info("Решение завершено, объём воды в результате доливов=" + result);
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Ошибка в solve: " + e.getMessage());
            throw e;
        }
    }
}