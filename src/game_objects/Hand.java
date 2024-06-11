package src.game_objects;

import src.data_structures.MultiSet;

public class Hand {
    MultiSet<Card> hand;

    public Hand() {
        build();
    }

    public void build() {
        hand = new MultiSet<>();
        for (int i=0; i<3; ++i) {
            hand.add(new Card("flower"));
        }
        hand.add(new Card("skull"));
    }

    // TODO potential leak here messing with internals? probably fine
    public MultiSet<Card> getMultiSet() {return this.hand;}

    @Override
    public String toString() {
        return hand.toString();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        // TODO add exception checking?
        hand.remove(card);
    }

    public Card popSkull() {
        return hand.remove(new Card("skull")); // using equals method removes skullCard like one passed
    }

    public Card popFlower() {
        return hand.remove(new Card("flower"));
    }

    public int size() {
        return hand.size();
    }

    public boolean empty() {
        return size()==0;
    }
    
    /**
     * Empties hand of all cards, useful for testing.
     */
    public void clearCards() {
        hand = new MultiSet<>();
    }
}