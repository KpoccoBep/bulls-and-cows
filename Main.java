package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static int length;
    static String codeString;
    static boolean rightGuess = false;
    static int turn = 1;

    private static void printGrade(int bulls, int cows) {
        System.out.print("Grade: ");
        if (cows == 0 && bulls == 0) {
            System.out.println("None");
        } else if (cows == 0) {
            System.out.printf("%d bull%s\n", bulls, bulls == 1 ? "" : "s");
        } else if (bulls == 0) {
            System.out.printf("%d cow%s\n", cows, cows == 1 ? "" : "s");
        } else {
            System.out.printf("%d bull%s and %d cow%s\n",
                    bulls, bulls == 1 ? "" : "s",
                    cows, cows == 1 ? "" : "s");
        }
    }

    private static boolean initCode() {
        System.out.println("Input the length of the secret code:");
        String input = scanner.nextLine();
        if (!input.matches("\\d+")) {
            System.out.printf("Error: \"%s\" isn't a valid number.\n", input);
            return false;
        }

        length = Integer.parseInt(input);
        if (length < 1 || length > 36) {
            System.out.println("Error: the code's length must be positive and less than or equal to 36 (0-9, a-z).");
            return false;
        }

        int totalNumber;
        System.out.println("Input the number of possible symbols in the code:");
        input = scanner.nextLine();
        if (!input.matches("\\d+")) {
            System.out.printf("Error: \"%s\" isn't a valid number.\n", input);
            return false;
        }

        totalNumber = Integer.parseInt(input);
        if (totalNumber < length) {
            System.out.printf("Error: the number of possible symbols must be greater than or equal to the code's length (%d).\n", length);
            return false;
        } else if (totalNumber > 36) {
            System.out.println("Error: the number of possible symbols must be less than or equal to 36 (0-9, a-z).");
            return false;
        }

        char[] codeArray = new char[length];
        int[] uniqueSymbols = new int[totalNumber];
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            while (true) {
                int number = random.nextInt(totalNumber);
                if (uniqueSymbols[number] == 0) {
                    if (number < 10) {
                        codeArray[i] = (char) ('0' + number);
                    } else {
                        codeArray[i] = (char) ('a' + number - 10);
                    }
                    uniqueSymbols[number] = 1;
                    break;
                }
            }
        }

        String upperLimit;
        if (totalNumber <= 10) {
            upperLimit = String.valueOf(totalNumber - 1);
        } else if (totalNumber == 11) {
            upperLimit = "9, a";
        } else {
            upperLimit = String.format("9, a-%c", (char) ('a' + totalNumber - 10 - 1));
        }
        codeString = String.valueOf(codeArray);

        System.out.printf("The secret is prepared: %s (0-%s).\n", "*".repeat(length), upperLimit);
        System.out.println("Okay, let's start a game!");
        return true;
    }

    private static boolean makeGuess() {
        System.out.printf("Turn %d:\n", turn++);
        String guessString = scanner.next();

        if (guessString.length() != length) {
            System.out.printf("Error: the input's length is incorrect; it must be equal to %d.\n", length);
            return false;
        }

        int bulls = 0;
        int cows = 0;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (codeString.charAt(i) == guessString.charAt(j)) {
                    if (i == j) {
                        bulls++;
                    } else {
                        cows++;
                    }
                }
            }
        }

        printGrade(bulls, cows);

        if (bulls == codeString.length()) {
            rightGuess = true;
        }

        return true;
    }

    private static void finishGame() {
        System.out.println("Congratulations! You guessed the secret code.");
    }

    public static void main(String[] args) {
        boolean success = initCode();
        if (!success) return;

        while (!rightGuess) {
            success = makeGuess();
            if (!success) return;
        }

        finishGame();
    }
}
