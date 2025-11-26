package spring.mvc.hiber.solver;

import spring.mvc.hiber.domain.InputEvent;
import spring.mvc.hiber.parser.InputParser;
import spring.mvc.hiber.simulation.WaterSimulator;

import java.util.List;

/**
 * Оркестратор: собирает парсер и симулятор.
 * SRP: только композиция.
 */
public class HumidifierSolver {

    private final InputParser parser;
    private final WaterSimulator simulator;

    // **УЛУЧШЕНО: Dependency Injection via constructor (избегает Hard-coded создания, testable)**
    public HumidifierSolver() {
        this.parser = new InputParser();
        this.simulator = new WaterSimulator();
    }

    /**
     * Основной метод: парсит и симулирует.
     * @param inputStr вход.
     * @return результат как строка.
     */
    public String solve(String inputStr) {
        // **УЛУЧШЕНО: Делегирование, метод теперь <10 строк (избегает Long Method)**
        List<InputEvent> events = parser.parse(inputStr);
        long water = simulator.simulate(events);
        return String.valueOf(water);
    }
}