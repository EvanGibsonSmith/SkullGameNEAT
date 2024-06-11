package src.game_objects;

public class TerminalPlayer extends Player {
    
    public TerminalPlayer() {
        super();
    }
    
    public TerminalPlayer(Hand hand) {
        super(hand);
    }

    public TerminalPlayer(String name) {
        super(name, new Hand());
    }

    public TerminalPlayer(String name, Hand hand) {
        super(name, hand);
    }

    public void setupRoundPlayers() {
        placingRoundPlayer = new PlacingRoundPlayerTerminal(hand, playedCards, name);
        bettingRoundPlayer = new BettingRoundPlayerTerminal(name);
        revealingRoundPlayer = new RevealingRoundPlayerTerminal(hand, playedCards, name);
        removeRoundPlayer = new RemoveRoundPlayerTerminal(hand, name);
    }
}
