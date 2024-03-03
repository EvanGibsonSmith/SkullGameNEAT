
public class SkullCard implements Card {
    
    public boolean equals(Object other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }
        return true; // all skullCards are the same 
    }
}
