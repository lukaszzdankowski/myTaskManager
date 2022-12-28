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
                System.out.println(ConsoleColors.RED + "Incorect option, try again"
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

    public static String[][] appendOneTask(String[][] taksTab, String line) {
        String[][] outTab = new String[taksTab.length + 1][3];
        for (int i = 0; i < taksTab.length; i++) {
            for (int j = 0; j < 3; j++) {
                outTab[i][j] = taksTab[i][j];
            }
        }
        String[] oneTask = line.split(", ");
        for (int i = 0; i < 3; i++) {
            outTab[outTab.length - 1][i] = oneTask[i];
        }
        return outTab;
    }

    public static void listTasks() {
        String[][] taksTab = readFile();
        for (int i = 0; i < taksTab.length; i++) {
            System.out.println((i + 1) + " : " + taksTab[i][0] + "\t" + taksTab[i][1] + "\t" + taksTab[i][2]);
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
        System.out.print("Type number of task to delete: ");
        int choice = scan.nextInt();
        String[][] taskTab = readFile();
        String[][] outTab = new String[0][0];
        for (int i = 0; i < choice - 1; i++) {
            String line = taskTab[i][0] + ", " + taskTab[i][1] + ", " + taskTab[i][2];
            outTab = appendOneTask(outTab, line);
        }
        for (int i = choice; i < taskTab.length; i++) {
            String line = taskTab[i][0] + ", " + taskTab[i][1] + ", " + taskTab[i][2];
            outTab = appendOneTask(outTab, line);
        }
        saveFile(outTab);
    }


}