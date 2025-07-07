package Main_game_file;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Random;
import Game.*;

public class GamePanel extends JPanel {
    private DungeonMap dungeon;
    private Player player;
    private Game.Player gamePlayer; // RPG player with inventory
    private int level = 1;
    private final int CELL_SIZE = 20;
    private boolean gameOver = false;
    private List<Game.Items> floorItems = new ArrayList<>(); // Items on the floor
    
    public GamePanel() {
        setFocusable(true);
        requestFocusInWindow();
        setBackground(Color.BLACK);
        
        // Initialize game
        initializeGame();
        
        // Add movement controls
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver) return;
                
                Direction dir = null;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> dir = Direction.UP;
                    case KeyEvent.VK_S -> dir = Direction.DOWN;
                    case KeyEvent.VK_A -> dir = Direction.LEFT;
                    case KeyEvent.VK_D -> dir = Direction.RIGHT;
                    case KeyEvent.VK_E -> { // Pick up items
                        pickupItem();
                        repaint();
                        return;
                    }
                    case KeyEvent.VK_Q -> { // Use item/food
                        useFood();
                        repaint();
                        return;
                    }
                    case KeyEvent.VK_R -> { // Use power
                        gamePlayer.usePower();
                        repaint();
                        return;
                    }
                }
                
                if (dir != null) {
                    player.move(dir, dungeon);
                    
                    // Check if player reached the door
                    if (dungeon.isAtDoor(player.getX(), player.getY())) {
                        level++;
                        gamePlayer.setCurrentFloorLevel(level);
                        gamePlayer.gainExperience(50 * level); // More XP for higher levels
                        
                        JOptionPane.showMessageDialog(GamePanel.this, 
                            "Level " + level + " - You found the door! +XP");
                        initializeGame(); // Start new level

                    } else {
                        // Update enemies
                        dungeon.updateEnemies();
                        
                        // Check if player died
                        if (player.isDead()) {
                            gameOver = true;
                            repaint(); // Repaint to show "GAME OVER" before dialog
                            int choice = JOptionPane.showConfirmDialog(GamePanel.this, 
                                "Game Over! You reached level " + level + ".\nDo you want to play again?", 
                                "Game Over", JOptionPane.YES_NO_OPTION);
                            
                            if (choice == JOptionPane.YES_OPTION) {
                                resetGame(); // Reset game state and start new game
                            } else {
                                System.exit(0); // Exit the application
                            }
                        }
                    }
                    
                    repaint();
                }
            }
        });
    }
    
    private void initializeGame() {
        player = new Player(2, 2);
        dungeon = new DungeonMap(40, 20, player);
        gameOver = false;
        
        // Initialize RPG player if not exists
        if (gamePlayer == null) {
            gamePlayer = new Game.Player("Hero", "PLAYER001", 1, level);
            
            // Give starting items
            gamePlayer.getInventoryItems().addItem(new Food("Bread", "FOOD001", 3, 20, 10));
            gamePlayer.getInventoryItems().addItem(new Weapon("Sword", "WEAPON001", 1, 15));
            gamePlayer.getInventoryItems().addItem(new Items("Shield", 1, "SHIELD001"));
            gamePlayer.setCurrentPower(new Power("Heal", "Restoration", 0, 30, 0, 5, 1));
        } else {
            gamePlayer.setCurrentFloorLevel(level);
        }
        
        // Place enemies in walkable positions (more enemies each level)
        int enemyCount = level + 1;
        for (int i = 0; i < enemyCount; i++) {
            if (i % 2 == 0) {
                placeEnemyInWalkablePosition(dungeon, new RandomEnemy(0, 0));
            } else {
                placeEnemyInWalkablePosition(dungeon, new ChaserEnemy(0, 0));
            }
        }
        
        // Place random items on the floor
        placeRandomItems();
    }
    
    private void placeRandomItems() {
        floorItems.clear();
        Random random = new Random();
        
        // Place 2-4 random items on the floor
        int itemCount = 2 + random.nextInt(3);
        for (int i = 0; i < itemCount; i++) {
            Game.Items item = generateRandomItem(random);
            placeItemOnFloor(item);
        }
    }
    
    private Game.Items generateRandomItem(Random random) {
        int type = random.nextInt(3);
        switch (type) {
            case 0: // Food
                return new Food("Potion", "FOOD" + random.nextInt(1000), 1, 
                               10 + random.nextInt(20), 5 + random.nextInt(15));
            case 1: // Weapon
                return new Weapon("Weapon", "WEAPON" + random.nextInt(1000), 1, 
                                 5 + random.nextInt(20));
            case 2: // Shield
                return new Items("Shield", 1, "SHIELD" + random.nextInt(1000));
            default:
                return new Food("Apple", "FOOD" + random.nextInt(1000), 1, 5, 5);
        }
    }
    
    private void placeItemOnFloor(Game.Items item) {
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
            
            // Create a positioned item
            PositionedItem posItem = new PositionedItem(item, pos[0], pos[1]);
            floorItems.add(posItem);
        }
    }
    
    private void pickupItem() {
        int playerX = player.getX();
        int playerY = player.getY();
        
        for (int i = floorItems.size() - 1; i >= 0; i--) {
            PositionedItem posItem = (PositionedItem) floorItems.get(i);
            if (posItem.getX() == playerX && posItem.getY() == playerY) {
                if (gamePlayer.getInventoryItems().getCurrentItemsCount() < 
                    gamePlayer.getInventoryItems().getInventorySize()) {
                    gamePlayer.getInventoryItems().addItem(posItem.getItem());
                    floorItems.remove(i);
                    JOptionPane.showMessageDialog(this, "Picked up: " + posItem.getItem().getType());
                } else {
                    JOptionPane.showMessageDialog(this, "Inventory full!");
                }
                break;
            }
        }
    }
    
    private void useFood() {
        for (Game.Items item : gamePlayer.getInventoryItems().getItemInInventory()) {
            if (item instanceof Food) {
                gamePlayer.useItem(item);
                JOptionPane.showMessageDialog(this, "Used: " + item.getType());
                break;
            }
        }
    }

    private void resetGame() {
        level = 1;
        gamePlayer = null; // Reset RPG player
        initializeGame();
        repaint();
    }
    
    private void placeEnemyInWalkablePosition(DungeonMap dungeon, Enemy enemy) {
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
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (dungeon == null) return;
        
        // Get the dungeon map for rendering
        char[][] map = getDungeonMap();
        
        // Calculate offset to center the dungeon
        int offsetX = (getWidth() - map[0].length * CELL_SIZE) / 2;
        int offsetY = (getHeight() - map.length * CELL_SIZE) / 2;
        
        // Draw player info panel
        drawPlayerInfo(g);
        
        // Draw controls info
        drawControls(g);
        
        // Draw the dungeon
        Font cellFont = new Font("Monospaced", Font.BOLD, 14);
        g.setFont(cellFont);
        
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                int px = offsetX + x * CELL_SIZE;
                int py = offsetY + y * CELL_SIZE;
                
                char cell = map[y][x];
                
                // Set color based on cell type
                switch (cell) {
                    case '#' -> g.setColor(Color.GRAY);
                    case '.' -> g.setColor(Color.DARK_GRAY);
                    case '@' -> g.setColor(Color.GREEN);
                    case 'E' -> g.setColor(Color.RED);
                    case 'S' -> g.setColor(Color.ORANGE);
                    case '+' -> g.setColor(Color.YELLOW);
                    case '!' -> g.setColor(Color.CYAN);      // Weapon
                    case '%' -> g.setColor(Color.MAGENTA);   // Shield
                    case '*' -> g.setColor(Color.PINK);      // Food
                    default -> g.setColor(Color.WHITE);
                }
                
                // Draw the character
                g.drawString(String.valueOf(cell), px + 5, py + 15);
            }
        }
        
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            g.drawString("GAME OVER", getWidth()/2 - 60, getHeight()/2);
        }
    }
    
    private void drawPlayerInfo(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 12));
        
        int x = 10;
        int y = 20;
        int lineHeight = 15;
        
        // Player basic info
        g.drawString("=== PLAYER INFO ===", x, y);
        y += lineHeight;
        g.drawString("Name: " + gamePlayer.getName(), x, y);
        y += lineHeight;
        g.drawString("Level: " + gamePlayer.getPlayerLevel(), x, y);
        y += lineHeight;
        g.drawString("Floor: " + level, x, y);
        y += lineHeight;
        
        // Health and Energy
        g.setColor(Color.RED);
        g.drawString("Health: " + gamePlayer.getHealth() + "/" + gamePlayer.getMaxHealth(), x, y);
        y += lineHeight;
        g.setColor(Color.BLUE);
        g.drawString("Energy: " + gamePlayer.getEnergy() + "/" + gamePlayer.getMaxEnergy(), x, y);
        y += lineHeight;
        g.setColor(Color.YELLOW);
        g.drawString("XP: " + gamePlayer.getExperience() + "/" + gamePlayer.getExperienceToNextLevel(), x, y);
        y += lineHeight;
        
        // Equipment
        g.setColor(Color.WHITE);
        g.drawString("=== EQUIPMENT ===", x, y);
        y += lineHeight;
        
        Game.Items leftItem = gamePlayer.getItemOnHands().seeItemOnLeft();
        Game.Items rightItem = gamePlayer.getItemOnHands().seeItemOnRight();
        
        g.drawString("Left: " + (leftItem != null ? leftItem.getType() : "Empty"), x, y);
        y += lineHeight;
        g.drawString("Right: " + (rightItem != null ? rightItem.getType() : "Empty"), x, y);
        y += lineHeight;
        
        // Power
        if (gamePlayer.getCurrentPower() != null) {
            g.setColor(Color.MAGENTA);
            g.drawString("Power: " + gamePlayer.getCurrentPower().getName() + 
                        " (" + gamePlayer.getCurrentPower().getAmountOfCanBeUsed() + " uses)", x, y);
            y += lineHeight;
        }
        
        // Inventory count
        g.setColor(Color.WHITE);
        g.drawString("Inventory: " + gamePlayer.getInventoryItems().getCurrentItemsCount() + 
                    "/" + gamePlayer.getInventoryItems().getInventorySize(), x, y);
    }
    
    private void drawControls(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        
        int x = 10;
        int y = getHeight() - 80;
        int lineHeight = 12;
        
        g.drawString("=== CONTROLS ===", x, y);
        y += lineHeight;
        g.drawString("WASD: Move", x, y);
        y += lineHeight;
        g.drawString("E: Pick up item", x, y);
        y += lineHeight;
        g.drawString("Q: Use food", x, y);
        y += lineHeight;
        g.drawString("R: Use power", x, y);
        y += lineHeight;
        g.drawString("Legend: ! = Weapon, % = Shield, * = Food", x, y);
    }
    
    private char[][] getDungeonMap() {
        char[][] display = dungeon.renderToCharArray();
        
        // Add floor items to the display
        for (Game.Items item : floorItems) {
            PositionedItem posItem = (PositionedItem) item;
            int x = posItem.getX();
            int y = posItem.getY();
            
            if (x >= 0 && x < display.length && y >= 0 && y < display[0].length) {
                if (item instanceof Weapon) {
                    display[x][y] = '!';
                } else if (item.getType().equals("Shield")) {
                    display[x][y] = '%';
                } else if (item instanceof Food) {
                    display[x][y] = '*';
                }
            }
        }
        
        return display;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 700);
    }
    
    // Inner class to hold positioned items
    private class PositionedItem extends Game.Items {
        private int x, y;
        private Game.Items item;
        
        public PositionedItem(Game.Items item, int x, int y) {
            super(item.getType(), item.getQuantity(), item.getItemID());
            this.item = item;
            this.x = x;
            this.y = y;
        }
        
        public int getX() { return x; }
        public int getY() { return y; }
        public Game.Items getItem() { return item; }
    }
}