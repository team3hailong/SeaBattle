package view;

import controller.gamemanager.SoundController;

import controller.Main;

public class EventInterface {
    public static final String reset = "\u001B[0m";
    public static final String red = "\u001B[31m";
    public static final String yellow = "\u001B[33m";
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String purple = "\u001B[35m";
    public static final String gray = "\u001B[90m";
    public static SoundController soundController = SoundController.getInstance();

    private static class Holder {
        private static final EventInterface INSTANCE = new EventInterface();
    }

    public static EventInterface getInstance() {
        return Holder.INSTANCE;
    }
    public static void alert(String message) {
        System.out.println(red + message + reset);
    }

    public static int getValidOptionWithPrompt(String prompt, String notification) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = Main.scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                alert(notification);
            }
        }
    }
    public void start(){
        soundController.playSoundWithDurationAsync("/resources/sound/start.wav", false,4000, 1f);
        String[] lines = {
                " ____ _____  _    ____ _____ _ ",
                "/ ___|_   _|/ \\  |  _ \\_   _| |",
                "\\___ \\ | | / _ \\ | |_) || | | |",
                " ___) || |/ ___ \\|  _ < | | |_|",
                "|____/ |_/_/   \\_\\_| \\_\\|_| (_)"
        };

        for (String line : lines) {
            System.out.println(yellow + line + reset);
        }
        System.out.println();
    }
    public void end(){
        soundController.playSoundWithDurationAsync("/resources/sound/end.wav", false, 3000, 1f);
        String[] lines = {
                " _____ _   _ ____  _ ",
                "| ____| \\ | |  _ \\| |",
                "|  _| |  \\| | | | | |",
                "| |___| |\\  | |_| |_|",
                "|_____|_| \\_|____/(_)"
        };

        for (String line : lines) {
            System.out.println(purple + line + reset);
        }
    }

    public void hit(){
        soundController.playSoundWithDurationAsync("/resources/sound/hit.wav", false,3000, 1f);
        String[] lines = {
                " __    __   __  .___________. __ ",
                "|  |  |  | |  | |           ||  |",
                "|  |__|  | |  | `---|  |----`|  |",
                "|   __   | |  |     |  |     |  |",
                "|  |  |  | |  |     |  |     |__|",
                "|__|  |__| |__|     |__|     (__)"
        };

        for (String line : lines) {
            System.out.println(red + line + reset);
        }
    }
    public void miss(){
        soundController.playSoundWithDurationAsync("/resources/sound/miss.wav", false,3000, 1f);
        String[] lines = {
                ".___  ___.  __       _______.     _______.",
                "|   \\/   | |  |     /       |    /       |",
                "|  \\  /  | |  |    |   (----`   |   (----`",
                "|  |\\/|  | |  |     \\   \\        \\   \\    ",
                "|  |  |  | |  | .----)   |   .----)   |   ",
                "|__|  |__| |__| |_______/    |_______/    "
        };
        for (String line : lines) {
            System.out.println(gray + line + reset);
        }
    }
    public void sunk(){
        soundController.playSoundWithDurationAsync("/resources/sound/sink.wav", false,3000, 1f);
        String[] lines = {
                "     _______. __    __  .__   __.  __  ___  __   __   __ ",
                "    /       ||  |  |  | |  \\ |  | |  |/  / |  | |  | |  |",
                "   |   (----`|  |  |  | |   \\|  | |  '  /  |  | |  | |  |",
                "    \\   \\    |  |  |  | |  . `  | |    <   |  | |  | |  |",
                ".----)   |   |  `--'  | |  |\\   | |  .  \\  |__| |__| |__|",
                "|_______/     \\______/  |__| \\__| |__|\\__\\ (__) (__) (__)"
        };

        for (String line : lines) {
            System.out.println(yellow + line + reset);
        }
    }

    public void explosion(){
        soundController.playSoundWithDurationAsync("/resources/sound/sink.wav", false,3000, 1f);
        String[] art = {
                "          _ ._  _ , _ ._",
                "        (_ ' ( `  )_  .__)",
                "      ( (  (    )   `)  ) _)",
                "     (__ (_   (_ . _) _) ,__)",
                "         `~~`\\ ' . /`~~`",
                "              ;   ;",
                "              /   \\",
                "_____________/_ __ \\_____________"
        };

        for (String line : art) {
            System.out.println(red + line + reset);
        }
    }
}
