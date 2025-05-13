package src.game_objects;

public class Card {
    String type;

    // TODO add check for if it is within valid types (skull or flower). It makes the rest of this quite messy...
    public Card(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }
        return this.type==((Card) other).type; // all skullCards are the same 
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return this.type;
    }

}
