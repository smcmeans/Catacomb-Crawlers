import java.util.ArrayList;
import java.util.HashMap;

public class Town {
    private String townName;
    private String townId;
    private String[] avaliableBuildings = { "Catacombs", "Builder's House", "Blacksmith", "Hospital", "Bakery",
            "Library" };
    private ArrayList<String> buildings = new ArrayList<>();

    /**
     * Creates a town with the default id
     */
    Town() {
        this("110000");
    }

    /**
     * Creates a town with the inputted id
     * 
     * @param townId
     */
    Town(String townId) {
        this.townId = townId;
        setBuildings();
    }

    /**
     * Creates a town with the inputted id and name
     * 
     * @param townId
     * @param townName
     */
    Town(String townId, String townName) {
        this.townId = townId;
        this.townName = townName;
        setBuildings();
    }

    /**
     * Adds buildings to the buildings arrayList
     */
    private void setBuildings() {
        buildings.clear();
        for (int i = 0; i < avaliableBuildings.length; i++) {
            if (townId.charAt(i) == '1') {
                buildings.add(avaliableBuildings[i]);
            }
        }
    }

    public ArrayList<String> getBuildings() {
        return buildings;
    }

    public void setName(String name) {
        townName = name;
    }

    public String getName() {
        return townName;
    }

    public String getId() {
        return townId;
    }

    public void setId(String townId) {
        this.townId = townId;
        setBuildings();
    }

    /**
     * Gets the next unbuilt building
     * 
     * @return
     */
    public String getNextBuilding() {
        int nextBuilding = -1;
        for (int i = 0; i < avaliableBuildings.length; i++) {
            if (townId.charAt(i) == '0') {
                nextBuilding = i;
                break;
            }
        }
        if (nextBuilding >= 0) {
            return avaliableBuildings[nextBuilding];
        } else {
            return "All buildings built\nGood job!";
        }
    }

    /**
     * Calls the function for each place
     * 
     * @param place
     * @param player
     * @param dependencies
     */
    public void goTo(String place, Actor player, Dependencies dependencies) {
        switch (place) {
            case "Builder's House":
                buildersHouse(player, dependencies);
                break;
            case "Blacksmith":
                blacksmith(player, dependencies);
                break;
            case "Hospital":
                hospital(player, dependencies);
                break;
            case "Bakery":
                bakery(player, dependencies);
                break;
            case "Library":
                library(player, dependencies);
                break;
            default:
                dependencies.textWindow(place + " is not avaliable currently");
                break;
        }
    }

    /**
     * Gets the activity from user input that will be used in the goTo function
     * 
     * @param player
     * @param dependencies
     * @param settings
     * @return
     */
    public String getActivity(Actor player, Dependencies dependencies, HashMap<String, Integer> settings) {
        dependencies.clearScreen();
        String activities = "You are at " + getName() + "\nWhat would you like to do?\n\n";
        for (int i = 0; i < getBuildings().size(); i++) {
            activities = activities.concat((i + 1) + ". Go to the " + getBuildings().get(i) + "\n");
        }

        if (settings.get("saveFiles") == 1) {
            activities = activities.concat((getBuildings().size() + 1) + ". Save game");
        }

        boolean choosingDestination = true;
        String activity = null;
        while (choosingDestination) {
            dependencies.textWindow(activities);
            int userInput = dependencies.getMenuInput(1, getBuildings().size() + 1);

            try {
                activity = getBuildings().get(userInput - 1);
                choosingDestination = false;
            } catch (Exception e) {
                if (settings.get("saveFiles") == 1 && userInput == getBuildings().size() + 1) {
                    activity = "Save";
                    choosingDestination = false;
                }
                continue;
            }
        }

        return activity;
    }

