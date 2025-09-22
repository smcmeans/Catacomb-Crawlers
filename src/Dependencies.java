import java.util.Scanner;

public class Dependencies {
    
    private Scanner input;

    Dependencies() {

    }

    Dependencies(Scanner input) {
        this.input = input;
    }

    /**
     * Clears the terminal
     */
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Calculates the distance between two points
     * @param pos1
     * @param pos2
     * @return      Double distance
     */
    public double distance(int[] pos1, int[] pos2) {
        double distance = Math.sqrt(Math.pow(pos1[0] - pos2[0], 2) + Math.pow(pos1[1] - pos2[1], 2));

        return distance;
    }

    /**
     * Creates a text window off a string of text using \n as break points
     * @param text
     */
    public void textWindow(String text) {

        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].trim();
        }

        int longestLine = lines[0].length();
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].length() > longestLine) {
                longestLine = lines[i].length();
            }
        }

        for (int i = 0; i < longestLine + 4; i++) {
            System.out.print("_");
        }
        System.out.printf("\n|%" + (longestLine + 2) + "s|\n", "");
        for (int i = 0; i < lines.length; i++) {
            int numSpaces = (longestLine - lines[i].length()) / 2 + 1;
            int additionalSpace = (longestLine - lines[i].length()) % 2;

            System.out.printf("|%" + numSpaces + "s%s%" + (numSpaces + additionalSpace) + "s|\n", "", lines[i], "");
        }
        System.out.print("|");
        for (int i = 0; i < longestLine + 2; i++) {
            System.out.print("_");
        }
        System.out.println("|\n");
    }

    /**
     * Gets an integer input
     * @return  The int inputted
     */
    public int getIntInput() {
        while (true) {
            String userInput = input.nextLine();
            try {
                return Integer.parseInt(userInput);
            } catch (Exception e) {
                continue;
            }
        }
    }

    /**
     * Uses getIntInput and adds bounds that the input must be between (inclusive)
     * 
     * @param lowerBound
     * @param upperBound
     * @return
     */
    public int getMenuInput(int lowerBound, int upperBound) {
        int userInput;
        do {
            userInput = getIntInput();
        } while (userInput < lowerBound || userInput > upperBound);
        return userInput;
    }

    /**
     * Unused code that gets the first character of an input
     * Was going to be used to allow the player to choose the enemy icons
     * @return  the first character
     */
    public char chooseIcon() {
        while (true) {
            try {
                return input.nextLine().charAt(0);
            } catch (Exception e) {
                continue;
            }
        }
    }

    /**
     * Simple UI element that pauses the game to allow the user to read the text
     */
    public void waitForInput() {
        textWindow("Hit enter to continue");
        input.nextLine();
    }

    /**
     * Gets the name of the player
     * @return  the name of the player
     */
    public String getPlayerName() {
        clearScreen();
        String playerName = "";
        while (playerName.equals("")) {
            textWindow("What is your name, heroic adventurer?");
            playerName = input.nextLine();
        }

        return playerName;
    }

    

}
