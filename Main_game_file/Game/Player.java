package Game;
public class Player {
    private String name;
    private String playerID;
    private int playerLevel;
    private int currentFloorLevel;
    private int health;
    private int maxHealth;
    private int energy;
    private int maxEnergy;
    private int experience;
    private int experienceToNextLevel;
    private OnHandItem itemOnHands;
    private Inventory inventoryItems;
    private Power currentPower;

    public Player(String name, String playerID, int playerLevel, int currentFloorLevel){
        this.name = name;
        this.playerID = playerID;
        this.playerLevel = playerLevel;
        this.currentFloorLevel = currentFloorLevel;
        this.maxHealth = 100 + (playerLevel * 20); // Health increases with level
        this.health = maxHealth;
        this.maxEnergy = 50 + (playerLevel * 10); // Energy increases with level
        this.energy = maxEnergy;
        this.experience = 0;
        this.experienceToNextLevel = playerLevel * 100;
        this.itemOnHands = new OnHandItem();
        this.inventoryItems = new Inventory(20 + (playerLevel * 5)); // Inventory size increases with level
        this.currentPower = null;
    }


    // Getters
    public String getName() {
        return name;
    }

    public String getPlayerID() {
        return playerID;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public int getCurrentFloorLevel() {
        return currentFloorLevel;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getExperience() {
        return experience;
    }

    public int getExperienceToNextLevel() {
        return experienceToNextLevel;
    }

    public OnHandItem getItemOnHands() {
        return itemOnHands;
    }

    public Inventory getInventoryItems() {
        return inventoryItems;
    }

    public Power getCurrentPower() {
        return currentPower;
    }

    // Setters
    public void setCurrentFloorLevel(int currentFloorLevel) {
        this.currentFloorLevel = currentFloorLevel;
    }

    public void setCurrentPower(Power power) {
        this.currentPower = power;
    }

    public void takeDamage(int damage) { 
        this.health -= damage;
        if (this.health < 0) { 
            this.health = 0;
        }
    }

    public void heal(int healAmount){
        this.health+=healAmount;
        if (health>maxHealth) {
            this.health=maxHealth;
        }
    }

    public boolean isAlive(){
        return health>0; 
    }

    public void useEnergy(int energyAmount){
        this.energy-=energyAmount;
        if (this.energy<0) {
            this.energy=0;
        }
    }

    public void restoreEnergy(int energyAmount) {
        this.energy+=energyAmount;
        if (energy>maxEnergy) {
            this.energy=maxEnergy;
        }
    }

    public boolean hasEnergy(int requiredEnergy){
        return energy>=requiredEnergy;
    }

    public void gainExperience(int exp) {
        this.experience += exp;
        checkLevelUp();
    }

    private void checkLevelUp() {
        if (experience >= experienceToNextLevel) {
            levelUp();
        }
    }

    private void levelUp() {
        playerLevel++;
        experience -= experienceToNextLevel;
        experienceToNextLevel = playerLevel * 100;
        
        // Increase stats
        int healthIncrease = 20;
        int energyIncrease = 10;
        maxHealth += healthIncrease;
        maxEnergy += energyIncrease;
        health = maxHealth; // Full heal on level up
        energy = maxEnergy; // Full energy restore on level up
        
        System.out.println("Level up! Now level " + playerLevel);
        System.out.println("Health increased by " + healthIncrease);
        System.out.println("Energy increased by " + energyIncrease);
    }

    // Item management
     public boolean equipItemToLeftHand(Items item) {
        if (inventoryItems.hasItem(item)) {
            if (itemOnHands.putItemOnLeft(item)) {
                inventoryItems.removeItem(item);
                return true;
            }
        }
        return false;
    }

    public boolean equipItemToRightHand(Items item) {
        if (inventoryItems.hasItem(item)) {
            if (itemOnHands.putItemOnRight(item)) {
                inventoryItems.removeItem(item);
                return true;
            }
        }
        return false;
    }

    public void unequipLeftHand(){
        Items item=itemOnHands.removeItemFromLeft();
        if (item!=null) {
            inventoryItems.addItem(item);
        }
    }

    public void unequipRightHand(){
        Items item=itemOnHands.removeItemFromRight();
        if (item!=null) {
            inventoryItems.addItem(item);
        }
    }

    //use item
    public void useItem(Items item) {
        if (item instanceof Food) {
            Food food = (Food) item;
            heal(food.getHealthRestore());
            restoreEnergy(food.getEnergyRestore());
            item.reduceQuantity();

            if (item.getQuantity() <=0 ) {
                inventoryItems.removeItem(item);
            }
        } else if (item instanceof Weapon) {
            System.out.println("Weapon equipped and ready to use!");
        }
    }

    // use power
    public boolean usePower(){
        if (currentPower!=null && currentPower.canBeUsed()) {
            if (currentPower.usePower()) {
                heal(currentPower.getHealthRestore());
                restoreEnergy(currentPower.getEnergyRestore());
                return true;
            }
        }
        return false;
    }

    // attack with wepon on hand
    public int attack(){
        int totalDamage=0;

        Items leftItem=itemOnHands.seeItemOnLeft();
        Items rightItem=itemOnHands.seeItemOnRight();

        if (leftItem instanceof Weapon) {
            totalDamage+= ((Weapon) leftItem).getDamage();
        }

        if (rightItem instanceof Weapon) {
            totalDamage+=((Weapon)rightItem).getDamage();
        }

        if (currentPower!=null && currentPower.canBeUsed()) {
            totalDamage+=currentPower.getDamage();
        }

        return totalDamage;
    }

    @Override
    public String toString() {
        return "Player: " + name + " (ID: " + playerID + ")" +
               "\nLevel: " + playerLevel + " | Floor: " + currentFloorLevel +
               "\nHealth: " + health + "/" + maxHealth +
               "\nEnergy: " + energy + "/" + maxEnergy +
               "\nExperience: " + experience + "/" + experienceToNextLevel +
               "\nInventory: " + inventoryItems.getCurrentItemsCount() + "/" + inventoryItems.getInventorySize() + " items";
    }
}
