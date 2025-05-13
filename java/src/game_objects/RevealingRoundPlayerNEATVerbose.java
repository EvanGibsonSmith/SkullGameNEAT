package src.game_objects;

import java.util.Stack;

public class RevealingRoundPlayerNEATVerbose extends RevealingRoundPlayerNEAT {
    
    public RevealingRoundPlayerNEATVerbose(Hand hand, Stack<Card> playedCards, String name, int genomeID) {
        super(hand, playedCards, name, genomeID);
    }

    @Override
    public String decide(Player[] players) {
        String decision = super.decide(players);
        System.out.println(name + " has chosen to flip the top card of " + decision);
        return decision;
    }

}
