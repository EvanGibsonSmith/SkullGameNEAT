package src.game_objects;

import java.util.Stack;

import java.util.HashSet;
import java.util.Set;

public abstract class RevealingRoundPlayer {
    Hand hand;
    int points = 0; // TODO should this be in the larger player class?
    Stack<Card> playedCards;
    String name;

    public RevealingRoundPlayer(Hand hand, Stack<Card> playedCards, String name) {
        this.hand = hand;
        this.playedCards = playedCards;
        this.name = name;
    }
    
    public Hand getHand() {return this.hand;}

    public int getPoints() {return this.points;}
    
    public Stack<Card> getPlayedCards() {return this.playedCards;}

    public void incrementPoints() {
        if (points<2) {++points;}
    }

    public void resetPoints() {
        this.points = 0;
    }

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

    public abstract String decide(Player[] players);
}
