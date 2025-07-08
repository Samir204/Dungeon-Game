package Game;
public class OnHandItem {
    public Items onLeftHand;
    public Items onRightHand;

    public OnHandItem(){
        onLeftHand=null;
        onRightHand=null;
    }

    public Items seeItemOnLeft(){ return this.onLeftHand;}
    public Items seeItemOnRight(){ return this.onRightHand;}

    public boolean putItemOnLeft(Items item){
        if (onLeftHand==null) {
            this.onLeftHand=item;
            return true;
        }
        return false;
    }
    public boolean putItemOnRight(Items items){
        if (onRightHand==null) {
            this.onRightHand=items;
            return true;
        }
        return false;
    }

    public Items removeItemFromLeft(){
        Items item= onLeftHand;
        onLeftHand=null;
        return item;
    }
    public Items removeItemFromRight(){
       Items item=onRightHand;
       onRightHand=null;
       return item;
    }

}
