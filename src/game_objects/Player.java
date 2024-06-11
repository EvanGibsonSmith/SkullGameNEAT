package src.game_objects;

import java.util.Stack;

public abstract class Player {
    String name;
    Hand hand; 
    Stack<Card> playedCards = new Stack<>();
    PlacingRoundPlayer placingRoundPlayer;
    BettingRoundPlayer bettingRoundPlayer;
    RevealingRoundPlayer revealingRoundPlayer;
    RemoveRoundPlayer removeRoundPlayer;
    int wonGames = 0; // number of games won
    int gamesPlayed = 0;

    public Player() {
        this("Default", new Hand()); // initializes player with new full hand/default name
    }

    public Player(Hand hand) {
        this("Default", hand);
    }

    public Player(String name) {
        this(name, new Hand());
        
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getWonGames() {
        return wonGames;
    }

    public void incrementWonGames() {
        ++wonGames;
    }

    public void incrementGamesPlayed() {
        ++gamesPlayed;
    }

    /**
     * Sets player up to play another
     */
    public void resetCards() {
        this.hand.build(); // new hand
        this.playedCards.clear(); // clear stack
        this.getRevealingRoundPlayer().resetPoints(); // reset points back to 0 for new game
        this.getBettingRoundPlayer().resetBet(); // reset bet if somehow bet was not 0
    }

    public Player(String name, Hand hand) {
        this.name = name;
        this.hand = hand;
        setupRoundPlayers();
    }

    protected abstract void setupRoundPlayers();

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) { // player names must be equal to be equal players
        if (!(obj instanceof Player)) {
            return false;
        }
        return ((Player) obj).getName().equals(this.getName());
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
