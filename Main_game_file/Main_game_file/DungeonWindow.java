package Main_game_file;
import Game.Player;

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

        // welcome panel
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
        instructionsLabel.setForeground(Color.CYAN);
        instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start 
        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(150, 40));
        startButton.setBackground(Color.DARK_GRAY);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        // Exit 
        JButton exitButton = new JButton("Exit");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setMaximumSize(new Dimension(150, 40));
        exitButton.setBackground(Color.DARK_GRAY);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        welcomePanel.add(titleLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        welcomePanel.add(subtitleLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 40)));
        welcomePanel.add(instructionsLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 40)));
        welcomePanel.add(startButton);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        welcomePanel.add(exitButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcomeFrame.dispose(); 
                SwingUtilities.invokeLater(DungeonWindow::showPlayerSetupScreen); 
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
                    statusLabel.setText("Please enter your Player ID or click 'Create New' and you'll get a player ID");
                    statusLabel.setForeground(Color.RED);
                    return;
                }
                
                if (!Player.ifPlayerExist(id)) {
                    statusLabel.setText("Player ID not found. Please enter your correct player ID or create new one");
                    statusLabel.setForeground(Color.RED);
                    return;
                }


                Player existingPlayer= Player.getPlayerByID(id);
                if (existingPlayer==null) {
                    statusLabel.setText("Error loading player date. why? because FUCK YOU");
                    statusLabel.setForeground(Color.RED);
                    return;
                }

                statusLabel.setText("Login successful! Starting game...");
                statusLabel.setForeground(Color.GREEN);
                
                SwingUtilities.invokeLater(() -> {
                    setupFrame.dispose();
                    new DungeonWindow(existingPlayer);
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
                
                String tempPlayerID =generatePlayerID();
                while (Player.ifPlayerExist(tempPlayerID)) {
                    tempPlayerID =generatePlayerID();
                }
                final String newPlayerID= tempPlayerID; // this is here because of this error: Local variable newPlayerID is required to be final or effectively final based on its usage
                
                
                statusLabel.setText("Creating new account...");
                statusLabel.setForeground(Color.CYAN);
                
                Player newPlayer= new Player(name, newPlayerID, 1,1);

                if (newPlayer!=null ) {
                    setupFrame.dispose();
                    showNewPlayerInfoScreen(newPlayer);
                }
                // else{
                //     statusLabel.setText("Failed to create account. why? because FUCK YOU");
                //     statusLabel.setForeground(Color.RED);
                // }
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
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return "PLR" + timestamp + random;
    }

    private static Player currentPlayer;

    public DungeonWindow(Player player){
        currentPlayer =player;
        setTitle("Dungeon Game - RPG Adventure - " + player.getName());
        setSize(1200, 800); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        GamePanel panel= new GamePanel(player);
        add(panel);
        panel.requestFocusInWindow();
        setVisible(true);
    }

    public static Player getCurrenPlayer(){ return currentPlayer; }


    /////////////////////
    public static void showNewPlayerInfoScreen(Player player){
        JFrame newPlayerWindow = new JFrame("New Player INFO");
        newPlayerWindow.setSize(600, 400);
        newPlayerWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newPlayerWindow.setLocationRelativeTo(null);
        newPlayerWindow.setResizable(false);

        // panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));


        JLabel title = new JLabel("NEW PLAYER CREATED!");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // message
        JLabel successMsg = new JLabel("Your account has been successfully created!");
        successMsg.setForeground(Color.GREEN);
        successMsg.setFont(new Font("Arial", Font.PLAIN, 16));
        successMsg.setAlignmentX(Component.CENTER_ALIGNMENT);


        // player name
        JLabel name = new JLabel("You player name is: "+ player.getName());
        name.setForeground(Color.CYAN);
        name.setFont(new Font("Arial", Font.PLAIN, 16));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);


        // player ID
        JLabel playerID = new JLabel("You player ID is: "+ player.getPlayerID());
        playerID.setForeground(Color.BLUE);
        playerID.setFont(new Font("Arial", Font.PLAIN, 16));
        playerID.setAlignmentX(Component.CENTER_ALIGNMENT);

        // note1
        JLabel note1 = new JLabel("Please save your Player ID, because if you dont");
        note1.setForeground(Color.RED);
        note1.setFont(new Font("Arial", Font.ITALIC, 14));
        note1.setAlignmentX(Component.CENTER_ALIGNMENT);

        // note2
        JLabel note2 = new JLabel("well you better enjoy the remaining 24 houres");
        note2.setForeground(Color.RED);
        note2.setFont(new Font("Arial", Font.ITALIC, 14));
        note2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start 
        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(150, 40));
        startButton.setBackground(Color.DARK_GRAY);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(successMsg);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(name);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(playerID);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(note1);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(note2);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newPlayerWindow.dispose(); 
                // start 
                SwingUtilities.invokeLater(() -> {
                    new DungeonWindow(player);
                });
            }
        });

        newPlayerWindow.add(panel);
        newPlayerWindow.setVisible(true);
    }

}


//present player with his info after creatig new account