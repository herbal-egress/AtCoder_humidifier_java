package spring.mvc.hiber;

import lombok.extern.slf4j.Slf4j;
import spring.mvc.hiber.domain.TestCase;
import spring.mvc.hiber.solver.HumidifierSolver;

import java.util.Arrays;

/**
 * Главный класс для запуска тестов.
 */

@Slf4j
public class Main {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
            String errorMsg = "Глобальная неперехваченная ошибка: " + e.getMessage();
            log.error(errorMsg);
            System.err.println(errorMsg);
        });
        log.info("Запуск Main");
        TestCase[] tests = {
                new TestCase("4\n1 3\n3 1\n4 4\n7 1", "3", "Тест1"),
                new TestCase("3\n1 8\n10 11\n21 5", "5", "Тест2"),
                new TestCase("10\n2 1\n22 10\n26 17\n29 2\n45 20\n47 32\n72 12\n75 1\n81 31\n97 7", "57", "Тест3")
        };
        HumidifierSolver solver = new HumidifierSolver();
        Arrays.stream(tests)
                .map(test -> {
                    String result = solver.solve(test.input());
                    boolean passed = test.passed(result);
                    log.info(test.name() + " завершён, passed=" + passed);
                    return String.format("%s: Результат=%s, Ожидаемый=%s, Прошёл=%s",
                            test.name(), result, test.expectedOutput(), passed);
                })
                .forEach(System.out::println);
        log.info("Main завершён");
    }
}