package src.game_objects;

public class NEATPlayer extends Player  {
    int genomeID = -1;

    public NEATPlayer(String name, int genomeID) {
        this(name, new Hand(), genomeID);     
    }

    public NEATPlayer(String name, Hand hand, int genomeID) {
        super(name, hand);
        this.genomeID = genomeID;
        setupRoundPlayers();
    }

    public void setupRoundPlayers() {
        this.placingRoundPlayer = new PlacingRoundPlayerNEAT(hand, playedCards, name, genomeID);
        this.bettingRoundPlayer = new BettingRoundPlayerNEAT(name, genomeID);
        this.revealingRoundPlayer = new RevealingRoundPlayerNEAT(hand, playedCards, name, genomeID);
        this.removeRoundPlayer = new RemoveRoundPlayerNEAT(hand, name, genomeID);
    }

    public int getGenomeID() {
        return genomeID;
    }
}
