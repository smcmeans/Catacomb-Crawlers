public class GameBoard {
    private char[][] gameBoard;
    private int size;
    private int[] exitPos = new int[2];

    public GameBoard() {
        this(5);
    }

    /**
     * Creates a gameBoard
     * 
     * @param size The size of the walls
     */
    public GameBoard(int size) {
        gameBoard = new char[size][size];
        this.size = size;

        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                gameBoard[i][j] = ' ';
            }
        }
        this.exitPos[0] = size - 1;
        this.exitPos[1] = size - 1;
        gameBoard[exitPos[0]][exitPos[1]] = 'X';
    }

    /**
     * Displays the gameBoard
     */
    public void display() {
        for (int i = 0; i < gameBoard.length * 2 + 1; i++) {
            System.out.print("_");
        }
        System.out.println();
        for (int i = 0; i < gameBoard.length; i++) {
            System.out.print("|");
            // This actually displays the contents, the rest is just to make it look good
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (i == getSize() - 1) {
                    System.out.print((char)27 + "[4m" + gameBoard[i][j]);
                    if (j == getSize() - 1) {
                        System.out.print((char)27 + "[24m");
                    }
                } else {
                    System.out.print(gameBoard[i][j]);
                }
                if (j != getSize() - 1) {
                    if (i == getSize() - 1) {
                        System.out.print('_');
                    } else {
                        System.out.print(' ');
                    }
                }
            }
            System.out.println("|");
        }
    }

    /**
     * Finds nearby monsters
     * 
     * @param playerPosition
     * @param dependencies
     * @return The number of monsters
     */
    public int nearbyMonsterCheck(int[] playerPosition, Dependencies dependencies) {
        int counter = 0;

        for (int i = playerPosition[0] - 1; i <= playerPosition[0] + 1; i++) {
            for (int j = playerPosition[1] - 1; j <= playerPosition[1] + 1; j++) {
                int[] pos2 = { i, j };
                try {
                    if (dependencies.distance(playerPosition, pos2) <= 1
                            && (gameBoard[i][j] != ' ' && gameBoard[i][j] != 'X' && gameBoard[i][j] != 'P')) {
                        counter++;
                    }
                } catch (Exception e) {
                }
            }
        }

        return counter;
    }

    /**
     * Displays only the squares vision distance away from the player
     * Used this website to learn how to underline text in java
     * https://stackoverflow.com/questions/5062458/font-settings-for-strings-in-java
     * @param playerPosition
     * @param vision         How far the hero can see
     */
    public void display(int[] playerPosition, int vision, Dependencies dependencies) {
        for (int i = 0; i < gameBoard.length * 2 + 1; i++) {
            System.out.print("_");
        }
        System.out.println();
        for (int i = 0; i < gameBoard.length; i++) {
            System.out.print("|");
            for (int j = 0; j < gameBoard[i].length; j++) {
                int[] pos1 = { i, j };
                // Check to see if the position is close enough to be visible or the exit
                if (dependencies.distance(pos1, playerPosition) <= vision
                        || (pos1[0] == exitPos[0] && pos1[1] == exitPos[1])) {
                    if (i == getSize() - 1) {
                        System.out.print((char)27 + "[4m" + gameBoard[i][j]);
                        if (j == getSize() - 1) {
                            System.out.print((char)27 + "[24m");
                        }
                    } else {
                        System.out.print(gameBoard[i][j]);
                    }

                } else if (i == getSize() - 1) {
                    System.out.print('_');
                } else {
                    System.out.print(' ');
                }
                if (j != getSize() - 1) {
                    if (i == getSize() - 1) {
                        System.out.print('_');
                    } else {
                        System.out.print(' ');
                    }

                }
            }
            System.out.println("|");
        }
    }

    /**
     * Places the icon in the inputted position of the gameBoard
     * 
     * @param position
     * @param icon
     */
    public void setPosition(int[] position, char icon) {
        try {
            gameBoard[position[0]][position[1]] = icon;
        } catch (Exception e) {
            System.out.println("Invalid position");
        }
    }

    /**
     * Gets the size of the gameBoard
     * 
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the exitPosition
     * 
     * @return
     */
    public int[] getExitPos() {
        return exitPos;
    }
}
