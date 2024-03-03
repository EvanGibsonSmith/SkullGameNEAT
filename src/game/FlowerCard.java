
public class FlowerCard implements Card{
    
    public boolean equals(Object other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }
        return true; // all flowerCards are the same 
    }
}
