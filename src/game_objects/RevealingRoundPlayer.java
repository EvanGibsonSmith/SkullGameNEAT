package src.game_objects;

import java.util.Stack;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

// TODO make simple interface for all Round types since they all have decide and queryPlayer? However, return types are different.
// adding to this, these classes should probably be mostly used within player, each of them being an abstract class with their own 
// decide method unimplemented for two extending classes (AI vs TerminalPlayer) to extend.
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
    public Set<String> validOptions(Player[] players) {
        Set<String> options = new HashSet<>();
        for (Player p: players) {
            // note that own cards will be empty after automatically flipped, so no edge case needed
            if (!p.getPlayedCards().isEmpty()) {
                options.add(p.getName());
            }
        }
        return options;
    }

    public String decide(Player[] players) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please input the name of the player you would like to pick.");
        Set<String> validOptions = validOptions(players);
        String decision = scnr.nextLine();
        while (!validOptions.contains(decision)) {
            System.out.println("That is not a valid option, please enter another option");
            decision = scnr.nextLine();
        }
        //scnr.close(); TODO not closing right now (which isn't great) so user can continue to give inputs
        return decision;
    }

    /**
     * Flips the top card of this player, used to reveal to other players..
     * @return true if top card skull, false if flower.
     */
    public boolean flip() {
        Card flippedCard = playedCards.pop();
        hand.addCard(flippedCard); // return card to the proper hand of the player
        return flippedCard.equals(new Card("skull"));
    }

    public void returnCards() {
        while (!playedCards.empty()){
            hand.addCard(playedCards.pop());
        }
    }
}
