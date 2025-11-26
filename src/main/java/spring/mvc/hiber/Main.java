package spring.mvc.hiber;

import spring.mvc.hiber.domain.TestCase;
import spring.mvc.hiber.solver.HumidifierSolver;
import java.util.Arrays;

/**
 * Главный класс: запускает тесты.
 */
public class Main {

    /**
     * Точка входа.
     */
    public static void main(String[] args) {
        // **УЛУЧШЕНО: Массив TestCase вместо hard-coded (избегает Duplicate Code, легко расширяемо)**
        TestCase[] tests = {
                new TestCase("4\n1 3\n3 1\n4 4\n7 1", "3", "Test1"),
                new TestCase("3\n1 8\n10 11\n21 5", "5", "Test2"),
                new TestCase("10\n2 1\n22 10\n26 17\n29 2\n45 20\n47 32\n72 12\n75 1\n81 31\n97 7", "57", "Test3")
        };

        HumidifierSolver solver = new HumidifierSolver();

        // **УЛУЧШЕНО: Arrays.stream() для вывода, избегает Duplicate Code print**
        Arrays.stream(tests)
                .map(test -> {
                    String result = solver.solve(test.input());
                    boolean passed = test.passed(result);
                    return String.format("%s: Result=%s, Expected=%s, Passed=%s",
                            test.name(), result, test.expectedOutput(), passed);
                })
                .forEach(System.out::println);
    }
}