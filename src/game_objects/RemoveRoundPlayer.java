package src.game_objects;

import java.util.HashSet;
import java.util.Set;

import src.data_structures.MultiSet;

public abstract class RemoveRoundPlayer {   
    Hand hand;
    String name;

    // TODO for all of these classes, can add equality function dictating in code that the same names within a game a the same players
    public RemoveRoundPlayer(Hand hand, String name) {
        this.hand = hand;
        this.name = name;
    }

    public Hand getHand() {return this.hand;}

    public Set<Integer> validOptions() {
        Set<Integer> options = new HashSet<>();
        MultiSet<Card> cards = hand.getMultiSet();
        if (cards.contains(new Card("skull"))) {options.add(0);}
        if (cards.contains(new Card("flower"))) {options.add(1);}
        return options;
    }

    public void removeCard(Player[] players) {
        int cardRemoveType = decide(players);
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

    public abstract int decide(Player[] players);
    
}

