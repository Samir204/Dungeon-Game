package Main_game_file;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.List; // This is the correct import - remove the java.awt.List import

class DungeonMap {
    private char[][] map;
    private final Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private int doorX = -1, doorY = -1;

    public DungeonMap(int width, int height, Player player) {
        this.map = generateRandomMap(width, height);
        this.player = player;
        // Ensure player starts in a walkable position
        placePlayerInWalkablePosition();
        // Place a door randomly
        placeDoor();
    }

    public void addEnemy(Enemy e) { enemies.add(e); }

    public boolean isWalkable(int x, int y) {
        return x >= 0 && y >= 0 && x < map.length && y < map[0].length && map[x][y] != '#';
    }

    public boolean hasEnemyAt(int x, int y) {
        for (Enemy e : enemies) {
            if (e.getX() == x && e.getY() == y) return true;
        }
        return false;
    }

    public boolean isAtDoor(int x, int y) {
        return x == doorX && y == doorY; 
    }
    
    public Player getPlayer() {
        return player;
    }

    public void updateEnemies() {
        for (Enemy e : enemies) {
            e.takeTurn(player, this);
            if (isNextTo(e, player)) player.setDead(true);
        }
    }

    private boolean isNextTo(Entity a, Entity b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        return (dx + dy) == 1;
    }

    private void placePlayerInWalkablePosition() {
        // Find the first walkable position and place the player there
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == '.') {
                    player.setPosition(x, y);
                    return;
                }
            }
        }
    }

    private void placeDoor() {
        // Use flood fill to find all reachable positions from player
        Set<String> reachable = getReachablePositionsInternal(player.getX(), player.getY());
        
        // Place door in a random reachable position (not too close to player)
        Random random = new Random();
        List<int[]> validPositions = new ArrayList<>();
        
        for (String pos : reachable) {
            String[] coords = pos.split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            
            // Make sure door is not too close to player (at least 5 tiles away)
            int distance = Math.abs(x - player.getX()) + Math.abs(y - player.getY());
            if (distance > 5) {
                validPositions.add(new int[]{x, y});
            }
        }
        
        if (!validPositions.isEmpty()) {
            int[] doorPos = validPositions.get(random.nextInt(validPositions.size()));
            doorX = doorPos[0];
            doorY = doorPos[1];
        } else {
            // Fallback: place door at any reachable position
            if (!reachable.isEmpty()) {
                String pos = reachable.iterator().next();
                String[] coords = pos.split(",");
                doorX = Integer.parseInt(coords[0]);
                doorY = Integer.parseInt(coords[1]);
            }
        }
    }
    
    public Set<String> getReachablePositionsInternal(int startX, int startY) {
        Set<String> visited = new HashSet<>();
        Queue<int[]> queue = new LinkedList<>();
        
        queue.offer(new int[]{startX, startY});
        visited.add(startX + "," + startY);
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            
            // Check all 4 directions
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                String pos = nx + "," + ny;
                
                if (isWalkable(nx, ny) && !visited.contains(pos)) {
                    visited.add(pos);
                    queue.offer(new int[]{nx, ny});
                }
            }
        }
        
        return visited;
    }
    
    public Set<String> getReachablePositions(int startX, int startY) {
        return getReachablePositionsInternal(startX, startY);
    }

    public void render() {
        char[][] display = new char[map.length][map[0].length];
        for (int i = 0; i < map.length; i++)
            System.arraycopy(map[i], 0, display[i], 0, map[i].length);

        // Place door
        if (doorX != -1 && doorY != -1) {
            display[doorX][doorY] = '+';
        }

        display[player.getX()][player.getY()] = player.getSymbol();
        for (Enemy e : enemies)
            display[e.getX()][e.getY()] = e.getSymbol();

        for (char[] row : display) {
            for (char c : row) System.out.print(c);
            System.out.println();
        }
    }

    private char[][] generateRandomMap(int width, int height) {
        char[][] newMap = new char[height][width];
        Random random = new Random();
        
        // Initialize with all walls
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newMap[i][j] = '#';
            }
        }

        // Create random rooms and store their centers for connection
        int numRooms = 5 + random.nextInt(6); // between 5 and 10
        List<int[]> roomCenters = new ArrayList<>();
        
        for (int r = 0; r < numRooms; r++) {
            int rw = 4 + random.nextInt(4); // room width
            int rh = 4 + random.nextInt(4); // room height
            int rx = 1 + random.nextInt(width - rw - 2);
            int ry = 1 + random.nextInt(height - rh - 2);

            // Create the room
            for (int y = ry; y < ry + rh; y++) {
                for (int x = rx; x < rx + rw; x++) {
                    newMap[y][x] = '.';
                }
            }
            
            // Store room center for corridor connection
            roomCenters.add(new int[]{ry + rh/2, rx + rw/2});
        }

        // Connect all rooms with corridors to ensure connectivity
        for (int i = 0; i < roomCenters.size() - 1; i++) {
            int[] room1 = roomCenters.get(i);
            int[] room2 = roomCenters.get(i + 1);
            
            createCorridor(newMap, room1[0], room1[1], room2[0], room2[1]);
        }
        
        // Add some additional random connections for more interesting layout
        for (int i = 0; i < 3 && roomCenters.size() > 2; i++) {
            int idx1 = random.nextInt(roomCenters.size());
            int idx2 = random.nextInt(roomCenters.size());
            if (idx1 != idx2) {
                int[] room1 = roomCenters.get(idx1);
                int[] room2 = roomCenters.get(idx2);
                createCorridor(newMap, room1[0], room1[1], room2[0], room2[1]);
            }
        }

        return newMap;
    }
    
    private void createCorridor(char[][] map, int y1, int x1, int y2, int x2) {
        // Create L-shaped corridor between two points
        // First go horizontally, then vertically
        
        int startX = Math.min(x1, x2);
        int endX = Math.max(x1, x2);
        int startY = Math.min(y1, y2);
        int endY = Math.max(y1, y2);
        
        // Horizontal corridor
        for (int x = startX; x <= endX; x++) {
            if (y1 >= 0 && y1 < map.length && x >= 0 && x < map[0].length) {
                map[y1][x] = '.';
            }
        }
        
        // Vertical corridor
        for (int y = startY; y <= endY; y++) {
            if (y >= 0 && y < map.length && x2 >= 0 && x2 < map[0].length) {
                map[y][x2] = '.';
            }
        }
    }

    public char[][] renderToCharArray() {
        char[][] display = new char[map.length][map[0].length];
        
        // Copy the base map
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(map[i], 0, display[i], 0, map[i].length);
        }

        // Place door
        if (doorX != -1 && doorY != -1) {
            display[doorX][doorY] = '+';
        }

        // Place player
        display[player.getX()][player.getY()] = player.getSymbol();
        
        // Place enemies
        for (Enemy e : enemies) {
            display[e.getX()][e.getY()] = e.getSymbol();
        }

        return display;
    }

    // Also add this method to get map dimensions
    public int getWidth() {
        return map[0].length;
    }

    public int getHeight() {
        return map.length;
    }

    public Enemy getEnemyAt(int checkX, int checkY) {
        for (Enemy enemy : enemies) {
            if (enemy.getX()==checkX && enemy.getY()==checkY) {
                return enemy;
            }
        }
        return null;
    }

    public void removeEnemy(Enemy enemy){
        enemies.remove(enemy);
    }
}