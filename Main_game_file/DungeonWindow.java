package Main_game_file;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class DungeonWindow extends JFrame{
    public DungeonWindow(){
        setTitle("Dungeon Game - RPG Adventure");
        setSize(1200, 800); // Increased size for player info panel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        GamePanel panel = new GamePanel();
        add(panel);
        panel.requestFocusInWindow(); // Ensure the panel gets focus for key events
        setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DungeonWindow::new);
    }
}