import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Pr4 {
    private static final long globalStartTime = System.currentTimeMillis();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long part1StartTime = System.currentTimeMillis();

        System.out.println("=== Частина 1: Генерація та вивід масиву ===");

        CompletableFuture<Void> part1 = CompletableFuture
                .supplyAsync(() -> {
                    int[][] matrix = new int[3][3];
                    Random rand = new Random();
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            matrix[i][j] = rand.nextInt(50);
                        }
                    }
                    printWithTime("Згенеровано масив", Arrays.deepToString(matrix));
                    return matrix;
                })
                .thenApplyAsync(matrix -> {
                    int[][] columns = new int[3][3];
                    for (int j = 0; j < 3; j++) {
                        for (int i = 0; i < 3; i++) {
                            columns[j][i] = matrix[i][j];
                        }
                    }
                    printWithTime("Трансформовано у стовпці", Arrays.deepToString(columns));
                    return columns;
                })
                .thenAcceptAsync(columns -> {
                    for (int col = 0; col < columns.length; col++) {
                        StringBuilder sb = new StringBuilder("Стовпець " + (col+1) + ": ");
                        for (int val : columns[col]) {
                            sb.append(val).append(" ");
                        }
                        printWithTime("Вивід стовпця", sb.toString().trim());
                    }
                })
                .thenRunAsync(() -> {
                    printWithTime("Завершення (Частина 1)", "Усі стовпці виведені!");
                })
                .exceptionally(ex -> {
                    System.err.println("Сталася помилка в частині 1: " + ex.getMessage());
                    return null;
                });

        part1.join();
        long part1EndTime = System.currentTimeMillis();
        long part1Duration = part1EndTime - part1StartTime;

        long part2StartTime = System.currentTimeMillis();

        System.out.println("\n=== Частина 2: Обчислення суми min непарних та max парних індексів ===");

        CompletableFuture<Void> part2 = CompletableFuture
                .supplyAsync(() -> {
                    double[] arr = new double[20];
                    Random rand = new Random();
                    for (int i = 0; i < 20; i++) {
                        arr[i] = rand.nextDouble() * 100;
                    }
                    printWithTime("Згенеровано рядок", Arrays.toString(arr));
                    return arr;
                })
                .thenApplyAsync(arr -> {
                    double minOddPositions = Double.MAX_VALUE;
                    double maxEvenPositions = Double.MIN_VALUE;

                    for (int i = 0; i < arr.length; i++) {
                        if (i % 2 == 0) {
                            // i - парний індекс, відповідає (a1,a3,a5...)
                            if (arr[i] < minOddPositions) {
                                minOddPositions = arr[i];
                            }
                        } else {
                            // i - непарний індекс, відповідає (a2,a4,a6...)
                            if (arr[i] > maxEvenPositions) {
                                maxEvenPositions = arr[i];
                            }
                        }
                    }

                    double result = minOddPositions + maxEvenPositions;
                    return new CalculationResult(arr, minOddPositions, maxEvenPositions, result);
                })
                .thenAcceptAsync(res -> {
                    printWithTime("Результат обчислення",
                            "min (a1,a3,a5,...) = " + res.minOddPositions +
                                    ", max (a2,a4,a6,...) = " + res.maxEvenPositions +
                                    "\nСума: " + res.result);
                })
                .thenRunAsync(() -> {
                    printWithTime("Завершення (Частина 2)", "Усі обчислення завершено!");
                })
                .exceptionally(ex -> {
                    System.err.println("Сталася помилка в частині 2: " + ex.getMessage());
                    return null;
                });

        part2.join();
        long part2EndTime = System.currentTimeMillis();
        long part2Duration = part2EndTime - part2StartTime;

        long totalDuration = part1Duration + part2Duration;

        System.out.println("\nЧас виконання Частини 1: " + part1Duration + " мс");
        System.out.println("Час виконання Частини 2: " + part2Duration + " мс");
        System.out.println("Сумарний час обох частин: " + totalDuration + " мс");

        long globalTotalTime = System.currentTimeMillis() - globalStartTime;
        System.out.println("Загальний час від старту програми до кінця: " + globalTotalTime + " мс");
    }

    private static void printWithTime(String messageType, String message) {
        long currentTime = System.currentTimeMillis() - globalStartTime;
        System.out.println("[" + currentTime + " мс] " + messageType + ": " + message);
    }

    private static class CalculationResult {
        double[] arr;
        double minOddPositions;
        double maxEvenPositions;
        double result;

        CalculationResult(double[] arr, double minOddPositions, double maxEvenPositions, double result) {
            this.arr = arr;
            this.minOddPositions = minOddPositions;
            this.maxEvenPositions = maxEvenPositions;
            this.result = result;
        }
    }
}