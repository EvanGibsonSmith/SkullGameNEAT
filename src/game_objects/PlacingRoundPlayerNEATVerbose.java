package src.game_objects;

import java.util.Stack;

public class PlacingRoundPlayerNEATVerbose extends PlacingRoundPlayerNEAT {
    
    public PlacingRoundPlayerNEATVerbose(Hand hand, Stack<Card> playedCards, String name, int genomeID) {
        super(hand, playedCards, name, genomeID);
    }

    public PlacingRoundPlayerNEATVerbose(Hand hand, String name, int genomeID) {
        super(hand, name, genomeID);
    }

    @Override
    public int decide(Player[] players, boolean canBeginBetting) { 
        int decision = super.decide(players, canBeginBetting);
        if (decision==0 || decision==1) {
            System.out.println(name + " has placed a card.");
        }
        else {
            System.out.println(name + " has ended the placing round.");
        }
        return decision;
    }

}