    /**
     * Handles all buildersHouse text and logic
     * 
     * @param player
     * @param dependencies
     */
    private void buildersHouse(Actor player, Dependencies dependencies) {
        dependencies.clearScreen();
        dependencies.textWindow("You enter Gerry the Builder's house\n\n1. Purchase a building\n2. Leave");
        int userInput = dependencies.getIntInput();

        switch (userInput) {
            case 1:
                String building = getNextBuilding();
                int cost = 0;

                switch (building) {
                    case "Catacombs":
                        cost = 0;
                        break;
                    case "Builder's House":
                        cost = 0;
                        break;
                    case "Blacksmith":
                        cost = 100;
                        break;
                    case "Hospital":
                        cost = 500;
                        break;
                    case "Bakery":
                        cost = 1000;
                        break;
                    case "Library":
                        cost = 5000;
                        break;
                    default:
                        break;
                }

                boolean purchasing = true;
                while (purchasing) {
                    dependencies.clearScreen();
                    dependencies
                            .textWindow("I've been looking into building a\n" + building + ", but it'll cost\n" + cost
                                    + " gold\n\n1. Buy\n2. Leave");
                    dependencies.textWindow("You have " + player.getGold() + " gold");
                    int userChoice = dependencies.getIntInput();
                    switch (userChoice) {
                        case 1:
                            if (player.useGold(cost)) {
                                purchasing = false;
                                String townId = getId();
                                int nextBuilding = -1;
                                for (int i = 0; i < townId.length(); i++) {
                                    if (townId.charAt(i) == '0') {
                                        nextBuilding = i;
                                        break;
                                    }
                                }
                                try {
                                    townId = townId.substring(0, nextBuilding) + "1"
                                            + townId.substring(nextBuilding + 1);
                                } catch (Exception e) {
                                }
                                setId(townId);

                                dependencies.clearScreen();
                                dependencies.textWindow("Purchase successful");
                                dependencies.waitForInput();
                            } else {
                                dependencies.clearScreen();
                                dependencies.textWindow("You do not have enough gold");
                                dependencies.waitForInput();
                            }
                            break;
                        case 2:
                            purchasing = false;
                            break;
                        default:
                            purchasing = false;
                            break;
                    }
                }
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    /**
     * Handles all the blacksmith text and logic
     * 
     * @param player
     * @param dependencies
     */
    private void blacksmith(Actor player, Dependencies dependencies) {
        dependencies.clearScreen();
        dependencies.textWindow(
                "Billy the Blacksmith greets you\nWhat would you like to do?\n\n1. Upgrade weapon\n2. Leave");
        int userInput = dependencies.getMenuInput(1, 2);

        boolean inBlacksmith = true;
        while (inBlacksmith)
            switch (userInput) {
                case 1:
                    int cost = (player.getMinDamage() / 5 + 1) * 100;
                    dependencies.clearScreen();
                    dependencies.textWindow(
                            "That'll cost you " + cost + " gold\nfor a good sharpening\n\n1. Buy\n2. Leave");
                    dependencies.textWindow(
                            "Your current max damage is " + player.getMaxDamage() + "\nYour current minimum damage is "
                                    + player.getMinDamage() + "\nYou have " + player.getGold()
                                    + " gold");
                    int userChoice = dependencies.getMenuInput(1, 2);
                    switch (userChoice) {
                        case 1:
                            if (player.useGold(cost)) {
                                player.setMaxDamage(player.getMaxDamage() + 10);
                                player.setMinDamage(player.getMinDamage() + 5);
                                dependencies.clearScreen();
                                dependencies.textWindow("Purchase successful");
                                dependencies.waitForInput();
                            } else {
                                dependencies.clearScreen();
                                dependencies.textWindow("Not enough gold");
                                dependencies.waitForInput();
                            }
                            break;
                        case 2:
                            inBlacksmith = false;
                        default:
                            break;
                    }
                    break;
                case 2:
                    inBlacksmith = false;
                default:
                    break;
            }
    }

    /**
     * Handles all the hospital text and logic
     * 
     * @param player
     * @param dependencies
     */
    private void hospital(Actor player, Dependencies dependencies) {
        if (!player.hasRunningShoes()) {
            dependencies.clearScreen();
            dependencies.textWindow(
                    "Heather the Nurse greets you\nThank you for funding this building,\nas a gift, I want to give you\nthese");
            dependencies.textWindow("You recieved Runnning Shoes");
            dependencies.waitForInput();
            player.giveRunningShoes();
        }
        dependencies.clearScreen();
        dependencies.textWindow(
                "Heather the Nurse greets you\nWould you like to get stronger?\n\n1. Increase Health\n2. Leave");
        int userInput = dependencies.getMenuInput(1, 2);

        switch (userInput) {
            case 1:
                boolean inHospital = true;
                while (inHospital) {
                    int cost = (player.getMaxHealth() / 10) * 50;
                    dependencies.clearScreen();
                    dependencies.textWindow(
                            "It will cost you " + cost
                                    + " gold to increase your health capacity\n\n1. Buy\n2. Leave");
                    dependencies.textWindow(
                            "Your current max health is " + player.getMaxHealth() + "\nYou have " + player.getGold()
                                    + " gold");
                    int userChoice = dependencies.getMenuInput(1, 2);

                    switch (userChoice) {
                        case 1:
                            dependencies.clearScreen();
                            if (player.useGold(cost)) {
                                player.setMaxHealth(player.getMaxHealth() + 50);
                                dependencies.textWindow("Purchase successful");
                                dependencies.waitForInput();
                            } else {
                                dependencies.textWindow("Not enough gold");
                                dependencies.waitForInput();
                            }
                            break;
                        case 2:
                            inHospital = false;
                            break;
                        default:
                            break;
                    }
                }
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    /**
     * Handles the introduction to the bakery
     * 
     * @param player
     * @param dependencies
     */
    private void bakery(Actor player, Dependencies dependencies) {
        dependencies.clearScreen();
        dependencies.textWindow("Harry the Baker greets you\nWhat brings you in here?\n\n1. Buy\n2. Leave");
        int userInput = dependencies.getMenuInput(1, 2);
        dependencies.clearScreen();

        switch (userInput) {
            case 1:
                boolean purchasing = true;
                while (purchasing) {
                    dependencies.clearScreen();
                    dependencies.textWindow(
                            "We have all sorts of treats today\n\n1. Bread | 100 gold\n2. Cookie | 500 gold\n3. Cake | 1500 gold\n4. Mysterious Potion | 5000 gold\n5. Leave");
                    dependencies.textWindow("You have " + player.getGold() + " gold");
                    int userChoice = dependencies.getMenuInput(1, 5);
                    dependencies.clearScreen();
                    switch (userChoice) {
                        case 1:
                            bakeryTextHandler("bread", 100, player, dependencies);
                            break;
                        case 2:
                            bakeryTextHandler("cookie", 500, player, dependencies);
                            break;
                        case 3:
                            bakeryTextHandler("cake", 1500, player, dependencies);
                            break;
                        case 4:
                            bakeryTextHandler("mysterious potion", 5000, player, dependencies);
                            break;
                        case 5:
                            purchasing = false;
                            break;
                        default:
                            break;
                    }
                }
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    /**
     * Handles the purchases in the bakery
     * 
     * @param type
     * @param cost
     * @param player
     * @param dependencies
     */
    private void bakeryTextHandler(String type, int cost, Actor player, Dependencies dependencies) {
        String text = "";

        switch (type) {
            case "bread":
                text = "Bread is said to increase your health and\ndamage by a fixed amount\nfor a short period of time";
                break;
            case "cookie":
                text = "Cookies are said to increase your health and\ndamage by a multiplicative amount\nfor a short period of time";
                break;
            case "cake":
                text = "Cake is said to increase your health and\ndamage by a powerful amount\nfor a short period of time";
                break;
            case "mysterious potion":
                text = "We're not sure what this will do";
                break;
            default:
                break;
        }
        text = text.concat("\n\n1. Buy for " + cost + " gold\n2. Leave");

        boolean purchasing = true;
        while (purchasing) {
            dependencies.clearScreen();
            dependencies.textWindow(
                    text);
            dependencies.textWindow(
                    "You have " + player.getBuffedHealth() + " health\nYou have " + player.getBuffedDamage()
                            + " max damage\nYou have " + player.getGold() + " gold");
            int userInput = dependencies.getMenuInput(1, 2);
            switch (userInput) {
                case 1:
                    if (player.useGold(cost)) {
                        player.addBuff(type);
                        dependencies.clearScreen();
                        dependencies.textWindow("Purchase successful");
                        dependencies.waitForInput();
                    } else {
                        dependencies.clearScreen();
                        dependencies.textWindow("Not enough gold");
                        dependencies.waitForInput();
                    }
                    break;
                case 2:
                    purchasing = false;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Handles the library text and logic
     * 
     * @param player
     * @param dependencies
     */
    private void library(Actor player, Dependencies dependencies) {
        boolean inLibrary = true;
        while (inLibrary) {

            dependencies.clearScreen();
            String introduction = "Barry the Librarian greets you\n";
            if (player.hasLibraryCard()) {
                introduction = introduction.concat("\n1. Checkout a book");
            } else {
                introduction = introduction.concat("Do you have a library card?\n\n1. Purchase");
            }
            introduction = introduction.concat("\n2. Leave");

            dependencies.textWindow(introduction);
            int userInput = dependencies.getMenuInput(1, 2);

            switch (userInput) {
                case 1:
                    if (player.hasLibraryCard()) {
                        libraryGetBook(player, dependencies);
                    } else {
                        buyLibraryCard(player, dependencies);
                    }
                    break;
                case 2:
                    inLibrary = false;
                    break;
            }
        }
    }

    /**
     * Allows the player to buy the library card
     * 
     * @param player
     * @param dependencies
     */
    private void buyLibraryCard(Actor player, Dependencies dependencies) {
        long cost = player.getGold();
        cost++;

        dependencies.clearScreen();
        dependencies.textWindow(
                "Getting a library card is the perfect\nnext step for any young adventurer\nWe don't get too many purchases\nso the price is pretty steep\n\n1. Buy for "
                        + cost + " gold\n2. Leave");
        dependencies.textWindow("You have " + player.getGold() + " gold");
        int userChoice = dependencies.getMenuInput(1, 2);

        switch (userChoice) {
            case 1:
                if (player.useGold((int) cost) && cost <= Integer.MAX_VALUE) {
                    player.giveLibraryCard();

                    dependencies.clearScreen();
                    dependencies.textWindow("Thank you for your purchase");
                    dependencies.waitForInput();
                } else {
                    dependencies.clearScreen();
                    dependencies.textWindow("Not enough gold");
                    dependencies.waitForInput();
                }
        }
    }

    /**
     * Unused function
     * 
     * @param player
     */
    private void libraryGetBook(Actor player, Dependencies dependencies) {
        String[] avaliableBooks = { "Art of War", "Charlie Brown", "Prisoner of Azkaban" };

        String allText = "We have a large selection of books,\ndepending on your definition of large\n\n";
        for (int i = 0; i < avaliableBooks.length; i++) {
            allText = allText.concat((i + 1) + ". " + avaliableBooks[i] + "\n");
        }

        boolean choosingBook = true;
        while (choosingBook) {
            dependencies.clearScreen();
            dependencies.textWindow(allText);

            int userInput = dependencies.getMenuInput(1, avaliableBooks.length);
        }
    }

}
