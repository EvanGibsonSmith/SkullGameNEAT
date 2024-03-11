package src.game_objects;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import src.data_structures.MultiSet;

public class RemoveRoundPlayer {   
    Hand hand;

    public RemoveRoundPlayer(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {return this.hand;}

    // TODO create removal round player as well, so each player has one validOptions and decide function.
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
                removalCard = new Card("skull"); // TODO instead of skull and flower card just make a card with field of either skull or flower since there are no methods to differentiate the two
                break;
            case 1:
                removalCard = new Card("flower");
                break;

            default:
                break;
        }
        hand.removeCard(removalCard);
    }

    public int decide() {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please input type of card to remove. 0 for skull, 1 for flower.");
        Set<Integer> validOptions = validOptions();
        int decision = scnr.nextInt();
        while (!validOptions.contains(decision)) {
            System.out.println("That is not a valid option, please enter another option");
            decision = scnr.nextInt();
        }
        //scnr.close(); TODO not closing right now because System.in needed later
        return decision;
    }
    
}

