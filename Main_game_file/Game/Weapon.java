package Game;

public class Weapon extends Items {
    private int damage;

    public Weapon(String type, String weaponID, int quantity, int damage) {
        super(type, quantity, weaponID);
        this.damage = damage;
    }

    public int getDamage() { return damage; }

    @Override
    public String toString() {
        return "type: " + getType() + ", damage: " + getDamage() + ", quantity: " + getQuantity();
    }
}
