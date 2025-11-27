package spring.mvc.hiber.solver;

import lombok.extern.slf4j.Slf4j;
import spring.mvc.hiber.domain.InputEvent;
import spring.mvc.hiber.parser.InputParser;
import spring.mvc.hiber.simulation.WaterSimulator;

import java.util.List;

@Slf4j
public class HumidifierSolver {
    private final InputParser parser;
    private final WaterSimulator simulator;

    public HumidifierSolver() {
        this.parser = new InputParser();
        this.simulator = new WaterSimulator();
    }

    public String solve(String inputStr) {
        try {
            List<InputEvent> events = parser.parse(inputStr);
            long water = simulator.simulate(events);
            String result = String.valueOf(water);
            log.info("Решение завершено, объём воды в результате доливов=" + result);
            return result;
        } catch (IllegalArgumentException e) {
            String errorMsg = "Ошибка в обработке входных данных или симуляции: " + e.getMessage();
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg, e);
        } catch (NullPointerException e) {
            String errorMsg = "Неожиданный null в данных для решения";
            log.error(errorMsg + ": " + e.getMessage());
            throw new IllegalArgumentException(errorMsg, e);
        } catch (Exception e) {
            String errorMsg = "Непредвиденная ошибка в solve: " + e.getMessage();
            log.error(errorMsg, e);
            throw new IllegalArgumentException(errorMsg, e);
        }
    }
}