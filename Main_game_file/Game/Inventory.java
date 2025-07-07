package Game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    public int inventorySize;
    public int curentItemsCount;
    public Map<String,Items> inventory; // string-> itemID, Items -> items
    

    public Inventory(int inventorySize){
        this.inventorySize=inventorySize;
        this.curentItemsCount=0;
        this.inventory=new HashMap<>();
    }
    public int getInventorySize(){ return this.inventorySize; }
    public int getCurrentItemsCount(){ return this.curentItemsCount; }

    public List<Items> getItemInInventory(){ 
        List<Items> items= new ArrayList<>();
        for (Map.Entry<String,Items> entry : inventory.entrySet()) {
            if (entry.getValue()!=null) {
                items.add(entry.getValue());
            }
        }
        return items;
    }

    public Items getItemFromID(String id){
        return inventory.get(id);
    }

    public boolean hasItem(Items item){
        return inventory.containsValue(item);
    }

    public boolean hasItemByID(String itemID){
        return inventory.containsKey(itemID);
    }

    // public Items getItemFromID(String id){
    //     return inventory.get(id);
    // }

    // public boolean getItem(Items item){
    //     return inventory.containsValue(item);
    // }

    // public int getQuantityFromItem(Items item){
    //     return item.getQuantity();
    // }

    // public String getTypeFromItem(Items item){
    //     return item.getType();
    // }

    // public String getIDFromItem(Items item){
    //     return item.getItemID();
    // }
    
    public void addItem(Items item){
        if (item!=null && curentItemsCount<inventorySize) {
            if (inventory.containsKey(item.getItemID())) {
                Items existingItem= inventory.get(item.getItemID()); // item exist in inventory
                existingItem.setQuantity(existingItem.getQuantity()+ item.getQuantity());
            }
            else{
                inventory.put(item.getItemID(), item);
                curentItemsCount++;
            }
        }
        else{
            System.out.println("Cannot add item: inventory full or item is null");
        }
    }
    public boolean removeItem(String itemID){
        if (itemID!=null && inventory.containsKey(itemID)) {
            inventory.remove(itemID);
            curentItemsCount--;
            return true;
        }
        return false;
    }

    public boolean removeItem(Items item){
        if (item!=null && inventory.containsValue(item)) {
            inventory.remove(item.getItemID());
            curentItemsCount--;
            return true;
        }
        return false;
    }



}   
