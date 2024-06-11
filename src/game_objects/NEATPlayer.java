package src.game_objects;

public class NEATPlayer extends Player  {
    int genomeID = -1;
    double fitnessMultiplier = 100;

    public NEATPlayer(String name, int genomeID) {
        this(name, new Hand(), genomeID);     
    }

    public NEATPlayer(String name, int genomeID, double fitnessMultiplier) {
        this(name, new Hand(), genomeID);     
        this.fitnessMultiplier = fitnessMultiplier;
    }

    public NEATPlayer(String name, Hand hand, int genomeID) {
        super(name, hand);
        this.genomeID = genomeID;
        setupRoundPlayers();
    }
    
    @Override
    public void resetCards() {
        super.resetCards(); // reset cards, but also internal HashMaps for each player
        ((PlacingRoundPlayerNEAT) this.placingRoundPlayer).reset();
        ((BettingRoundPlayerNEAT) this.bettingRoundPlayer).reset();
        ((RevealingRoundPlayerNEAT) this.revealingRoundPlayer).reset();
        ((RemoveRoundPlayerNEAT) this.removeRoundPlayer).reset();
    }

    @Override
    protected void setupRoundPlayers() {
        this.placingRoundPlayer = new PlacingRoundPlayerNEAT(hand, playedCards, name, genomeID);
        this.bettingRoundPlayer = new BettingRoundPlayerNEAT(name, genomeID);
        this.revealingRoundPlayer = new RevealingRoundPlayerNEAT(hand, playedCards, name, genomeID);
        this.removeRoundPlayer = new RemoveRoundPlayerNEAT(hand, name, genomeID);
    }

    public double getFitness() {
        double fitnessTotal = fitnessMultiplier * wonGames; // get score for winning games, then subtract mistake outputs off
        fitnessTotal += ((PlacingRoundPlayerNEAT) placingRoundPlayer).getFitness(); // these fitnesses are negative, so adding them is punishment
        fitnessTotal += ((BettingRoundPlayerNEAT) bettingRoundPlayer).getFitness();
        fitnessTotal += ((RevealingRoundPlayerNEAT) revealingRoundPlayer).getFitness();
        fitnessTotal += ((RemoveRoundPlayerNEAT) removeRoundPlayer).getFitness();
        return fitnessTotal;
    }

    public int getGenomeID() {
        return genomeID;
    }
}
