package pl.coderslab;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class TaskManager {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            showOptions();
            String choice = scan.nextLine();
            if (choice.equals("exit")) {
                System.out.println(ConsoleColors.RED + "Bye bye"
                        + ConsoleColors.RESET);
                break;
            } else if (choice.equals("list")) {
                listTasks();
            } else if (choice.equals("add")) {
                addTask();
            } else if (choice.equals("remove")) {
                removeTask();
            } else {
                System.out.println(ConsoleColors.RED + "Incorect option, choose again"
                        + ConsoleColors.RESET);
            }
        }
    }

    public static void showOptions() {
        String[] optionTab = {"add", "remove", "list", "exit"};
        System.out.println(ConsoleColors.BLUE + "Please select an option:"
                + ConsoleColors.RESET);
        for (String s : optionTab) {
            System.out.println(s);
        }
        System.out.print(ConsoleColors.GREEN + "Your option: "
                + ConsoleColors.RESET);
    }

    public static String[][] readFile() {
        Path path = Paths.get("tasks.csv");
        String[][] taskTab = new String[0][0];
        try {
            for (String line : Files.readAllLines(path)) {
                taskTab = appendOneTask(taskTab, line);
            }
        } catch (IOException ex) {
            System.out.println("Error");
        }
        return taskTab;
    }

    public static String[][] appendOneTask(String[][] taskTab, String line) {
        String[][] outTab = new String[taskTab.length + 1][3];
        for (int i = 0; i < taskTab.length; i++) {
            for (int j = 0; j < 3; j++) {
                outTab[i][j] = taskTab[i][j];
            }
        }
        String[] oneTask = line.split(", ");
        for (int i = 0; i < 3; i++) {
            outTab[outTab.length - 1][i] = oneTask[i];
        }
        return outTab;
    }

    public static void listTasks() {
        String[][] taskTab = readFile();
        for (int i = 0; i < taskTab.length; i++) {
            System.out.println((i + 1) + " : " + taskTab[i][0] + "\t" + taskTab[i][1] + "\t" + taskTab[i][2]);
        }
    }

    public static void addTask() {
        Scanner scan = new Scanner(System.in);
        String line = "";
        System.out.print("Please add task description: ");
        line += scan.nextLine() + ", ";
        System.out.print("Please add task due date: ");
        line += scan.nextLine() + ", ";
        System.out.print("Is your task important (true/false): ");
        line += scan.nextLine();
        String[][] taskTab = readFile();
        taskTab = appendOneTask(taskTab, line);
        saveFile(taskTab);
    }

    public static void saveFile(String[][] taskTab) {
        String lines = new String();
        for (int i = 0; i < taskTab.length; i++) {
            lines += taskTab[i][0] + ", " + taskTab[i][1] + ", " + taskTab[i][2];
            if (i < taskTab.length - 1) {
                lines += "\n";
            }
        }
        try (PrintWriter printw = new PrintWriter("tasks.csv")) {
            printw.println(lines);
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR in saveFile()");
        }
    }

    public static void removeTask() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Choose task to delete:");
        listTasks();
        String[][] taskTab = readFile();
        System.out.print("Type number of task to delete: ");
        String choiceStr = scan.nextLine();
        while (!isNumeric(choiceStr)) {
            System.out.print("You must type a NUMBER of task to delete:");
            choiceStr = scan.nextLine();
        }
        while (Integer.parseInt(choiceStr) < 1 || Integer.parseInt(choiceStr) > taskTab.length) {
            System.out.print("You must type a NUMBER of EXISTING task to delete:");
            choiceStr = scan.nextLine();
        }
        int choice = Integer.parseInt(choiceStr);
        String[][] outTab = new String[0][0];
        for (int i = 0; i < choice - 1; i++) {
            String line = taskTab[i][0] + ", " + taskTab[i][1] + ", " + taskTab[i][2];
            outTab = appendOneTask(outTab, line);
        }
        String nameOfDeleted = taskTab[choice - 1][0];
        for (int i = choice; i < taskTab.length; i++) {
            String line = taskTab[i][0] + ", " + taskTab[i][1] + ", " + taskTab[i][2];
            outTab = appendOneTask(outTab, line);
        }
        System.out.println("Task: \"" + nameOfDeleted + "\" succesfully deleted");
        saveFile(outTab);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int num = Integer.parseInt(strNum);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }


}