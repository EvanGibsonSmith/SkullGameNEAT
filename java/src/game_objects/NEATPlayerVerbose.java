package src.game_objects;

public class NEATPlayerVerbose extends NEATPlayer {

    public NEATPlayerVerbose(String name, int genomeID) {
        this(name, new Hand(), genomeID);     
    }

    public NEATPlayerVerbose(String name, int genomeID, double fitnessMultiplier) {
        this(name, new Hand(), genomeID);     
    }

    public NEATPlayerVerbose(String name, Hand hand, int genomeID) {
        super(name, hand, genomeID);
    }

    @Override
    protected void setupRoundPlayers() {
        this.placingRoundPlayer = new PlacingRoundPlayerNEATVerbose(hand, playedCards, name, genomeID);
        this.bettingRoundPlayer = new BettingRoundPlayerNEATVerbose(name, genomeID);
        this.revealingRoundPlayer = new RevealingRoundPlayerNEATVerbose(hand, playedCards, name, genomeID);
        this.removeRoundPlayer = new RemoveRoundPlayerNEATVerbose(hand, name, genomeID);
    }
}
