package Main_game_file;
// import Game.*; 

/**
 * the main that runs all this shit
 * 
 * compilation: javac Main_game_file/*.java Game/*.java
 * GUI Mode: java Main_game_file.DungeonWindow
 */
public class Main {
        
    public static void main(String[] args) {
        if (args.length>0 && args[0].equals("console")) {
            System.out.println("Console mode not yet implemented");
        }
        else{
        javax.swing.SwingUtilities.invokeLater(() -> DungeonWindow.showWelcomeScreen());
        }
    }
}