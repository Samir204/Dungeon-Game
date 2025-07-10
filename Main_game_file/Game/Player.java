package Game;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private String playerID;
    public static List<Player> allPlayers= new ArrayList<>(); 
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
        this.maxHealth = 100 + (playerLevel * 20); 
        this.health = maxHealth;
        this.maxEnergy = 50 + (playerLevel * 10); 
        this.energy = maxEnergy;
        this.experience = 0;
        this.experienceToNextLevel = playerLevel * 100;
        this.itemOnHands = new OnHandItem();
        this.inventoryItems = new Inventory(20 + (playerLevel * 5)); // Inventory size increases with level
        this.currentPower = null;

        registerPlayer(this);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// registering functions

    public static boolean registerPlayer(Player player){
        if (player!=null && !ifPlayerExist(player.getPlayerID())) {
            allPlayers.add(player);
            System.out.println("Player registered "+ player.getName() + " (ID: "+ player.getPlayerID() + " )");
            return true;
        }
        return false;
    }

    public static boolean ifPlayerExist(String playerID){
        return allPlayers.stream().anyMatch(player -> player.getPlayerID().equals(playerID)); 
    }

    public static Player getPlayerByID(String playerID){
        return allPlayers.stream()
                .filter(player -> player.getPlayerID().equals(playerID))
                .findFirst()
                .orElse(null);
    }

    public static List<Player> getAllPlayers(){
        return new ArrayList<>(allPlayers);
    }

    public static List<String> getAllPlayersID(){
        return allPlayers.stream()
                .map(Player:: getPlayerID)
                .collect(java.util.stream.Collectors.toList());
    }

    public static Player createNewPlayer(String name, String id, int level, int floor){
        if (ifPlayerExist(id)) {
            System.out.println("Player ID already exists: "+ id);
            return null;
        }
        return new Player(name, id, level, floor);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    public void levelUp() {
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
    ///////////////////
    // public List<String> listPlayers(){
    //     return allPlayersID;
    // }

    // public void addPlayer(String playerID){
    //     if (playerID!=null && !(allPlayersID.contains(playerID))) {
    //         allPlayersID.add(playerID);
    //     }
    //     else{
    //         System.out.println("player ID is null or player already exist in data");
    //     }
    // }

    // // if exist 
    // // get player 
    // public static boolean ifPlayerExist(String playerID){
    //     return allPlayersID.contains(playerID);
    // }

    // public static List<String> getAllPlayerID(){
    //     return new ArrayList<>(allPlayersID);
    // }
    // // public static String getPlayerName(String playID){
    // //     if (playID!=null && allPlayersID.contains(playID)) {
    // //         return
    // //     }
    // //     return null;
    // // }

    // ///////////////////

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
