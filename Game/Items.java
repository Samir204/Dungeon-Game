package Game;

public class Items {
    protected String type;
    protected String itemID;
    private int quantity;

    public Items(String type, int quantity, String itemID){
        this.type = type;
        this.quantity = quantity;
        this.itemID = itemID;
    }

    public String getType() { return type; }
    public String getItemID() { return itemID; }
    public int getQuantity() { return quantity; }

    public void reduceQuantity() {
        if (this.quantity > 0) {
            this.quantity--;
        }
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "item ID: " + getItemID() + ", quantity: " + getQuantity();
    }
}


