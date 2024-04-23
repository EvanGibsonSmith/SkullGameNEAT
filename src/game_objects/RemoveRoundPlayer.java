package src.game_objects;

import java.util.HashSet;
import java.util.Set;

import src.data_structures.MultiSet;

public abstract class RemoveRoundPlayer {   
    Hand hand;

    public RemoveRoundPlayer(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {return this.hand;}

    public Set<Integer> validOptions() {
        Set<Integer> options = new HashSet<>();
        MultiSet<Card> cards = hand.getMultiSet();
        if (cards.contains(new Card("skull"))) {options.add(0);}
        if (cards.contains(new Card("flower"))) {options.add(1);}
        return options;
    }

    public void removeCard() {
        int cardRemoveType = decide();
        Card removalCard = null;
        switch (cardRemoveType) {
            case 0:
                removalCard = new Card("skull");
                break;
            case 1:
                removalCard = new Card("flower");
                break;

            default:
                break;
        }
        hand.removeCard(removalCard);
    }

    public abstract int decide();
    
}

