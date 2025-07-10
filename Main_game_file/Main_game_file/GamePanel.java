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
    private boolean shieldActive = false; // Shield protection status
    private boolean gamePaused = false;
    private Timer gameTimer;
    private JPanel pauseMenuPanel;
    private boolean pauseMenuVisible = false;
    
    public GamePanel(Game.Player existingPlayer) {

        this.gamePlayer=existingPlayer;
        this.level=existingPlayer.getCurrentFloorLevel();

        setFocusable(true);
        requestFocusInWindow();
        setBackground(Color.BLACK);

        Music.initializeMusic();
        Music.startMusic();

        gameTimer = new Timer(100, e -> {
            if (!gamePaused && !gameOver) {
                repaint();
            }
        });
        gameTimer.start();

        initializeGame();
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver) return;

                if (e.getKeyCode()==KeyEvent.VK_SPACE) {
                    togglePause();
                    repaint();
                    return;
                }

                if (gamePaused) return;
                
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
                    case KeyEvent.VK_F -> { // Use item/food
                        useFood();
                        repaint();
                        return;
                    }
                    case KeyEvent.VK_P -> { // Use power
                        gamePlayer.usePower();
                        repaint();
                        return;
                    }
                    case KeyEvent.VK_Z -> { // inventory
                        gamePlayer.getInventoryItems();
                        showInventoryWindow();
                        repaint();
                        return;
                    }
                    case KeyEvent.VK_I -> { // attack 
                        Game.Items rightHandItem = gamePlayer.getItemOnHands().seeItemOnRight();

                        // Check if we have either a Weapon or BowAndarow equipped
                        if (rightHandItem instanceof Weapon || rightHandItem instanceof BowAndarow) {
                            int totalDamage = gamePlayer.attack();
                            boolean enemyKilled = performAttack(gamePlayer, player, dungeon);
                        
                            if (enemyKilled) {
                                String weaponType = rightHandItem instanceof Weapon ? "Weapon" : "Bow";
                                
                                gamePlayer.gainExperience(1);
                                JOptionPane.showMessageDialog(GamePanel.this, 
                                    "Attack successful! Enemy defeated with " + weaponType + "! Total damage: " + totalDamage);
                            } else {
                                String weaponType = rightHandItem instanceof Weapon ? "Weapon" : "Bow";
                                JOptionPane.showMessageDialog(GamePanel.this, 
                                    "Attack failed! No enemies in range for " + weaponType + ". Total damage: " + totalDamage);
                            }
                        } else {
                            JOptionPane.showMessageDialog(GamePanel.this, 
                                "No weapon in right hand! Press 'O' to equip a Sword or 'B' to equip Bow and Arrow.");
                        }
                        repaint();
                        break;
                    }
                  
                    case KeyEvent.VK_B -> { // put bow
                        for (Game.Items items : gamePlayer.getInventoryItems().getItemInInventory()) {
                            if (items instanceof BowAndarow) {
                                if (gamePlayer.equipItemToRightHand(items)) {
                                    JOptionPane.showMessageDialog(GamePanel.this, 
                                    "Equipped " +items.getType()+ " to the right hand! ");
                                }
                                else{
                                    JOptionPane.showMessageDialog(GamePanel.this, 
                                    "Couldn't equip "+ items.getType()+ " - right hand may be full");
                                }
                                break;
                            }
                        }
                        repaint();
                        break;
                    }
                    case KeyEvent.VK_K -> { // use shield
                        Game.Items leftHandItem= gamePlayer.getItemOnHands().seeItemOnLeft();

                        if (leftHandItem instanceof Shield) {
                            // Shield shield= (Shield) leftHandItem;
                            shieldActive = !shieldActive;
                            if (shieldActive) {
                                JOptionPane.showMessageDialog(GamePanel.this, 
                                "Shield activated! You are now protected from enemy attacks.");
                            } else {
                                JOptionPane.showMessageDialog(GamePanel.this, 
                                "Shield deactivated! You are vulnerable to attacks again.");
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(GamePanel.this, 
                            "There's no shield on left hand! Press L to equip a shield.");
                        }
                        repaint();
                        break;
                    }
                    case KeyEvent.VK_O -> { // put weapon on right
                        for (Game.Items items : gamePlayer.getInventoryItems().getItemInInventory()) {
                            if (items instanceof Weapon) {
                                if (gamePlayer.equipItemToRightHand(items)) {
                                    JOptionPane.showMessageDialog(GamePanel.this, 
                                    "Equipped " +items.getType()+ " to the right hand! ");
                                }
                                else{
                                    JOptionPane.showMessageDialog(GamePanel.this, 
                                    "Couldn't equip "+ items.getType()+ " - right hand may be full");
                                }
                                break;
                            }
                        }
                        repaint();
                        break;
                    }
                    case KeyEvent.VK_L -> { // put shield on left
                        for (Game.Items items : gamePlayer.getInventoryItems().getItemInInventory()) {
                            if (items instanceof Shield) {
                                if (gamePlayer.equipItemToLeftHand(items)) {
                                    JOptionPane.showMessageDialog(GamePanel.this, 
                                    "Equipped " + items.getType() + " to the left hand! ");
                                }
                                else{
                                    JOptionPane.showMessageDialog(GamePanel.this, 
                                    "Couldn't equip " + items.getType() + " - left hand may be full");
                                }
                                break;
                            }
                        }
                        repaint();
                        break;
                    }
                    case KeyEvent.VK_R -> { // Remove right hand item
                        Game.Items rightHandItems = gamePlayer.getItemOnHands().seeItemOnRight();

                        if (rightHandItems != null) {
                            // Check if inventory has space
                            if (gamePlayer.getInventoryItems().getCurrentItemsCount() < 
                                gamePlayer.getInventoryItems().getInventorySize()) {
                                
                                Game.Inventory inventory = gamePlayer.getInventoryItems();
                                inventory.addItem(rightHandItems);
                                gamePlayer.unequipRightHand();
                                
                                JOptionPane.showMessageDialog(GamePanel.this, 
                                    "Item " + rightHandItems.getType() + " removed and put back in inventory");
                            } else {
                                JOptionPane.showMessageDialog(GamePanel.this, 
                                    "Cannot remove item - inventory is full!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(GamePanel.this, 
                                "No items in right hand");
                        }
                        repaint();
                        break;
                    }
                    case KeyEvent.VK_T -> { // Remove left hand item
                        Game.Items leftHandItems = gamePlayer.getItemOnHands().seeItemOnLeft();

                        if (leftHandItems != null) {
                            if (gamePlayer.getInventoryItems().getCurrentItemsCount() < 
                                gamePlayer.getInventoryItems().getInventorySize()) {
                                
                                Game.Inventory inventory = gamePlayer.getInventoryItems();
                                inventory.addItem(leftHandItems);
                                gamePlayer.unequipLeftHand();
                                
                                JOptionPane.showMessageDialog(GamePanel.this, 
                                    "Item " + leftHandItems.getType() + " removed and put back in inventory");
                            } else {
                                JOptionPane.showMessageDialog(GamePanel.this, 
                                    "Cannot remove item - inventory is full!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(GamePanel.this, 
                                "No items in left hand");
                        }
                        repaint();
                        break;
                    }

                    case KeyEvent.VK_SPACE -> { // pause game
                        togglePause();
                        repaint();
                        break;
                    }
                }
                
                if (dir != null) {
                    player.move(dir, dungeon);
                    
                    if (dungeon.isAtDoor(player.getX(), player.getY())) {
                        level++;
                        gamePlayer.setCurrentFloorLevel(level);
                        gamePlayer.gainExperience(50 * level); // More XP for higher levels
                        
                        JOptionPane.showMessageDialog(GamePanel.this, 
                            "Level " + level + " - You found the door! +XP");
                        initializeGame(); // Start new level

                    } else {
                        if (!shieldActive) {
                            dungeon.updateEnemies();
                        }
                        
                        if (!shieldActive && player.isDead()) {
                            gameOver = true;
                            // Music.stopMusicOrPause();
                            repaint(); // Repaint to show "GAME OVER" before dialog
                            int choice = JOptionPane.showConfirmDialog(GamePanel.this, 
                                "Game Over! You reached level " + level + ".\nDo you want to play again?", 
                                "Game Over", JOptionPane.YES_NO_OPTION);
                            
                            if (choice == JOptionPane.YES_OPTION) {
                                resetGame(); 
                            } else {
                                System.exit(0); 
                            }
                        }
                    }
                    
                    repaint();
                }
            }
        });
    }

    public void togglePause(){
        gamePaused= !gamePaused;
        if (gamePaused) {
            if (gameTimer!=null) {
                gameTimer.stop();
            }
            // Music.stopMusicOrPause();
            showPauseMenu();
        }
        else{
            if (gameTimer!=null) {
                gameTimer.start();
            }
            Music.startMusic();
            hidePauseMenu();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    public void showPauseMenu(){
        if (pauseMenuPanel==null) {
            createPauseMenu();
        }
        
        pauseMenuVisible=true;
        pauseMenuPanel.setVisible(true);
        this.add(pauseMenuPanel);
        this.setComponentZOrder(pauseMenuPanel, 0);
        this.revalidate();
        this.repaint();
    }

    public void hidePauseMenu(){
        if (pauseMenuPanel!=null) {
            pauseMenuVisible=false;
            pauseMenuPanel.setVisible(false);
            this.remove(pauseMenuPanel);
            this.revalidate();
            this.repaint();
        }
    }

    private void createPauseMenu() {
        pauseMenuPanel = new JPanel();
        pauseMenuPanel.setOpaque(false);
        pauseMenuPanel.setLayout(new BoxLayout(pauseMenuPanel, BoxLayout.Y_AXIS));
        
        pauseMenuPanel.setBounds(0, 0, getWidth(), getHeight());
        
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setOpaque(false);
        buttonContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        buttonContainer.add(Box.createVerticalStrut(200));
        
        JButton resumeButton = createPauseButton("Resume");
        JButton saveAndQuitButton = createPauseButton("Save and Quit");
        JButton resetButton = createPauseButton("Reset Game");
        
        // adding button action
        resumeButton.addActionListener(e -> {
            togglePause(); // this will resu,e the game 
        });

        saveAndQuitButton.addActionListener(e -> {
            gamePlayer.setCurrentFloorLevel(level);
            gameOver = true;
            hidePauseMenu();

            JOptionPane.showMessageDialog(GamePanel.this, 
                "Game saved! Level: " + level + "\nReturning to main menu...");

            // here in a normal game, you would typically return to main menu
            // however, because i didnt make a main menu, itll just quite and fuck off
            System.exit(0);
        });

        resetButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(GamePanel.this,
                "Are you sure you want to reset the game?\nAll progress will be lost!",
                "Reset Game", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                hidePauseMenu();
                resetGame();
            }
        });

        buttonContainer.add(resumeButton);
        buttonContainer.add(Box.createVerticalStrut(20));
        buttonContainer.add(saveAndQuitButton);
        buttonContainer.add(Box.createVerticalStrut(20));
        buttonContainer.add(resetButton);

        pauseMenuPanel.add(buttonContainer);
    }

    private JButton createPauseButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.setMinimumSize(new Dimension(200, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setFont(new Font("Arial", Font.BOLD, 16));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(Color.DARK_GRAY);
            }
        });

        return button;
    }



    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public GamePanel(){
        this(null);
    }
    
    
    private void initializeGame() {
        player = new Player(2, 2);
        dungeon = new DungeonMap(40, 20, player);
        gameOver = false;
        

        if (gamePlayer == null) { // creating this if the player not existing
            gamePlayer = new Game.Player("Hero", "PLAYER001", 1, level);
            
            // Give starting items
            gamePlayer.getInventoryItems().addItem(new Food("Bread", "FOOD001", 3, 20, 10));
            gamePlayer.getInventoryItems().addItem(new Weapon("Sword", "WEAPON001", 1, 15));
            gamePlayer.getInventoryItems().addItem(new Shield("Shield", "SHIELD001", 1, 10, 100));
            gamePlayer.getInventoryItems().addItem(new BowAndarow("Bow", "BOW001", 1, 12, 100));
            gamePlayer.setCurrentPower(new Power("Heal", "Restoration", 0, 30, 0, 5, 1));
        } else {
            gamePlayer.setCurrentFloorLevel(level);

            if (gamePlayer.getInventoryItems().getCurrentItemsCount()==0) {
                // adding this because im testing shit and dont want to waist time for no fucking reason 
                gamePlayer.getInventoryItems().addItem(new Food("Bread", "FOOD001", 3, 20, 10));
                gamePlayer.getInventoryItems().addItem(new Weapon("Sword", "WEAPON001", 1, 15));
                gamePlayer.getInventoryItems().addItem(new Shield("Shield", "SHIELD001", 1, 10, 100));
                gamePlayer.getInventoryItems().addItem(new BowAndarow("Bow", "BOW001", 1, 12, 100));
                gamePlayer.setCurrentPower(new Power("Heal", "Restoration", 0, 30, 0, 5, 1));
            }
        }
        
        int enemyCount = level + 1;
        for (int i = 0; i < enemyCount; i++) {
            if (i % 2 == 0) {
                placeEnemyInWalkablePosition(dungeon, new RandomEnemy(0, 0));
            } else {
                placeEnemyInWalkablePosition(dungeon, new ChaserEnemy(0, 0));
            }
        }
        
        placeRandomItems();
    }
    
    private void placeRandomItems() {
        floorItems.clear();
        Random random = new Random();
        
        int itemCount = 3 + random.nextInt(3);
        for (int i = 0; i < itemCount; i++) {
            Game.Items item = generateRandomItem(random);
            placeItemOnFloor(item);
        }
    }
    
    // having this here for now just for testing 
    private Game.Items generateRandomItem(Random random) {
        int type = random.nextInt(4); // Changed to 4 to include bow
        switch (type) {
            case 0: // Food
                return new Food("Health Potion", "FOOD" + random.nextInt(1000), 1, 
                               10 + random.nextInt(20), 5 + random.nextInt(15));
            case 1: // Weapon
                return new Weapon("Iron Sword", "WEAPON" + random.nextInt(1000), 1, 
                                 8 + random.nextInt(15));
            case 2: // Shield
                return new Shield("Iron Shield", "SHIELD" + random.nextInt(1000), 1, 
                                 5 + random.nextInt(10), 100);
            case 3: // Bow
                return new BowAndarow("Wooden Bow","BOW" + random.nextInt(1000), 1, 6 + random.nextInt(12), 100);
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

    private void showInventoryWindow(){
        JDialog inventoryDialog= new JDialog(SwingUtilities.getWindowAncestor(this), "Inventory", Dialog.ModalityType.MODELESS);
        inventoryDialog.setSize(200, 300);
        inventoryDialog.setLocationRelativeTo(this);
        inventoryDialog.setResizable(false);

        JPanel inventoryPanel= new JPanel();
        inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));
        
        if (gamePlayer!=null && gamePlayer.getInventoryItems()!=null) {
            for (Game.Items items : gamePlayer.getInventoryItems().getItemInInventory()) {
                inventoryPanel.add(new JLabel(items.getType() + " x" + items.getQuantity()));
            }
        }
        else{
            inventoryPanel.add(new JLabel("Inventory not available."));
        }

        inventoryDialog.add(inventoryPanel);
        inventoryDialog.setVisible(true);
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



    
    //////////////////////////////////////

      //////////////////
    public boolean performAttack(Game.Player gamePlayer, Player player, DungeonMap dungeon){
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

    private boolean performMeleeAttack(Player player, DungeonMap dungeon, int damage) {
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

    private boolean performRangedAttack(Player player, DungeonMap dungeon, int damage) {
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

    /////////////////////////////////////





    private void resetGame() {
        level = 1;
        gamePlayer.setCurrentFloorLevel(level);
        shieldActive = false;
        gameOver=false; 
        initializeGame();
        // Music.startMusic();
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
        
        char[][] map = getDungeonMap();
        
        int offsetX = (getWidth() - map[0].length * CELL_SIZE) / 2;
        int offsetY = (getHeight() - map.length * CELL_SIZE) / 2;
        
        drawPlayerInfo(g);
        drawControls(g);
        Font cellFont = new Font("Monospaced", Font.BOLD, 14);
        g.setFont(cellFont);
        
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                int px = offsetX + x * CELL_SIZE;
                int py = offsetY + y * CELL_SIZE;
                
                char cell = map[y][x];
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
                    case '^' -> g.setColor(Color.GREEN);     // Bow
                    default -> g.setColor(Color.WHITE);
                }
                g.drawString(String.valueOf(cell), px + 5, py + 15);
            }
        }

        if (gamePaused) {
            g.setColor(new Color(0, 0, 0, 150)); //sem-transparent overlay
            g.fillRect(0, 0, getWidth(), getHeight());
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            String pauseText = "< PAUSED >";
            int textWidth = g.getFontMetrics().stringWidth(pauseText);
            g.drawString(pauseText, getWidth()/2 - textWidth/2, getHeight()/2 - 270);
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
        g.drawString("Experience: " + gamePlayer.getExperience(), x, y);
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
        y += lineHeight;
        
        // Shield status
        if (shieldActive) {
            g.setColor(Color.CYAN);
            g.drawString("SHIELD ACTIVE - PROTECTED!", x, y);
        }
    }
    
    private void drawControls(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        
        int x = 10;
        int y = getHeight()-200;
        int lineHeight = 12;
        
        g.drawString("=== CONTROLS ===", x, y);
        y += lineHeight;
        g.drawString("WASD: Move", x, y);
        y += lineHeight;
        g.drawString("SPACE: to Pause Game", x, y);
        y += lineHeight;
        g.drawString("E: Pick up item", x, y);
        y += lineHeight;
        g.drawString("F: Use food", x, y);
        y += lineHeight;
        g.drawString("P: Use power", x, y);
        y += lineHeight;
        g.drawString("Z: Open Inventory", x, y);
        y += lineHeight;
        g.drawString("I: Attack", x, y);
        y += lineHeight;
        g.drawString("K: Use Shield (Toggle Protection)", x, y);
        y += lineHeight;
        g.drawString("O: Equip Weapon (Right)", x, y);
        y += lineHeight;
        g.drawString("B: Equip Bow (Right)", x, y);
        y += lineHeight;
        g.drawString("L: Equip Shield (Left)", x, y);
        y += lineHeight;
        g.drawString("R: Un-Equip Weapon (Right)", x, y);
        y += lineHeight;
        g.drawString("T: Un-Equip Weapon (Left)", x, y);
        y += lineHeight;
        // y += lineHeight;
        g.drawString("Legend: ! = Weapon, % = Shield", x, y);
        y += lineHeight;
        g.drawString("        * = Food, ^ = Bow", x, y);
    }
    
    private char[][] getDungeonMap() {
        char[][] display = dungeon.renderToCharArray();
        
        // Add floor items to the display
        for (Game.Items item : floorItems) {
            PositionedItem posItem = (PositionedItem) item;
            int x = posItem.getX();
            int y = posItem.getY();
            
            // Fixed: Corrected the array indexing (y first, then x)
            if (y >= 0 && y < display.length && x >= 0 && x < display[0].length) {
                if (item instanceof Weapon) {
                    display[y][x] = '!';
                } else if (item instanceof Shield) {
                    display[y][x] = '%';
                } else if (item instanceof BowAndarow) {
                    display[y][x] = '^';
                } else if (item instanceof Food) {
                    display[y][x] = '*';
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