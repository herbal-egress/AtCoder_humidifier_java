package spring.mvc.hiber;

import spring.mvc.hiber.solver.HumidifierSolver;

import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;
import java.util.Scanner;

@Slf4j
public class MainForConsole {

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
            String errorMsg = "Глобальная неперехваченная ошибка: " + e.getMessage();
            log.error(errorMsg, e);
            System.err.println(errorMsg);
        });

        log.info("Запуск интерактивного режима");

        HumidifierSolver solver = new HumidifierSolver();
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("В офисе AtCoder есть один увлажнитель и он протекает!");

            int n = readPositiveInt(scanner, "Какое количество доливов воды хочешь сделать: ");

            StringBuilder input = new StringBuilder();
            input.append(n).append('\n');

            long prevTime = 0;

            for (int i = 1; i <= n; i++) {
                while (true) {
                    System.out.println("Через сколько часов от этого момента дольём (T_i) и сколько литров (V_i)?");
                    System.out.print("Укажи это двумя целыми числами T_i и V_i, разделёнными пробелом: ");
                    String line = scanner.nextLine().trim();

                    if (line.isEmpty()) {
                        System.err.println("Строка не может быть пустой. Попробуйте снова.");
                        log.warn("Пустая строка при вводе долива {}", i);
                        continue;
                    }

                    String[] parts = line.split("\\s+");
                    if (parts.length != 2) {
                        System.err.println("Нужно ввести ровно два числа (T_i V_i). Попробуйте снова.");
                        log.warn("Неверное количество чисел в строке долива {}: {}", i, line);
                        continue;
                    }

                    try {
                        long time = Long.parseLong(parts[0]);
                        long volume = Long.parseLong(parts[1]);

                        if (time <= prevTime) {
                            System.err.println("Время следующего долива T_i должно быть строго больше предыдущего (" + prevTime + "). Попробуйте снова.");
                            log.warn("Не возрастающее время: T_i={} <= prevTime={} в доливе {}", time, prevTime, i);
                            continue;
                        }
                        if (volume < 0) {
                            System.err.println("Объём V_i не может быть отрицательным. Попробуйте снова.");
                            log.warn("Отрицательный объём V_i={} в доливе {}", volume, i);
                            continue;
                        }

                        input.append(time).append(" ").append(volume).append('\n');
                        prevTime = time;
                        log.info("Успешно принят долив {}: T_i={} V_i={}", i, time, volume);
                        break;

                    } catch (NumberFormatException e) {
                        System.err.println("Оба значения должны быть целыми числами. Попробуйте снова.");
                        log.warn("Ошибка формата чисел в строке долива {}: {}", i, line);
                    }
                }
            }

            log.info("Получен полный ввод:\n{}", input);

            String result = solver.solve(input.toString());

            System.out.println("Итог: " + result);
            log.info("Решение успешно выведено: {}", result);

        } catch (NoSuchElementException e) {
            String errorMsg = "Ошибка ввода: конец потока данных";
            log.error(errorMsg + ": " + e.getMessage());
            System.err.println(errorMsg);
        } catch (Exception e) {
            String errorMsg = "Ошибка при вводе данных: " + e.getMessage();
            log.error(errorMsg, e);
            System.err.println(errorMsg);
        } finally {
            scanner.close();
            log.info("Интерактивный режим завершён");
        }
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value > 0) {
                    log.info("Принято количество доливов N={}", value);
                    return value;
                } else {
                    System.err.println("Количество доливов должно быть больше 0. Попробуйте снова.");
                    log.warn("Введено неположительное N: {}", line);
                }
            } catch (NumberFormatException e) {
                System.err.println("Введите целое положительное число. Попробуйте снова.");
                log.warn("Неверный формат N: {}", line);
            }
        }
    }
}