import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isTerminated = false;

		//фиксация идентификатора текущей сессии. Принято текущее время в милисекундах.
        System.setProperty("sessionID", String.valueOf(System.currentTimeMillis()));

        System.out.println(">>Welcome to your personal Training shedule.");

		//принудительное завершение работы по команде EXIT
        while (!isTerminated) {
            System.out.println(">>Input the command:");
            isTerminated = CommandAnalyzer.executeCommand(scanner.nextLine());
        }
    }
}