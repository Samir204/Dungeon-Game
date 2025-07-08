package Main_game_file;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DungeonWindow extends JFrame{

    public static void showWelcomeScreen(){
        JFrame welcomeFrame = new JFrame("Welcome to my simple RPG adventure game");
        welcomeFrame.setSize(600, 400);
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomeFrame.setLocationRelativeTo(null);
        welcomeFrame.setResizable(false);

        // Create welcome panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(Color.BLACK);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Welcome title
        JLabel titleLabel = new JLabel("DUNGEON ADVENTURE");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("A Simple RPG Game");
        subtitleLabel.setForeground(Color.LIGHT_GRAY);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Instructions
        JLabel instructionsLabel = new JLabel("Click 'Start Game' to begin your adventure!");
        instructionsLabel.setForeground(Color.YELLOW);
        instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start button
        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(150, 40));
        startButton.setBackground(Color.DARK_GRAY);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        // Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setMaximumSize(new Dimension(150, 40));
        exitButton.setBackground(Color.DARK_GRAY);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        // Add components to panel
        welcomePanel.add(titleLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        welcomePanel.add(subtitleLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 40)));
        welcomePanel.add(instructionsLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 40)));
        welcomePanel.add(startButton);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        welcomePanel.add(exitButton);

        // Button actions
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcomeFrame.dispose(); // Close welcome screen
                SwingUtilities.invokeLater(DungeonWindow::showPlayerSetupScreen); // Show player setup
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        welcomeFrame.add(welcomePanel);
        welcomeFrame.setVisible(true);
    }

    public static void showPlayerSetupScreen() {
        JFrame setupFrame = new JFrame("Player Setup - Dungeon Adventure");
        setupFrame.setSize(500, 400);
        setupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupFrame.setLocationRelativeTo(null);
        setupFrame.setResizable(false);

        JPanel setupPanel = new JPanel();
        setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.Y_AXIS));
        setupPanel.setBackground(Color.BLACK);
        setupPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("PLAYER SETUP");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Instructions
        JLabel instructionsLabel = new JLabel("Enter your player details or create a new account");
        instructionsLabel.setForeground(Color.LIGHT_GRAY);
        instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Name input
        JLabel nameLabel = new JLabel("Player Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameField = new JTextField(20);
        nameField.setMaximumSize(new Dimension(300, 30));
        nameField.setBackground(Color.DARK_GRAY);
        nameField.setForeground(Color.WHITE);
        nameField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        // Player ID input
        JLabel idLabel = new JLabel("Player ID (leave empty for new account):");
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField idField = new JTextField(20);
        idField.setMaximumSize(new Dimension(300, 30));
        idField.setBackground(Color.DARK_GRAY);
        idField.setForeground(Color.WHITE);
        idField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        // Status label for feedback
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.YELLOW);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buttons
        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(120, 35));
        loginButton.setBackground(Color.DARK_GRAY);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JButton createButton = new JButton("Create New");
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createButton.setMaximumSize(new Dimension(120, 35));
        createButton.setBackground(Color.DARK_GRAY);
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(120, 35));
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        // Button panel for horizontal layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(loginButton);
        buttonPanel.add(createButton);
        buttonPanel.add(backButton);

        // Add components to panel
        setupPanel.add(titleLabel);
        setupPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        setupPanel.add(instructionsLabel);
        setupPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        setupPanel.add(nameLabel);
        setupPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        setupPanel.add(nameField);
        setupPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        setupPanel.add(idLabel);
        setupPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        setupPanel.add(idField);
        setupPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        setupPanel.add(statusLabel);
        setupPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        setupPanel.add(buttonPanel);

        // Button actions
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String id = idField.getText().trim();
                
                if (name.isEmpty()) {
                    statusLabel.setText("Please enter your name");
                    statusLabel.setForeground(Color.RED);
                    return;
                }
                
                if (id.isEmpty()) {
                    statusLabel.setText("Please enter your Player ID or click 'Create New'");
                    statusLabel.setForeground(Color.RED);
                    return;
                }
                
                // Here you would typically validate the player ID against a database
                // For now, we'll just proceed with the game
                statusLabel.setText("Login successful! Starting game...");
                statusLabel.setForeground(Color.GREEN);
                
                // Start the game with existing player data
                SwingUtilities.invokeLater(() -> {
                    setupFrame.dispose();
                    new DungeonWindow(name, id, 1, 1); // Assuming level 1, floor 1 for existing player
                });
            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                
                if (name.isEmpty()) {
                    statusLabel.setText("Please enter your name");
                    statusLabel.setForeground(Color.RED);
                    return;
                }
                
                // Generate a unique player ID
                String newPlayerID = generatePlayerID();
                
                statusLabel.setText("New account created! Player ID: " + newPlayerID);
                statusLabel.setForeground(Color.GREEN);
                
                // Start the game with new player data
                SwingUtilities.invokeLater(() -> {
                    setupFrame.dispose();
                    new DungeonWindow(name, newPlayerID, 1, 1); // New player starts at level 1, floor 1
                });
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setupFrame.dispose();
                SwingUtilities.invokeLater(DungeonWindow::showWelcomeScreen);
            }
        });

        setupFrame.add(setupPanel);
        setupFrame.setVisible(true);
    }

    private static String generatePlayerID() {
        // Generate a unique player ID (simple implementation)
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return "PLR" + timestamp + random;
    }

    // Store player data for use in the game
    private static String playerName;
    private static String playerID;
    private static int playerLevel;
    private static int currentFloor;

    public DungeonWindow(){
        this("DefaultPlayer", "DEFAULT001", 1, 1);
    }

    public DungeonWindow(String name, String id, int level, int floor){
        playerName = name;
        playerID = id;
        playerLevel = level;
        currentFloor = floor;
        
        setTitle("Dungeon Game - RPG Adventure - " + name);
        setSize(1200, 800); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        GamePanel panel = new GamePanel();
        add(panel);
        panel.requestFocusInWindow(); 
        setVisible(true);
    }

    // Getters for player data
    public static String getPlayerName() { return playerName; }
    public static String getPlayerID() { return playerID; }
    public static int getPlayerLevel() { return playerLevel; }
    public static int getCurrentFloor() { return currentFloor; }
    
    // Main method removed - now handled by Main.java
}