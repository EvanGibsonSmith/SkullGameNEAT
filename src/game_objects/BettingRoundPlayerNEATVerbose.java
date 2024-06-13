package src.game_objects;

public class BettingRoundPlayerNEATVerbose extends BettingRoundPlayerNEAT {
    
    public BettingRoundPlayerNEATVerbose(String name, int genomeID) {
        super(name, genomeID);
    }

    @Override
    public boolean decide(Player[] players, boolean beganBetting, int currentBet, int maxBet) {
        boolean decision = super.decide(players, beganBetting, currentBet, maxBet);
        if (decision==false) {
            System.out.println(name + " has ended the betting round.");
        }
        else {
            System.out.println(name + " has raised the bet to " + bet);
        }
        return decision;
    }

}
