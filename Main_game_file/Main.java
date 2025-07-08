package Main_game_file;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import Game.*;

/**
 * Integrated Main class that combines the dungeon crawler with RPG elements
 * 
 * Compilation: javac Main_game_file/*.java Game/*.java
 * GUI Mode: java Main_game_file.DungeonWindow
 * Console Mode: java Main_game_file.Main
 */
public class Main {
    
    private static void placeEnemyInWalkablePosition(DungeonMap dungeon, Enemy enemy) {
        Player player = dungeon.getPlayer();
        Set<String> reachable = dungeon.getReachablePositions(player.getX(), player.getY());
        
        List<int[]> validPositions = new ArrayList<>();
        for (String pos : reachable) {
            String[] coords = pos.split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            
            if ((x != player.getX() || y != player.getY()) && !dungeon.hasEnemyAt(x, y)) {
                validPositions.add(new int[]{x, y});
            }
        }
        
        if (!validPositions.isEmpty()) {
            Random random = new Random();
            int[] pos = validPositions.get(random.nextInt(validPositions.size()));
            enemy.setPosition(pos[0], pos[1]);
            dungeon.addEnemy(enemy);
        }
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new DungeonWindow());
    }
    
    private static void playConsoleMode(Scanner scanner) {
        int width = 40;
        int height = 20;
        int level = 1;
        
        // Create RPG player
        Game.Player gamePlayer = new Game.Player("Hero", "PLAYER001", 1, 1);
        
        // Give starting equipment
        gamePlayer.getInventoryItems().addItem(new Food("Bread", "FOOD001", 3, 20, 10));
        gamePlayer.getInventoryItems().addItem(new Weapon("Sword", "WEAPON001", 1, 15));
        gamePlayer.getInventoryItems().addItem(new Shield("Shield", "SHIELD001", 1, 10, 100));
        gamePlayer.setCurrentPower(new Power("Heal", "Restoration", 0, 30, 0, 5, 1));
        
        // Create dungeon player
        Player player = new Player(2, 2);
        
        while (gamePlayer.isAlive()) {
            System.out.println("\n=== LEVEL " + level + " ===");
            System.out.println(gamePlayer.toString());
            
            DungeonMap dungeon = new DungeonMap(width, height, player);
            gamePlayer.setCurrentFloorLevel(level);

            // Place enemies
            int enemyCount = level + 1;
            for (int i = 0; i < enemyCount; i++) {
                if (i % 2 == 0) {
                    placeEnemyInWalkablePosition(dungeon, new RandomEnemy(0, 0));
                } else {
                    placeEnemyInWalkablePosition(dungeon, new ChaserEnemy(0, 0));
                }
            }

            // Level game loop
            while (gamePlayer.isAlive()) {
                System.out.println("\nLevel: " + level);
                System.out.println("Health: " + gamePlayer.getHealth() + "/" + gamePlayer.getMaxHealth());
                System.out.println("Energy: " + gamePlayer.getEnergy() + "/" + gamePlayer.getMaxEnergy());
                
                dungeon.render();
                System.out.print("Action (w/a/s/d/q=use food/r=use power/i=inventory): ");
                String input = scanner.nextLine();
                
                if (input.equals("i")) {
                    showInventory(gamePlayer);
                    continue;
                } else if (input.equals("q")) {
                    useFood(gamePlayer);
                    continue;
                } else if (input.equals("r")) {
                    if (gamePlayer.usePower()) {
                        System.out.println("Power used successfully!");
                    } else {
                        System.out.println("Cannot use power!");
                    }
                    continue;
                }
                
                Direction dir = Direction.fromInput(input);
                if (dir != null) {
                    player.move(dir, dungeon);
                    
                    // Check if reached door
                    if (dungeon.isAtDoor(player.getX(), player.getY())) {
                        level++;
                        gamePlayer.gainExperience(50 * level);
                        System.out.println("You found the door! Advancing to level " + level);
                        System.out.println("Gained " + (50 * level) + " experience!");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        break;
                    }
                    
                    // Update enemies and check for damage
                    dungeon.updateEnemies();
                    if (player.isDead()) {
                        gamePlayer.takeDamage(20);
                        player.setDead(false); // Reset for RPG health system
                        System.out.println("You were attacked! Health: " + gamePlayer.getHealth());
                    }
                }
            }
        }

        System.out.println("\nGame Over! You reached level " + level);
        System.out.println("Final Stats:");
        System.out.println(gamePlayer.toString());
    }

    //////////////////
    public static boolean performAttack(Game.Player gamePlayer, Player player, DungeonMap dungeon){
    Game.Items rightHandItem= gamePlayer.getItemOnHands().seeItemOnRight();

    if (rightHandItem instanceof Weapon) {
        // (swords)
        Weapon weapon = (Weapon) rightHandItem;
        int totalDamage = gamePlayer.attack();

        if (weapon.getType().toLowerCase().contains("sword")) {
            boolean enemyKilled = performMeleeAttack(player, dungeon, totalDamage);
            return enemyKilled;
        }
    } 
    else if (rightHandItem instanceof BowAndarow) {
        // (bows)
        int totalDamage = gamePlayer.attack();
        boolean enemyKilled = performRangedAttack(player, dungeon, totalDamage);
        return enemyKilled;
    }

    return false;
}

    private static boolean performMeleeAttack(Player player, DungeonMap dungeon, int damage) {
        int playerX = player.getX();
        int playerY = player.getY();
        
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
        
        for (int i = 0; i < 8; i++) {
            int checkX = playerX + dx[i];
            int checkY = playerY + dy[i];
            
            // Check if there's an enemy at this position
            Enemy enemy = dungeon.getEnemyAt(checkX, checkY);
            if (enemy != null) {
                // For melee, prioritize 'E' type enemies (ChaserEnemy)
                if (enemy instanceof ChaserEnemy) {
                    dungeon.removeEnemy(enemy);
                    return true;
                }
            }
        }
        
        // If no ChaserEnemy found, attack any adjacent enemy
        for (int i = 0; i < 8; i++) {
            int checkX = playerX + dx[i];
            int checkY = playerY + dy[i];
            
            Enemy enemy = dungeon.getEnemyAt(checkX, checkY);
            if (enemy != null) {
                dungeon.removeEnemy(enemy);
                return true;
            }
        }
        
        return false;
    }

    private static boolean performRangedAttack(Player player, DungeonMap dungeon, int damage) {
        int playerX = player.getX();
        int playerY = player.getY();
        
        // Check all positions within 2 block radius
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                // Skip the player's position
                if (dx == 0 && dy == 0) continue;
                
                // Calculate distance (Manhattan distance for simplicity)
                int distance = Math.abs(dx) + Math.abs(dy);
                if (distance > 2) continue; // Only within 2 blocks
                
                int checkX = playerX + dx;
                int checkY = playerY + dy;
                
                // Check if there's an enemy at this position
                Enemy enemy = dungeon.getEnemyAt(checkX, checkY);
                if (enemy != null) {
                    // For ranged, prioritize 'S' type enemies (RandomEnemy)
                    if (enemy instanceof RandomEnemy) {
                        dungeon.removeEnemy(enemy);
                        return true;
                    }
                }
            }
        }
        
        // If no RandomEnemy found, attack any enemy within range
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                if (dx == 0 && dy == 0) continue;
                
                int distance = Math.abs(dx) + Math.abs(dy);
                if (distance > 2) continue;
                
                int checkX = playerX + dx;
                int checkY = playerY + dy;
                
                Enemy enemy = dungeon.getEnemyAt(checkX, checkY);
                if (enemy != null) {
                    dungeon.removeEnemy(enemy);
                    return true;
                }
            }
        }
        
        return false;
    }

    //////////////////
    
    private static void showInventory(Game.Player player) {
        System.out.println("\n=== INVENTORY ===");
        List<Game.Items> items = player.getInventoryItems().getItemInInventory();
        if (items.isEmpty()) {
            System.out.println("Inventory is empty!");
        } else {
            for (Game.Items item : items) {
                System.out.println("- " + item.toString());
            }
        }
        
        System.out.println("\n=== EQUIPMENT ===");
        Game.Items left = player.getItemOnHands().seeItemOnLeft();
        Game.Items right = player.getItemOnHands().seeItemOnRight();
        System.out.println("Left Hand: " + (left != null ? left.getType() : "Empty"));
        System.out.println("Right Hand: " + (right != null ? right.getType() : "Empty"));
        
        if (player.getCurrentPower() != null) {
            System.out.println("Power: " + player.getCurrentPower().toString());
        }
    }
    
    private static void useFood(Game.Player player) {
        List<Game.Items> items = player.getInventoryItems().getItemInInventory();
        for (Game.Items item : items) {
            if (item instanceof Food) {
                player.useItem(item);
                System.out.println("Used: " + item.getType());
                return;
            }
        }
        System.out.println("No food available!");
    }

    
}