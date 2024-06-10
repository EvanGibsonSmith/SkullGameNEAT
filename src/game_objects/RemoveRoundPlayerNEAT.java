package src.game_objects;

import java.util.HashMap;
import java.util.Set;

import src.AI.NEATFunctions;

public class RemoveRoundPlayerNEAT extends RemoveRoundPlayer {
    int genomeID = -1;
    double fitness = 0.0;
    HashMap<Integer, Integer> outputIndexes = new HashMap<>();

    // TODO have this take in just played like the other rounds
    public RemoveRoundPlayerNEAT(Hand hand, String name, int genomeID) {
        super(hand, name);
        this.genomeID = genomeID;
    }

    // TODO could potentially reorganize so whole HashMap is built somewhere rather than in chunks?
    public void buildHashMap(Player[] players) { // just uses the last 2 nodes
        int n = players.length;
        if (!outputIndexes.isEmpty()) { // don't built if already built
            return;
        }
        // last two nodes represent removing skull (0) and flower (1)
        outputIndexes.put(3+1+(n*4)+(n-1), 0);
        outputIndexes.put(3+1+(n*4)+(n-1)+1, 1);
    }

    @Override
    // TODO document
    public int decide(Player[] players) {
        buildHashMap(players); // only build it once, but players are needed for nodes
        Double[] neatResults = NEATFunctions.getNEATOutput(players, name, 3, genomeID); // 3 is RemoveRound phase
        Set<Integer> choices = validOptions();
        fitness += NEATFunctions.badSelectionFitness(neatResults, outputIndexes, choices); // based on error, update fitness from invalid choices
        return NEATFunctions.selectBestValidOptions(neatResults, outputIndexes, choices); // actually make decision based on which are valid
    }

    public static void main(String[] args) {
        // dummy players    
        Player[] players = new Player[4];
        players[0] = new NEATPlayer("Player 0",46);
        players[1] = new TerminalPlayer("Player 1");
        players[2] = new TerminalPlayer("Player 2");
        players[3] = new TerminalPlayer("Player 3");

        players[0].getPlacingRoundPlayer().placeCard(false);
        players[1].getPlacingRoundPlayer().placeCard(false);
        players[2].getPlacingRoundPlayer().placeCard(true);
        players[3].getPlacingRoundPlayer().placeCard(true);

        // note that outputs change after placing a card.
        Double[] outputs = NEATFunctions.getNEATOutput(players, "Player 0", 3, ((NEATPlayer) players[0]).getGenomeID());
        for (int i=outputs.length-2; i<outputs.length; ++i) { // relevant information
            double output = outputs[i];
            System.out.println(i + " " + output);
        }

        int decision = players[0].getRemoveRoundPlayer().decide(players);
        System.out.println("Decision " + decision);
    }
}
