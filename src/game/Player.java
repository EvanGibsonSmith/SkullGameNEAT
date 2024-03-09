import java.util.Stack;

public class Player {
    Hand hand = new Hand(); 
    Stack<Card> playedCards = new Stack<>();
    PlacingRoundPlayer placingRoundPlayers;
    BettingRoundPlayer bettingRoundPlayers;
    RevealingRoundPlayer revealingRoundPlayers;

    public Player() {
        this.placingRoundPlayers = new PlacingRoundPlayer(hand);
        this.bettingRoundPlayers = new BettingRoundPlayer();
        this.revealingRoundPlayers = new RevealingRoundPlayer(hand, playedCards);
    }

    public Player(Hand hand) {
        this.hand = hand;
        this.placingRoundPlayers = new PlacingRoundPlayer(hand);
        this.bettingRoundPlayers = new BettingRoundPlayer();
        this.revealingRoundPlayers = new RevealingRoundPlayer(hand, playedCards);
    }

    public PlacingRoundPlayer getPlacingRoundPlayer() {
        return this.placingRoundPlayers;
    }

    public BettingRoundPlayer getBettingRoundPlayer() {
        return this.bettingRoundPlayers;
    }

    public RevealingRoundPlayer getRevealingRoundPlayer() {
        return this.revealingRoundPlayers;
    }

    public Hand getHand() {
        return this.hand;
    }

    public Stack<Card> getPlayedCards() {
        return this.playedCards;
    }
  
}
