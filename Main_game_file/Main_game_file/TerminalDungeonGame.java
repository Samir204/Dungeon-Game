package Main_game_file;
import java.util.*;

// === Direction Enum ===
enum Direction {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);
    public final int dx, dy;
    Direction(int dx, int dy) { this.dx = dx; this.dy = dy; }

    public static Direction fromInput(String input) {
        return switch (input.toLowerCase()) {
            case "w" -> UP;
            case "s" -> DOWN;
            case "a" -> LEFT;
            case "d" -> RIGHT;
            default -> null;
        };
    }

    public static Direction random() {
        return values()[new java.util.Random().nextInt(values().length)];
    }
}

// === Entity ===
abstract class Entity {
    protected int x, y;
    public Entity(int x, int y) { this.x = x; this.y = y; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
    public abstract char getSymbol();
}

// === Player ===
class Player extends Entity {
    private boolean dead = false;
    public Player(int x, int y) { super(x, y); }
    public char getSymbol() { return '@'; }
    public boolean isDead() { return dead; }
    public void setDead(boolean dead) { this.dead = dead; }

    public void move(Direction dir, DungeonMap dungeon) {
        int nx = x + dir.dx;
        int ny = y + dir.dy;
        if (dungeon.isWalkable(nx, ny)) {
            setPosition(nx, ny);
        }
    }
}

// === Enemy ===
abstract class Enemy extends Entity {
    public Enemy(int x, int y) { super(x, y); }
    public abstract void takeTurn(Player player, DungeonMap dungeon);
}

class RandomEnemy extends Enemy {
    public RandomEnemy(int x, int y) { super(x, y); }
    public char getSymbol() { return 'E'; }

    public void takeTurn(Player player, DungeonMap dungeon) {
        Direction dir = Direction.random();
        int nx = x + dir.dx;
        int ny = y + dir.dy;
        if (dungeon.isWalkable(nx, ny) && !dungeon.hasEnemyAt(nx, ny)) {
            setPosition(nx, ny);
        }
    }
}

class ChaserEnemy extends Enemy {
    public ChaserEnemy(int x, int y) { super(x, y); }
    public char getSymbol() { return 'S'; }

    public void takeTurn(Player player, DungeonMap dungeon) {
        int dx = Integer.compare(player.getX(), x);
        int dy = Integer.compare(player.getY(), y);
        
        // Try to move towards player
        int nx = x + dx;
        int ny = y + dy;
        if (dungeon.isWalkable(nx, ny) && !dungeon.hasEnemyAt(nx, ny)) {
            setPosition(nx, ny);
            return;
        }
        
        // If can't move directly towards player, try alternative moves
        Direction[] dirs = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        for (Direction dir : dirs) {
            nx = x + dir.dx;
            ny = y + dir.dy;
            if (dungeon.isWalkable(nx, ny) && !dungeon.hasEnemyAt(nx, ny)) {
                setPosition(nx, ny);
                return;
            }
        }
    }
}

public class TerminalDungeonGame {
    
    private static void clearScreen(){
        try {
            String os= System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else{
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} //†"