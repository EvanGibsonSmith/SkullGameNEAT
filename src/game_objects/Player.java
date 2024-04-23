package src.game_objects;

import java.util.Stack;

public class Player {
    String name;
    Hand hand; 
    Stack<Card> playedCards = new Stack<>();
    PlacingRoundPlayer placingRoundPlayer;
    BettingRoundPlayer bettingRoundPlayer;
    RevealingRoundPlayer revealingRoundPlayer;
    RemoveRoundPlayer removeRoundPlayer;

    public Player() {
        this("Default", new Hand()); // initializes player with new full hand/default name
    }

    public Player(Hand hand) {
        this("Default", hand);
    }

    public Player(String name) {
        this(name, new Hand());
    }

    public Player(String name, Hand hand) {
        this.name = name;
        this.hand = hand;
        this.placingRoundPlayer = new PlacingRoundPlayerTerminal(hand, playedCards);
        this.bettingRoundPlayer = new BettingRoundPlayerTerminal();
        this.revealingRoundPlayer = new RevealingRoundPlayerTerminal(hand, playedCards);
        this.removeRoundPlayer = new RemoveRoundPlayerTerminal(hand);
    }

    
    public Player(String name, Hand hand, String type) {
        this.name = name;
        this.hand = hand;
        // set type of internal players
        if (type=="terminal") {
            this.placingRoundPlayer = new PlacingRoundPlayerTerminal(hand, playedCards);
            this.bettingRoundPlayer = new BettingRoundPlayerTerminal();
            this.revealingRoundPlayer = new RevealingRoundPlayerTerminal(hand, playedCards);
            this.removeRoundPlayer = new RemoveRoundPlayerTerminal(hand);
        }
        // TODO add NEAT type when created
    }

    public String getName() {
        return this.name;
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
