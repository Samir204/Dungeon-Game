package Game;

public class BowAndarow extends Items{
    public int damage;
    public int numOfArows;


    public BowAndarow(String type, String barID, int quantity, int damage, int numOfArows){
        super(type, quantity, barID);
        this.damage=damage;
        this.numOfArows=numOfArows;
    }

    public int getDamage(){ return this.damage; }
    public int getNumOfArows(){ return this.numOfArows; }

    public void addArows(int num){
        numOfArows+=num;
    }

    @Override
    public String toString(){
        return "type: " + getType() 
            + ", damage: " + getDamage() 
            + ", quantity: " + getQuantity()
            +", Number of arows: " + getNumOfArows();
    }
}
