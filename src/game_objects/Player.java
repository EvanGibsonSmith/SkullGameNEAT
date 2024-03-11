package src.game_objects;

import java.util.Stack;

public class Player {
    Hand hand = new Hand(); 
    Stack<Card> playedCards = new Stack<>();
    PlacingRoundPlayer placingRoundPlayer;
    BettingRoundPlayer bettingRoundPlayer;
    RevealingRoundPlayer revealingRoundPlayer;
    RemoveRoundPlayer removeRoundPlayer;

    public Player() {
        this.placingRoundPlayer = new PlacingRoundPlayer(hand, playedCards);
        this.bettingRoundPlayer = new BettingRoundPlayer();
        this.revealingRoundPlayer = new RevealingRoundPlayer(hand, playedCards);
        this.removeRoundPlayer = new RemoveRoundPlayer(hand);
    }

    public Player(Hand hand) {
        this.hand = hand;
        this.placingRoundPlayer = new PlacingRoundPlayer(hand, playedCards);
        this.bettingRoundPlayer = new BettingRoundPlayer();
        this.revealingRoundPlayer = new RevealingRoundPlayer(hand, playedCards);
        this.removeRoundPlayer = new RemoveRoundPlayer(hand);
    }

    public PlacingRoundPlayer getPlacingRoundPlayer() {
        return this.placingRoundPlayer;
    }

    public BettingRoundPlayer getBettingRoundPlayer() {
        return this.bettingRoundPlayer;
    }

    public RevealingRoundPlayer getRevealingRoundPlayer() {
        return this.revealingRoundPlayer;
    }

    public RemoveRoundPlayer getRemoveRoundPlayer() {
        return this.removeRoundPlayer;
    }
    
    public int getPoints() {
        return this.getRevealingRoundPlayer().getPoints();
    }

    public Hand getHand() {
        return this.hand;
    }

    public Stack<Card> getPlayedCards() {
        return this.playedCards;
    }
  
}
