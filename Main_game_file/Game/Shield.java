package Game;

public class Shield extends Items {
    private int defense;
    private int durability;
    private int maxDurability;

    public Shield(String type, String shieldID, int quantity, int defense, int durability) {
        super(type, quantity, shieldID);
        this.defense = defense;
        this.durability = durability;
        this.maxDurability = durability;
    }

    public int getDefense() { return defense; }
    public int getDurability() { return durability; }
    public int getMaxDurability() { return maxDurability; }

    public void takeDamage(int damage) {
        durability -= damage;
        if (durability < 0) {
            durability = 0;
        }
    }

    public boolean isBroken() {
        return durability <= 0;
    }

    public void repair(int amount) {
        durability += amount;
        if (durability > maxDurability) {
            durability = maxDurability;
        }
    }

    @Override
    public String toString() {
        return "Shield - type: " + getType() + ", defense: " + defense + 
               ", durability: " + durability + "/" + maxDurability + 
               ", quantity: " + getQuantity();
    }
}

// javac Main_game_file/*.java  
// java Main_game_file.DungeonWindow