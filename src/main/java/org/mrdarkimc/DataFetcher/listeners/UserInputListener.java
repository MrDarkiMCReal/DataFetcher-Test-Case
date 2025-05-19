package org.mrdarkimc.DataFetcher.listeners;

import java.util.Scanner;

public class UserInputListener {
    private final Scanner scanner = new Scanner(System.in);

    public String askInput() {
        System.out.println("Введите, пожалуйста, данные: ");
        return scanner.nextLine();
    }

    public String askInput(String message) {
        System.out.println(message);
        return scanner.nextLine().toLowerCase();
    }

    public Boolean askYesOrNo(String message) {
        System.out.println(message);
        System.out.println("Введите, пожалуйста, ответ: yes or no (y/n)");
        String s = scanner.nextLine().toLowerCase();
        return s.equals("yes") || s.equals("y");
    }

}
