package Game;

public class Power {
    private String name;
    private String type;
    private int damage;
    private int healthRestore;
    private int energyRestore;
    private int amountOfCanBeUsed;
    private int duration; // in seconds or turns

    public Power(String name, String type, int damage, int healthRestore, int energyRestore, int amountOfCanBeUsed, int duration){
        this.name = name;
        this.type = type;
        this.damage = damage;
        this.healthRestore = healthRestore;
        this.energyRestore = energyRestore;
        this.amountOfCanBeUsed = amountOfCanBeUsed;
        this.duration = duration;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }

    public int getHealthRestore() {
        return healthRestore;
    }

    public int getEnergyRestore() {
        return energyRestore;
    }

    public int getAmountOfCanBeUsed() {
        return amountOfCanBeUsed;
    }

    public int getDuration() {
        return duration;
    }

    // Use power (reduces usage count)
    public boolean usePower() {
        if (amountOfCanBeUsed > 0) {
            amountOfCanBeUsed--;
            return true;
        }
        return false;
    }

    // Check if power can be used
    public boolean canBeUsed() {
        return amountOfCanBeUsed > 0;
    }

    @Override
    public String toString() {
        return "Power: " + name + ", Type: " + type + ", Damage: " + damage 
               + ", Health Restore: " + healthRestore + ", Energy Restore: " + energyRestore
               + ", Uses Left: " + amountOfCanBeUsed + ", Duration: " + duration;
    }
}
