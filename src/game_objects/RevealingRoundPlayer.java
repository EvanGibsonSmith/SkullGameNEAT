package src.game_objects;

import java.util.Stack;

import src.data_structures.MultiSet;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

// TODO make simple interface for all Round types since they all have decide and queryPlayer? However, return types are different.
public class RevealingRoundPlayer {
    Hand hand;
    int points = 0; // TODO make these private instead of protected? (goes for many of these classes)
    Stack<Card> playedCards;

    public RevealingRoundPlayer(Hand hand, Stack<Card> playedCards) {
        this.hand = hand;
        this.playedCards = playedCards;
    }
    
    public Hand getHand() {return this.hand;}

    public int getPoints() {return this.points;}
    
    public Stack<Card> getPlayedCards() {return this.playedCards;}

    public void incrementPoints() {
        if (points<2) {++points;}
    }

    // TODO: Maybe too much info passing all of the players stuff? Additionally, needing other player info here breaks pattern of other player types
    public Set<Integer> validFlipOptions(RevealingRoundPlayer[] players) {
        Set<Integer> options = new HashSet<>();
        for (int i=0; i<players.length; ++i) {
            RevealingRoundPlayer p = players[i];
            // note that own cards will be empty after automatically flipped, so no edge case needed
            if (!p.getPlayedCards().isEmpty()) {
                options.add(i);
            }
        }
        return options;
    }

    // NOTE: decideFlip and decideRemoveCard are only used in the round if this is the player doing the revealing (TODO could potentially be broken up more? it is reasonably simple as is)
    public int decideFlip(RevealingRoundPlayer[] players) { // TODO implemented by different strategies? make interface later?
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please input the index of the player you would like to pick.");
        Set<Integer> validOptions = validFlipOptions(players);
        int decision = scnr.nextInt();
        while (!validOptions.contains(decision)) {
            System.out.println("That is not a valid option, please enter another option");
            decision = scnr.nextInt();
        }
        // TODO catch incorrect outputs
        //scnr.close(); TODO not closing right now (which isn't great) so user can continue to give inputs
        return decision;
    }

    // TODO create removal round player as well, so each player has one validOptions and decide function.
    public Set<Integer> validRemovalOptions() {
        Set<Integer> options = new HashSet<>();
        MultiSet<Card> cards = hand.getMultiSet();
        if (cards.contains(new Card("skull"))) {options.add(0);}
        if (cards.contains(new Card("flower"))) {options.add(1);}
        return options;
    }

    public int decideRemoveCard() {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please input type of card to remove. 0 for skull, 1 for flower.");
        Set<Integer> validOptions = validRemovalOptions();
        int decision = scnr.nextInt();
        while (!validOptions.contains(decision)) {
            System.out.println("That is not a valid option, please enter another option");
            decision = scnr.nextInt();
        }
        //scnr.close(); TODO not closing right now because System.in needed later
        return decision;
    }

    // TODO document (boolean is if skull, flips top of stack)
    public boolean flip() {
        Card flippedCard = playedCards.pop();
        hand.addCard(flippedCard); // return card to the proper hand of the player
        return flippedCard.equals(new Card("skull")); // TODO change this? a bit jank at the moment
    }

    public void returnCards() {
        while (!playedCards.empty()){
            hand.addCard(playedCards.pop());
        }
    }
}
