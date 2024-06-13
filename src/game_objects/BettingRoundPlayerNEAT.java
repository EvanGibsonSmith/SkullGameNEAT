package src.game_objects;

import java.util.HashMap;
import java.util.Set;

import src.AI.NEATFunctions;

public class BettingRoundPlayerNEAT extends BettingRoundPlayer {
    int genomeID = -1;
    double fitness = 0; // only negative fitness from mistakes in guessing
    HashMap<Integer, Integer> outputIndexes = new HashMap<>();

    public BettingRoundPlayerNEAT(String name, int genomeID) {
        super(name);
        this.genomeID = genomeID;
    }

    public void reset() {
        outputIndexes.clear(); // clear HashMap, ready to be built for next game
    }

    void buildHashMap(int numPlayers) {
        if (!outputIndexes.isEmpty()) { // don't built if already built
            return;
        }
        // note less than or EQUAL since every player can have 4 cards for bet maximum
        outputIndexes.put(3, -1); // passing is betting -1 on third output node
        for (int outputIndex=1; outputIndex<=numPlayers*4; ++outputIndex) {
            outputIndexes.put(outputIndex+3, outputIndex);
        } 
    }

    public double getFitness() {
        return this.fitness;
    }

    /**
     * Decision for which card to play, using scanner to query from player in console.
     * 
     * @param beganBetting If the betting round "has begun". If true, player must place bet and cannot "pass"
     * @param currentBet The current value of the bet at the table, if player does not pass they must bet higher than this value
     * @param maxBet The maximum allowable bet. This represents the number of cards at the table, because a bet higher than this cannot
     *               possible be won.
     * @return boolean on if this player ended the round or not.
     */
    // TODO maybe have this pass in the whole game object so that the get Inputs can be called, not all the info is used, but the Game gives quite a bit of it to you?
    @Override
    public boolean decide(Player[] players, boolean beganBetting, int currentBet, int maxBet) {
        buildHashMap(players.length); // only build it once, but players are needed for nodes
        double[] neatResults = NEATFunctions.getNEATOutput(players, name, 1, genomeID); // 1 is the BettingRound phase
        Set<Integer> choices = validOptions(beganBetting, currentBet, maxBet);
        fitness -= NEATFunctions.badSelectionFitness(neatResults, outputIndexes, choices); // based on error, update fitness from invalid choices
        int bettingOutput = NEATFunctions.selectBestValidOptions(neatResults, outputIndexes, choices); // actually make decision based on which are valid
        if (bettingOutput==-1) {
            return false;
        }
        else { // if valid bet value was given
            super.bet = bettingOutput;
            return true;
        }
    }

    public static void main(String[] args) {
        // TODO put this test in testing folder, commented out for now
        // dummy players    
        /*Player[] players = new Player[4];
        players[0] = new TerminalPlayer("Player 0");
        players[1] = new TerminalPlayer("Player 1");
        players[2] = new TerminalPlayer("Player 2");
        players[3] = new TerminalPlayer("Player 3");

        Double[] outputs = NEATFunctions.getNEATOutput(players, "Player 0", 3, 8);
        for (double output: outputs) {
            System.out.println(output);
        }

        System.out.println("Testing getting specific input stuff");
        Double[] inputs = NEATFunctions.getNEATParameterInputs(players, "Player 1", 1);
        String[] phaseNames = new String[] {"placingPhase", "bettingPhase", "revealPhase", "removePhase"};
        for (String phaseName: phaseNames) {
            int phaseIdx = NEATFunctions.getNEATOutputIndexByName(players.length, phaseName);
            System.out.println("Verifying phase " + phaseName + " Node Value:" + inputs[phaseIdx]);
        }

        // verify this player hand and stack size
        int thisPlayerNumCardsIdx = NEATFunctions.getNEATOutputIndexByName(players.length, "thisPlayerHandSize");
        System.out.println("Verifying hand size 4 (node val 1.0), Node Value:" + inputs[thisPlayerNumCardsIdx]);
        int thisPlayerStackSizeIdx = NEATFunctions.getNEATOutputIndexByName(players.length, "thisPlayerStackSize");
        System.out.println("Verifying stack size 0 (node val 0.0), Node Value:" + inputs[thisPlayerStackSizeIdx]);

        // verify for other players
        for (int otherPlayerIdx=1; otherPlayerIdx<players.length; ++otherPlayerIdx) {
            int otherPlayerNumCardsIdx = NEATFunctions.getNEATOutputIndexByName(players.length, "otherPlayer" + otherPlayerIdx + "HandSize");
            System.out.println("Verifying hand size 4 (node val 1.0), Node Value:" + inputs[otherPlayerNumCardsIdx]);
            int otherPlayerStackSizeIdx = NEATFunctions.getNEATOutputIndexByName(players.length, "otherPlayer" + otherPlayerIdx + "StackSize");
            System.out.println("Verifying stack size 0 (node val 0.0), Node Value:" + inputs[otherPlayerStackSizeIdx]);
        }*/

        // dummy players    
        Player[] players = new Player[4];
        players[0] = new NEATPlayer("Player 0",2);
        players[1] = new TerminalPlayer("Player 1");
        players[2] = new TerminalPlayer("Player 2");
        players[3] = new TerminalPlayer("Player 3");

        players[0].getPlacingRoundPlayer().placeCard(false);
        players[1].getPlacingRoundPlayer().placeCard(false);
        players[2].getPlacingRoundPlayer().placeCard(false);
        players[3].getPlacingRoundPlayer().placeCard(true);

        // note that outputs change after placing a card.
        double[] outputs = NEATFunctions.getNEATOutput(players, "Player 0", 1, ((NEATPlayer) players[0]).getGenomeID());
        for (double output: outputs) {
            System.out.println(output);
        }

        System.out.println("Bet before: " + players[0].getBettingRoundPlayer().getBet());
        boolean decision = players[0].getBettingRoundPlayer().decide(players, false, -1, 2);
        System.out.println(decision);
        System.out.println("Bet after: " + players[0].getBettingRoundPlayer().getBet());
    }
}