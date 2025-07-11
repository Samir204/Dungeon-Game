package Main_game_file;
// import java.util.*;


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
        
        // move towards player
        int nx = x + dx;
        int ny = y + dy;
        if (dungeon.isWalkable(nx, ny) && !dungeon.hasEnemyAt(nx, ny)) {
            setPosition(nx, ny);
            return;
        }
        
        // if cant move directly towards player try alternative moves
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

class BossEnemy extends Enemy {
    private int health;
    private int maxHealth;
    private int attackCooldown;
    private boolean isAttacking; // true if attacking, else false 

    public BossEnemy(int x, int y){
        super(x, y);
        this.maxHealth=100;
        this.health=maxHealth;
        this.attackCooldown=0;
        this.isAttacking=false;
    }
    public char getSymbol(){ return 'B'; }
    public int getHealth(){ return health; }
    public int getMaxHealth(){ return maxHealth; }
    public boolean isAttacking(){ return isAttacking; }

    public void takeDamage(int damage){
        health-=damage;
        if (health<0) {
            health=0;
        }
    }
    public boolean isDead(){
        return health<=0;
    }
    public void takeTurn(Player player, DungeonMap dungeon){
        if (attackCooldown>0) {
            attackCooldown--;
        }
        int playerX= player.getX();
        int playerY= player.getY();
        int distance= Math.abs(playerX -x)+ Math.abs(playerY-y);
        if (distance<=1 && attackCooldown==0) {
            isAttacking=true;
            attackCooldown=3;
            return;
        }
        else{
            isAttacking=false;
        }
        int dx= Integer.compare(playerX, x);
        int dy= Integer.compare(playerY, y);

        int nx= x+dx;
        int ny=y+dy; 
        if (dungeon.isWalkable(nx, ny) && !dungeon.hasEnemyAt(nx, ny)) { //try and move to player
            setPosition(nx, ny);
            return;
        }

        if (dx!=0 && dy!=0) {
            nx=x+dx;
            ny=y;
            if (dungeon.isWalkable(nx, ny) && !dungeon.hasEnemyAt(nx, ny)) { // if i cant then move diagonal
                setPosition(nx, ny);
                return;
            }
            nx=x;
            ny=y+dy;
            if (dungeon.isWalkable(nx, ny) && !dungeon.hasEnemyAt(nx, ny)) {
                setPosition(nx, ny);
                return;
            }
        }

        Direction[] dir= {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT}; 
        for (Direction direction : dir) {
            nx=x+direction.dx;
            ny=y+direction.dy;
            if (dungeon.isWalkable(nx, ny) && !dungeon.hasEnemyAt(nx, ny)) {
                setPosition(nx, ny);
                return;
            }
        }
    }
}
//†"