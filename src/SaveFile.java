import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class SaveFile {
    private String name;
    private File file;

    SaveFile() {
        this("UnnamedCharacter");
    }

    /**
     * Creates a save file from the selected file
     * @param file
     */
    SaveFile(File file) {
        this.name = file.getName();
        this.file = file;
    }

    /**
     * Creates a file with the inputted name
     * @param name
     */
    SaveFile(String name) {
        this.name = name;
        File file = new File(name + ".txt");
        this.file = file;
        
        try {
            if (file.createNewFile()) {
                System.out.println("Created successfully");
            } else {
                System.out.println("Character with selected name already found");
            }
        } catch (Exception e) {
            System.out.println("Save file failed to be created");
        }
    }

    public String getName() {
        return name;
    }

    /**
     * Adds data to the save file
     * @param player
     * @param currentTown
     */
    public void save(Actor player, Town currentTown) {
        try {
            FileOutputStream fileStream = new FileOutputStream(file);
            PrintWriter outFS = new PrintWriter(fileStream);

            outFS.print(getData(player, currentTown));
            System.out.println("Saved successfully");

            outFS.close();
        } catch (Exception e) {
            System.out.println("Save failed");
        }
    }

    /**
     * Creates a hashMap with the data from the save
     * @return
     */
    public HashMap<String, String> load() {
        HashMap<String, String> fileInfo = new HashMap<>();

        try {
            FileInputStream fileStream = new FileInputStream(file);
            Scanner inFS = new Scanner(fileStream);

            while (inFS.hasNext()) {
                String[] splitString = inFS.nextLine().split(":");
                fileInfo.put(splitString[0], splitString[1]);
            }

            inFS.close();
        } catch (Exception e) {
            System.out.println("Load failed");
        }
        
        return fileInfo;
    }

    /**
     * Gets all the data needed to save the game
     * @param player
     * @param currentTown
     * @return
     */
    private String getData(Actor player, Town currentTown) {
        String allData = "";

        // add name
        allData = allData.concat("name:" + player.getName() + "\n");
        // add gold
        allData = allData.concat("gold:" + player.getGold() + "\n");
        // add maxDamage
        allData = allData.concat("maxDamage:" + player.getMaxDamage() + "\n");
        // add minDamage
        allData = allData.concat("minDamage:" + player.getMinDamage() + "\n");
        // add maxHealth
        allData = allData.concat("maxHealth:" + player.getMaxHealth() + "\n");
        // add Running Shoes
        if (player.hasRunningShoes()) {
            allData = allData.concat("runningShoes:true\n");
        }
        // add Library Card
        if (player.hasLibraryCard()) {
            allData = allData.concat("libraryCard:true\n");
        }
        // add townName
        allData = allData.concat("townName:" + currentTown.getName() + "\n");
        // add townId
        allData = allData.concat("townId:" + currentTown.getId() + "\n");

        return allData;
    }
}
