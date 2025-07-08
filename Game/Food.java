package Game;

public class Food extends Items {
    private int healthRestore;
    private int energyRestore;

    public Food(String type, String foodID, int quantity, int healthRestore, int energyRestore) {
        super(type, quantity, foodID);
        this.healthRestore = healthRestore;
        this.energyRestore = energyRestore;
    }

    public int getHealthRestore() { return healthRestore; } 
    public int getEnergyRestore() { return energyRestore; }

    @Override
    public String toString() {
        return "type: " + getType() + ", quantity: " + getQuantity() 
               + ", health restore: " + healthRestore + ", energy restore: " + energyRestore;
    }
}