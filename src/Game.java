import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Game {

    static ArrayList<File> saveFiles = new ArrayList<>();
    static HashMap<String, Integer> settings = new HashMap<String, Integer>();
    static Scanner input = new Scanner(System.in);
    static Dependencies dependencies = new Dependencies(input);
    static Town currentTown;
    static SaveFile currentSave;

    /**
     * Searches a directory for all files of a chosen file type
     * 
     * @param directory The directory being searched
     * @param fileType  The desired file type
     */
    private static void openFileDiretory(File[] directory, String fileType) {
        for (File file : directory) {
            if (file.isDirectory()) {
                openFileDiretory(file.listFiles(), fileType);
            } else {
                String fileName = file.getName();
                if (fileName.substring(fileName.lastIndexOf('.') + 1).equals(fileType)) {
                    saveFiles.add(file);
                }
            }
        }
    }

    /**
     * Displays the title screen along with menu options
     */
    private static void titleScreen() {
        boolean inTitleScreen = true;

        while (inTitleScreen) {
            dependencies.clearScreen();
            // Title screen
            dependencies.textWindow("Catacomb Crawlers\n\n\n\n1. New Game\n2. Open save file\n3. Settings");

            // Get user input
            int userInput = dependencies.getMenuInput(1, 3);
            switch (userInput) {
                case 1:
                    inTitleScreen = false;
                    createNewGame();
                    break;
                case 2:
                    inTitleScreen = false;
                    openSaveFiles();
                    break;
                case 3:
                    settings();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Creates a new character and starting town, assigning default values and
     * creating a save file if the setting is enabled
     */
    private static void createNewGame() {
        Actor player = new Actor();

        player.setName(dependencies.getPlayerName());
        player.setMaxDamage(10);
        player.setMinDamage(1);
        player.setGold(0);
        player.setMaxHealth(100);

        Town startingTown = new Town();
        startingTown.setName("Vertigo");

        if (settings.get("saveFiles") == 1) {
            currentSave = new SaveFile(player.getName());
            currentSave.save(player, startingTown);
        }

        townHandler(startingTown, player);
    }

    /**
     * Searches for save files and displays them to the user
     */
    private static void openSaveFiles() {
        dependencies.clearScreen();
        // Check to see if save files are enabled
        if (settings.get("saveFiles") != 1) {
            dependencies.textWindow(
                    "Save files must be enabled in settings for this feature to work\n\nThis will scan the current directory\nfor text files to use\n\nEnable?");
            String userInput = input.nextLine();
            dependencies.clearScreen();

            try {
                if (Character.toUpperCase(userInput.charAt(0)) == 'Y') {
                    settings.put("saveFiles", 1);
                }
            } catch (Exception e) {
            }
        }

        // Find save files
        if (settings.get("saveFiles") == 1) {
            // Gets all files of type txt in this directory
            File[] fileDirectory = new File(System.getProperty("user.dir")).listFiles();
            openFileDiretory(fileDirectory, "txt");

            // Create an array list of all save files
            ArrayList<SaveFile> saveData = new ArrayList<>();
            for (File file : saveFiles) {
                SaveFile data = new SaveFile(file);
                saveData.add(data);
            }

            // Display options to the user to choose a save file
            String displaySaves = "Choose a save file\n\n";
            for (int i = 1; i <= saveData.size(); i++) {
                displaySaves = displaySaves.concat(i + ". " + saveData.get(i - 1).getName() + "\n");
            }
            displaySaves = displaySaves.concat((saveData.size() + 1) + ". Leave");

            dependencies.textWindow(displaySaves);
            int userInput = dependencies.getMenuInput(1, saveData.size() + 1);

            try {
                Actor player = loadSave(saveData.get(userInput - 1));
                townHandler(currentTown, player);
            } catch (Exception e) {
                if (userInput != saveData.size() + 1) {
                    dependencies.clearScreen();
                    dependencies.textWindow("This save appears to be corrupted");
                    dependencies.waitForInput();
                }
                titleScreen();
            }

        } else {
            titleScreen();
        }
    }

    /**
     * Loads a game based off the save file
     * 
     * @param saveFile Save file it is loading from
     * @return Player object
     */
    private static Actor loadSave(SaveFile saveFile) {
        HashMap<String, String> saveInfo = saveFile.load();
        currentSave = saveFile;

        Actor player = new Actor();
        player.setName(saveInfo.get("name"));
        player.setMaxDamage(Integer.parseInt(saveInfo.get("maxDamage")));
        player.setMinDamage(Integer.parseInt(saveInfo.get("minDamage")));
        player.setGold(Integer.parseInt(saveInfo.get("gold")));
        player.setMaxHealth(Integer.parseInt(saveInfo.get("maxHealth")));
        currentTown = new Town(saveInfo.get("townId"));
        currentTown.setName(saveInfo.get("townName"));
        try {
            String hasRunningShoes = saveInfo.get("runningShoes");
            if (hasRunningShoes.equals("true")) {
                player.giveRunningShoes();
            }
        } catch (Exception e) {
        }
        try {
            String hasLibraryCard = saveInfo.get("libraryCard");
            if (hasLibraryCard.equals("true")) {
                player.giveLibraryCard();
            }
        } catch (Exception e) {
        }

        return player;
    }

    /**
     * Displays all settings and allows the user to change them
     */
    private static void settings() {
        boolean inSettings = true;

        while (inSettings) {
            boolean settingsState;

            if (settings.get("saveFiles") == 1) {
                settingsState = true;
            } else {
                settingsState = false;
            }

            dependencies.clearScreen();
            dependencies.textWindow("Settings\n\n1. Use save files: " + settingsState + "\n2. Leave\n");
            int userInput = dependencies.getMenuInput(1, 2);

            switch (userInput) {
                case 1:
                    settings.put("saveFiles", 1);
                    break;
                case 2:
                    inSettings = false;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Creates the settings hashMap and assigns default values
     */
    private static void setDefaultSettings() {
        settings.put("saveFiles", 0);
        settings.put("animations", 0);
    }

    /**
     * Gets the size of the catacomb from user input
     * 
     * @return The size of the catacomb
     */
    private static int getCatacombSize() {
        dependencies.clearScreen();
        int size;

        while (true) {
            dependencies.textWindow("How wide of a catacomb do you want to face (5-10)?");
            size = dependencies.getIntInput();
            if (size > 10 || size < 5) {
                dependencies.textWindow("That is not a valid catacomb size!");
            } else {
                break;
            }
        }

        return size;
    }

    /**
     * Handles the gameplay loop while in the catacombs
     * 
     * @param player Actor object that is the player
     * @param level  The ecurrent catacomb level (starts at 1)
     */
    private static void enterCatacombs(Actor player, int level) {
        int startingHealth = player.getBuffedHealth();
        player.setHealth(startingHealth);
        player.setIcon('P');

        // Calculate buff decay
        int[] buffs = player.getBuffs();
        int[] buffDecay = { buffs[0] / 10, buffs[1] / 10 };

        boolean inCatacombs = true;

        while (inCatacombs) {

            GameBoard gameBoard = new GameBoard(getCatacombSize());
            int[] exit = gameBoard.getExitPos();

            // Player stating stats
            int[] startingPosition = { 0, 0 };
            int numOfKills = 0;
            int sniffingPower = 1;
            player.setPosition(startingPosition);

            // Get the enemy for each level
            String type;
            switch (level % 5) {
                case 1:
                    type = "goblin";
                    break;
                case 2:
                    type = "ogre";
                    break;
                case 3:
                    type = "giant";
                    break;
                case 4:
                    type = "spider";
                    break;
                case 0:
                    type = "dragon";
                    break;
                default:
                    type = "goblin";
                    break;
            }

            // Final level enemy
            if (level == 10) {
                type = player.getName();
            }
            // Create a list of all enemies
            Actor[] enemies = createEnemies(player, gameBoard, type, level);

            // Places the enemies on the gameBoard
            for (Actor enemy : enemies) {
                enemy.setIcon(Character.toUpperCase(type.charAt(0)));
                gameBoard.setPosition(enemy.getPosition(), enemy.getIcon());
            }

            // Gameplay loop
            boolean alive = true;
            int enemiesNear;
            while (alive) {
                dependencies.clearScreen();
                gameBoard.setPosition(player.getPosition(), player.getIcon());
                System.out.println("Level " + level);
                System.out.println("Health: " + player.getHealth());

                // Display gameBoard
                if (level != 10) {
                    buffs = player.getBuffs();
                    if (buffs[0] > 0 || buffs[1] > 0) {
                        sniffingPower = 2;
                    } else {
                        sniffingPower = 1;
                    }
                    gameBoard.display(player.getPosition(), sniffingPower, dependencies);
                } else {
                    gameBoard.display();
                }

                // Display nearby enemies
                enemiesNear = gameBoard.nearbyMonsterCheck(player.getPosition(), dependencies);
                if (enemiesNear == 1) {
                    System.out.println(enemiesNear + " enemy nearby");
                } else {
                    System.out.println(enemiesNear + " enemies nearby");
                }

                // Display directions
                dependencies.textWindow("Which direction would you like to go? (north, east, south, west)");

                // Get the position of the player before moving for the run mechanic
                int[] previousPosition = player.getPosition().clone();

                // Move the player
                int[] currentPosition = move(player.getPosition(), gameBoard);
                player.setPosition(currentPosition);

                // Move check
                if (!player.hasRunningShoes()) {
                    if (currentPosition[0] != previousPosition[0] || currentPosition[1] != previousPosition[1]) {
                        player.setHealth(player.getHealth() - 2);
                        if (player.getHealth() <= 0) {
                            alive = false;
                            inCatacombs = false;
                            death(player);
                        }
                    }
                }

                // Get the player position to see if it collides with a enemy postion
                int[] playerPosition = player.getPosition();

                for (Actor enemy : enemies) {
                    // Check if the player position == enemy position and the enemy is not dead
                    int[] enemyPosition = enemy.getPosition();
                    if (playerPosition[0] == enemyPosition[0] && playerPosition[1] == enemyPosition[1]
                            && enemy.getHealth() > 0) {
                        // start a battle between the player and enemy
                        alive = battle(player, enemy);
                        // Decay buffs
                        player.buffDecay(buffDecay);
                        // Run check
                        if (enemy.getHealth() > 0) {
                            gameBoard.setPosition(playerPosition, enemy.getIcon());
                            player.setPosition(previousPosition);
                        } else {
                            // If the player did not run, the enemy must have been killed
                            numOfKills++;
                        }
                        // Check if the player died in the fight
                        if (!alive) {
                            inCatacombs = false;
                            death(player);
                        }
                    }
                }
                // Check if the player is on the exit
                if (playerPosition[0] == exit[0] && playerPosition[1] == exit[1] && alive) {
                    inCatacombs = exit(numOfKills, player, level);
                    level++;
                    alive = false;
                }
            }
        }
    }

    /**
     * Handles the death of the player in the catacombs
     * 
     * @param player
     */
    private static void death(Actor player) {
        Random randGen = new Random();

        // Generate a random amount of lost gold
        int lostGold;
        try {
            lostGold = randGen.nextInt(player.getGold()) + 1;
            player.useGold(lostGold);
        } catch (Exception e) {
            lostGold = 0;
        }

        // Display the death
        dependencies.clearScreen();
        dependencies.textWindow(
                "You were defeated in the catacombs\n\nWith some effort you manage\nto scurry to the town,\nlosing "
                        + lostGold + " gold");
        dependencies.waitForInput();
    }

    /**
     * Handles leaving the dungeon
     * 
     * @param numOfKills Number of enemies defeated
     * @param player     Player actor
     * @param level      The level that was completed
     * @return If the user exited the catacombs
     */
    private static boolean exit(int numOfKills, Actor player, int level) {
        Random randGen = new Random();
        int goldMultiplier = (int) Math.pow(level, 2);
        int totalGold = 0;

        // Calculate the gold from the level
        for (int i = 0; i < numOfKills; i++) {
            totalGold += randGen.nextInt(10 * goldMultiplier) + goldMultiplier;
        }
        // Bonus for beating the final level
        if (level == 10) {
            totalGold = (int) Math.pow(totalGold, totalGold);
        }

        player.gainGold(totalGold);

        // Display the victory
        dependencies.clearScreen();
        dependencies.textWindow(
                "You successfully escaped the catacombs!\nYou defeated " + numOfKills + " monsters\nYou gained "
                        + totalGold + " gold");
        dependencies.waitForInput();

        dependencies.clearScreen();
        if (level != 10) {
            // See if the player wants to go further into the catacombs
            dependencies.textWindow("What would you like to do?\n\n1. Go to town\n2. Go deeper into the catacombs");
            int userInput = dependencies.getMenuInput(1, 2);

            switch (userInput) {
                case 1:
                    return false;
                case 2:
                    return true;
                default:
                    return false;
            }
        } else {
            dependencies.textWindow("You made it to the end of the dungeon\nYou must return to town");
            dependencies.waitForInput();
            return false;
        }
    }

    /**
     * Spawns enemies in a random position a certain distance away from the player
     * 
     * @param player    Player actor
     * @param gameBoard The gameBoard object
     * @param type      The type of enemy
     * @param level     The current catacomb level
     * @return
     */
    private static Actor[] createEnemies(Actor player, GameBoard gameBoard, String type, int level) {

        // Spawn enemies normally
        if (level != 10) {
            // Calculate the number of enemies, increasing with level
            int numOfEmenies = (int) (Math.pow(gameBoard.getSize(), 2)) / 6 + (level - 1);
            Actor[] enemies = new Actor[numOfEmenies];
            // Create an arrayList of postions that the enemies can spawn at
            ArrayList<int[]> validPos = new ArrayList<>();
            Random randGen = new Random();

            int[] exit = gameBoard.getExitPos();
            // Go through each square in gameBoard and ensure that it is far enough away
            // from the player and not the exit
            for (int i = 0; i < gameBoard.getSize(); i++) {
                for (int j = 0; j < gameBoard.getSize(); j++) {
                    int[] position = { i, j };
                    // Check to see if the tile is the exit or far enough away
                    if (dependencies.distance(position, player.getPosition()) >= 2
                            && !(position[0] == exit[0] && position[1] == exit[1])) {
                        validPos.add(position);
                    }
                }
            }

            // Create the monster
            String monsterName = String.valueOf(Character.toUpperCase(type.charAt(0))) + type.substring(1) + " ";
            for (int i = 0; i < numOfEmenies; i++) {
                enemies[i] = new Actor(type, level);
                enemies[i].setName(monsterName + (i + 1));
                // get a random index of the validPos arrayList
                int randomNum = randGen.nextInt(validPos.size());
                enemies[i].setPosition(validPos.get(randomNum));
                // remove that index
                validPos.remove(randomNum);
            }

            return enemies;
        } else {
            // Spawn the final boss
            Actor[] enemies = new Actor[1];
            enemies[0] = new Actor("mimic", 10);
            enemies[0].setName(player.getName());
            enemies[0].setPosition(gameBoard.getExitPos());

            return enemies;
        }
    }

    /**
     * Handles the movement of the player while in the catacombs
     * 
     * @param playerPosition Current position of the player
     * @param gameBoard      Current gameBoard
     * @return Updated position of the player
     */
    private static int[] move(int[] playerPosition, GameBoard gameBoard) {
        String userInput = input.nextLine().toLowerCase();
        int gameBoardSize = gameBoard.getSize();

        // Allow wasd as input
        switch (userInput) {
            case "w":
                userInput = "north";
                break;
            case "s":
                userInput = "south";
                break;
            case "a":
                userInput = "west";
                break;
            case "d":
                userInput = "east";
                break;
            default:
                break;
        }

        // Handles directions
        switch (userInput) {
            case "north":
                if (playerPosition[0] - 1 >= 0) {
                    // Clears the previous postion
                    gameBoard.setPosition(playerPosition, ' ');
                    // Updates the new position
                    playerPosition[0] -= 1;
                }
                break;
            case "south":
                if (playerPosition[0] + 1 < gameBoardSize) {
                    gameBoard.setPosition(playerPosition, ' ');
                    playerPosition[0] += 1;
                }
                break;
            case "east":
                if (playerPosition[1] + 1 < gameBoardSize) {
                    gameBoard.setPosition(playerPosition, ' ');
                    playerPosition[1] += 1;
                }
                break;
            case "west":
                if (playerPosition[1] - 1 >= 0) {
                    gameBoard.setPosition(playerPosition, ' ');
                    playerPosition[1] -= 1;
                }
                break;
            default:
                break;
        }

        return playerPosition;
    }

    /**
     * Handles the battle between the player and enemy
     * 
     * @param player
     * @param enemy
     * @return
     */
    private static boolean battle(Actor player, Actor enemy) {
        boolean inBattle = true;
        while (inBattle) {
            dependencies.clearScreen();
            System.out.printf("""
                    ___________________________________
                    |                                 |
                    |             %10s | %3d HP |
                    |                        O        |
                    |                       -|-       |
                    |                       / \\       |
                    |                                 |
                    |%6s | %3d HP                  |
                    |       O                         |
                    |      -|-                        |
                    |      / \\                        |
                    |_________________________________|

                                    """, enemy.getName(), enemy.getHealth(), player.getName(), player.getHealth());

            dependencies.textWindow("What would you like to do?\n1. Fight\n2. Auto battle\n3. Run");

            int userInput = dependencies.getMenuInput(1, 3);

            switch (userInput) {
                case 1:
                    dependencies.clearScreen();
                    fight(player, enemy);
                    fight(enemy, player);
                    inBattle = checkHealth(player, enemy);
                    dependencies.waitForInput();
                    break;
                case 2:
                    dependencies.clearScreen();
                    autoFight(player, enemy);
                    inBattle = checkHealth(player, enemy);
                    dependencies.waitForInput();
                    break;
                case 3:
                    inBattle = !run(player, enemy);
                    if (inBattle) {
                        fight(enemy, player);
                        inBattle = checkHealth(player, enemy);
                    }
                    dependencies.waitForInput();
                    break;
                default:
                    break;
            }
        }
        // Check if the player died
        if (player.getHealth() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Random number generator to see if the player can run from the enemy
     * 
     * @param player Player actor
     * @param enemy  Enemy actor
     * @return
     */
    private static boolean run(Actor player, Actor enemy) {
        dependencies.clearScreen();
        // the random number has to be above the runChance to run
        int runChance = enemy.getHealth() - player.getHealth();

        if (runChance < 0) {
            dependencies.textWindow("You run away successfully");
            return true;
        } else {
            Random randGen = new Random();
            if (randGen.nextInt(100) >= runChance) {
                dependencies.textWindow("You run away successfully");
                return true;
            } else {
                dependencies.textWindow("You can't get away!");
                return false;
            }
        }
    }

    /**
     * Checks if the player or enemy died
     * 
     * @param player Player actor
     * @param enemy  Enemy actor
     * @return True if player is alive, false if not
     */
    private static boolean checkHealth(Actor player, Actor enemy) {
        if (enemy.getHealth() == 0) {
            dependencies.textWindow("You defeated " + enemy.getName() + "!");
            return false;
        } else if (player.getHealth() == 0) {
            dependencies.textWindow("You were defeated by " + enemy.getName());
            return false;
        } else {
            return true;
        }
    }

    /**
     * Handles the damage calculation for fighting
     * 
     * @param actor1 The actor that is doing damage
     * @param actor2 The actor that is taking damage
     */
    private static void fight(Actor actor1, Actor actor2) {
        int damage = actor1.attack();
        if (damage > actor2.getHealth()) {
            damage = actor2.getHealth();
        }
        if (actor1.getHealth() > 0) {
            actor2.setHealth(actor2.getHealth() - damage);

            dependencies.textWindow(actor1.getName() + " deals " + damage + " damage\nto " + actor2.getName() + "\n"
                    + actor2.getHealth() + " HP left");
        }
    }

    /**
     * Condences a series of fight actions into 1
     * 
     * @param player The player actor
     * @param enemy  The enemy actor
     */
    private static void autoFight(Actor player, Actor enemy) {

        // Variables to hold the total damage that each actor has taken
        int enemyDamage = 0;
        int playerDamage = 0;
        int roundCounter = 0;

        // Check that nobody died
        while (player.getHealth() > playerDamage && enemy.getHealth() > enemyDamage) {
            // Calculate attacks
            enemyDamage += player.attack();
            if (enemy.getHealth() > enemyDamage) {
                playerDamage += enemy.attack();
                roundCounter++;
            }
        }
        // Calculate if the damage went over the total hp of each actor
        if (enemy.getHealth() <= enemyDamage) {
            enemyDamage = enemy.getHealth();
        } else if (player.getHealth() <= playerDamage) {
            playerDamage = player.getHealth();
        }

        // Add damage
        player.setHealth(player.getHealth() - playerDamage);
        enemy.setHealth(enemy.getHealth() - enemyDamage);

        // Display damage
        dependencies.textWindow(player.getName() + " deals " + enemyDamage + " damage\nto " + enemy.getName());
        dependencies.textWindow(enemy.getName() + " deals " + playerDamage + " damage\nto " + player.getName());
        dependencies.textWindow("Completed in " + roundCounter + " rounds");
    }

    /**
     * Handles all of the town actions
     * 
     * @param town
     * @param player
     */
    private static void townHandler(Town town, Actor player) {
        boolean inTown = true;

        // You can't escape the town
        while (inTown) {
            String activity = town.getActivity(player, dependencies, settings);

            switch (activity) {
                case "Catacombs":
                    enterCatacombs(player, 1);
                    break;
                case "Save":
                    currentSave.save(player, town);
                    dependencies.clearScreen();
                    dependencies.textWindow("Saved successfully");
                    dependencies.waitForInput();
                    break;
                default:
                    town.goTo(activity, player, dependencies);
                    break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        setDefaultSettings();
        titleScreen();

        input.close();
    }
}
