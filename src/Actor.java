import java.util.Random;

public class Actor {
    private String name;
    private int health;
    private int maxDamage;
    private int[] position = new int[2];
    private char icon;
    private int gold;
    private int maxHealth;
    private int[] buffBonus = new int[2];
    private boolean hasLibraryCard = false;
    private boolean hasRunningShoes = false;
    private int minDamage;

    public Actor() {

    }

    /**
     * Creates enemies with varying stats
     * @param type  Type of enemy
     * @param level Level of catacomb
     */
    public Actor(String type, int level) {
        switch (type) {
            case "goblin":
                health = (int) Math.pow(25, 1 + (level / 6));
                maxDamage = (int) Math.pow(5, 1 + (level / 6));
                minDamage = 1;
                break;
            case "ogre":
                health = (int) Math.pow(30, 1 + (level / 6));
                maxDamage = (int) Math.pow(10, 1 + (level / 6));
                minDamage = 5;
                break;
            case "giant":
                health = (int) Math.pow(50, 1 + (level / 6));
                maxDamage = (int) Math.pow(5, 1 + (level / 6));
                minDamage = 1;
                break;
            case "spider":
                health = (int) Math.pow(20, 1 + (level / 6));
                maxDamage = (int) Math.pow(20, 1 + (level / 6));
                minDamage = 10;
                break;
            case "dragon":
                health = (int) Math.pow(100, 1 + (level / 6));
                maxDamage = (int) Math.pow(25, 1 + (level / 6));
                minDamage = 15;
                break;
            default:
                // Final boss stats
                health = 9999999;
                maxDamage = 1000;
                minDamage = 500;
                break;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public void setPosition(int row, int column) {
        position[0] = row;
        position[1] = column;
    }

    public void setIcon(char icon) {
        this.icon = icon;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setMaxHealth(int maxHealth) {
        if (maxHealth > 0) {
            this.maxHealth = maxHealth;
        }
    }

    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
    }

    public void giveLibraryCard() {
        this.hasLibraryCard = true;
    }

    public void giveRunningShoes() {
        this.hasRunningShoes = true;
    }

    public String getName() {
        return name;
    }

    public int getGold() {
        return gold;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    /**
     * Returns the damage with the buffs added
     * @return
     */
    public int getBuffedDamage() {
        int totalDamage = maxDamage + buffBonus[1];
        if (totalDamage < 0) {
            totalDamage = Integer.MAX_VALUE;
        }
        return totalDamage;
    }

    public int[] getPosition() {
        return position;
    }

    public char getIcon() {
        return icon;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Returns the health with the buff added
     * @return
     */
    public int getBuffedHealth() {
        int totalHealth = maxHealth + buffBonus[0];
        if (totalHealth < 0) {
            totalHealth = Integer.MAX_VALUE;
        }
        return totalHealth;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public int[] getBuffs() {
        return buffBonus;
    }

    public boolean hasLibraryCard() {
        return hasLibraryCard;
    }

    public boolean hasRunningShoes() {
        return hasRunningShoes;
    }

    /**
     * Creates a random number inbetween minDamage and MaxDamage
     * @return
     */
    public int attack() {
        Random randGen = new Random();
        return randGen.nextInt(getBuffedDamage() - minDamage + 1) + minDamage;
    }

    /**
     * Adds the inputted amount of gold
     * @param gold
     */
    public void gainGold(int gold) {
        this.gold += gold;
        if (this.gold < 0) {
            this.gold = Integer.MAX_VALUE;
        }
    }

    /**
     * Returns true if the actor has the inputted amount of gold, else returns false
     * Uses the gold if the actor has enough
     * @param gold
     * @return
     */
    public boolean useGold(int gold) {
        if (gold <= this.gold) {
            this.gold -= gold;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds the buffs that the player has
     * @param type
     */
    public void addBuff(String type) {
        switch (type) {
            case "bread":
                buffBonus[0] += 50;
                buffBonus[1] += 10;
                break;
            case "cookie":
                buffBonus[0] += maxHealth + buffBonus[0];
                buffBonus[1] += maxDamage + buffBonus[1];
                break;
            case "cake":
                buffBonus[0] = (int) Math.pow(maxHealth + buffBonus[0], 1.5);
                buffBonus[1] = (int) Math.pow(maxDamage + buffBonus[1], 1.5);
                break;
            case "mysterious potion":
                Random randGen = new Random();
                buffBonus[0] = (int) Math.pow(getBuffedHealth(), randGen.nextInt(10));
                buffBonus[1] = (int) Math.pow(getBuffedDamage(), randGen.nextInt(10));
                break;
            default:
                break;
        }
        if (buffBonus[0] < 0) {
            buffBonus[0] = Integer.MAX_VALUE;
        }
        if (buffBonus[1] < 0) {
            buffBonus[1] = Integer.MAX_VALUE;
        }
    }

    /**
     * Subtracts the buff decay amount from each buff
     * @param amount
     */
    public void buffDecay(int[] amount) {
        buffBonus[0] -= amount[0];
        buffBonus[1] -= amount[1];

        if (buffBonus[0] < 0) {
            buffBonus[0] = 0;
        }
        if (buffBonus[1] < 0) {
            buffBonus[1] = 0;
        }

        if (getHealth() > getBuffedHealth()) {
            setHealth(getBuffedHealth());
        }
    }
}
