package src.game_objects;

import java.util.Stack;

import src.data_structures.MultiSet;

import java.util.HashSet;
import java.util.Set;

public abstract class PlacingRoundPlayer {
    Hand hand;
    Stack<Card> playedCards;
    String name;

    public PlacingRoundPlayer(Hand hand, Stack<Card> playedCards, String name) {
        this.hand = hand;
        this.playedCards = playedCards;
        this.name = name;
    }

    public PlacingRoundPlayer(Hand hand, String name) {
        this.hand = hand;
        this.name = name;
        playedCards = new Stack<>();
    }

    public Hand getHand() {
        return this.hand;
    }

    public int getStackSize() {return playedCards.size();}
    
    private Card popCard(boolean isSkull) {
        if (isSkull) {
            return hand.popSkull();
        }
        else {
            return hand.popFlower();
        }
    }
    
    /**
     * Returns a boolean array of the options that the play has for this round
     * at this moment in time. This could be playing a skull or flower
     * @return and array of true and false of length 2, representing the possible choices of the player,
     *         in the order of [skullBoolean, flowerBoolean]
     */
    public boolean[] options() {
        boolean[] optionsOutput = {false, false};
        if (hand.getMultiSet().contains(new Card("skull"))) {optionsOutput[0]=true;}
        if (hand.getMultiSet().contains(new Card("flower"))) {optionsOutput[1]=true;}

        return optionsOutput;
    }

    /**
     * Places the card based on if a skull or not. Does not catch exceptions.
     * 
     * @param isSkull if the card to place from the hand is a skull
     */
    public void placeCard(boolean isSkull) {
        Card toPlay = popCard(isSkull);
        playedCards.add(toPlay);
    }

    public void placeCard(int isSkull) {
        boolean isSkullBoolean = false;
        if (isSkull==0) {isSkullBoolean=true;}
        else if (isSkull==1) {isSkullBoolean=false;}
        else {
            throw new IllegalArgumentException("isSkull must be 0 or 1");
        }
        placeCard(isSkullBoolean);
    }

    // NOTE: Could remove if not super useful
    public Set<Integer> totalOptions() {
        Set<Integer> allOptions = new HashSet<>();
        allOptions.add(0);
        allOptions.add(1);
        allOptions.add(2);
        return allOptions;
    }

    // NOTE: potentially could rework so that card objects are in set, but likely clunkier
    public Set<Integer> validOptions(boolean canBeginBetting) { 
        Set<Integer> validOptions = new HashSet<>();
        MultiSet<Card> cards = hand.getMultiSet();
        if (cards.contains(new Card("skull"))) {validOptions.add(0);}
        if (cards.contains(new Card("flower"))) {validOptions.add(1);}
        if (canBeginBetting) {validOptions.add(2);}
        return validOptions;
    }

    public abstract int decide(Player[] players, boolean canBeginBetting);
}
