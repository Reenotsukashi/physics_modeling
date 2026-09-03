import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        while (true) {
            System.out.println("Выберите номер");
            System.out.println("0. выйти");
            System.out.println("1. Монохроматический свет (N щелей)");
            System.out.println("2. Квазимонохроматический свет (N щелей)");
            System.out.println("3. Вывести формулы и пояснения");

            String enterString = scanner.nextLine();

            int enter;
            try {
                enter = Integer.parseInt(enterString.trim());
            } catch (NumberFormatException e) {
                System.out.println("Вы должны ввести число, попробуйте заново");
                continue;
            }

            switch (enter) {
                case 0:
                    return;
                case 1:
                    mono();
                    break;
                case 2:
                    quasiMono();
                    break;
                case 3:
                    showHelp();
                    break;
                default:
                    System.out.println("Вы должны ввести число из приведенных выше, попробуйте заново");
                    continue;
            }
        }
    }

    private static void mono() {
        System.out.println("Введите N (1 - 10): ");
        int N = readInt(scanner, 1, 10);
        
        System.out.println("Введите ширину щели a (в мкм): ");
        double a = readDouble(scanner);
        
        System.out.println("Введите период d (в мкм): ");
        double d = readDouble(scanner);
        
        System.out.println("Введите длину волны λ (в нм): ");
        double lambda = readDouble(scanner);
        
        System.out.println("Введите расстояние до экрана L (в м): ");
        double L = readDouble(scanner);
        
        int numPoints = 1000;

        double lambdaMeters = lambda * 1e-9;
        double dMeters = d * 1e-6;
        
        double deltaX = (lambdaMeters * L / dMeters) * 1000;
        
        double screenWidth = 0.05 * deltaX;
        screenWidth = Math.max(screenWidth, 0.5);
        screenWidth = Math.min(screenWidth, 200.0);
        
        double[] intensity = InterferenceModel.calculateIntensity(N, a, d, lambda, L, numPoints, screenWidth);
        
        Visualization.showIntensityGraph(intensity, screenWidth, "Интерференция от " + N + " щелей");
        
        System.out.println("Параметры приняты: N=" + N + ", a=" + a + ", d=" + d + ", λ=" + lambda + ", L=" + L);
        System.out.println("График построен! Закройте окно графика, чтобы продолжить.\n");
    }

    private static void quasiMono() {
        System.out.println("Введите N (1 - 10): ");
        int N = readInt(scanner, 1, 10);
        
        System.out.println("Введите ширину щели a (в мкм): ");
        double a = readDouble(scanner);
        
        System.out.println("Введите период d (в мкм): ");
        double d = readDouble(scanner);
        
        System.out.println("Введите центральную длину волны λ₀ (в нм): ");
        double lambda0 = readDouble(scanner);
        
        System.out.println("Введите ширину спектра Δλ (в нм): ");
        double deltaLambda = readDouble(scanner);
        
        System.out.println("Введите расстояние до экрана L (в м): ");
        double L = readDouble(scanner);
        
        int numPoints = 1000;
        
        double lambda0Meters = lambda0 * 1e-9;
        double dMeters = d * 1e-6;
        double deltaX = (lambda0Meters * L / dMeters) * 1000;
        double screenWidth = 0.05 * deltaX;
        screenWidth = Math.max(screenWidth, 0.5);
        screenWidth = Math.min(screenWidth, 200.0);
        
        double[] intensity = InterferenceModel.calculateIntensityQuasi(N, a, d, lambda0, deltaLambda, L, numPoints, screenWidth);
        
        Visualization.showIntensityGraph(intensity, screenWidth, "Квазимонохроматический свет, N=" + N);
        Visualization.showColorMap(intensity, screenWidth, L, "Цветная карта");
        
        System.out.println("Параметры приняты: N=" + N + ", a=" + a + ", d=" + d + ", λ₀=" + lambda0 + ", Δλ=" + deltaLambda + ", L=" + L);
        System.out.println("График построен! Закройте окно графика, чтобы продолжить.\n");
    }

    private static void showHelp() {
        System.out.println("\n=== Формулы для расчета ===");
        System.out.println("Интенсивность: I(θ) = I₀ * (sin(Nδ/2) / sin(δ/2))² * (sin(β/2) / (β/2))²");
        System.out.println("δ = 2π * d * sin(θ) / λ  (разность фаз между щелями)");
        System.out.println("β = 2π * a * sin(θ) / λ  (разность фаз внутри щели)");
        System.out.println("θ = arctan(x / L) — угол дифракции");
        System.out.println("x — координата на экране, L — расстояние до экрана\n");
    }

    private static int readInt(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Число должно быть от " + min + " до " + max + ". Попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число. Попробуйте снова.");
            }
        }
    }

    private static double readDouble(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введите число. Попробуйте снова.");
            }
        }
    }
}