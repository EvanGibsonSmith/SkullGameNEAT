package src.game_objects;

import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

import src.AI.NEATFunctions;

public class RevealingRoundPlayerNEAT extends RevealingRoundPlayer {
    int genomeID = -1;
    double fitness = 0.0; // only negative fitness from mistakes in guessing
    HashMap<Integer, String> outputIndexes = new HashMap<>();

    // TODO could pass all the player objects in the constructor instead of decide for all these players
    public RevealingRoundPlayerNEAT(Hand hand, Stack<Card> playedCards, String name, int genomeID) {
        super(hand, playedCards, name);
        this.genomeID = genomeID;
    }

    public void reset() {
        outputIndexes.clear(); // clear HashMap, ready to be built for next game
    }

    // TODO could potentially reorganize so whole HashMap is built somewhere rather than in chunks?
    private void buildHashMap(Player[] players) {
        int n = players.length;
        if (!outputIndexes.isEmpty()) { // don't built if already built
            return;
        }
        int playerIndex = 0;
        for (int outputIndex=4+4*n; outputIndex<4+4*n+(n-1); ++outputIndex) { // getting outputs nodes corresponding to names
            if (players[playerIndex].getRevealingRoundPlayer().equals(this)) { // if it is this player, move to next player as option
                ++playerIndex;
            }
            outputIndexes.put(outputIndex, players[playerIndex].getName());
            ++playerIndex;
        } 
    }

    public double getFitness() {
        return this.fitness;
    }
    
    /**
     * Decision for which card to reveal, using the terminal to query the player.
     * The players are passed in so that we can pick the player to reveal the card of.
     * 
     * @param players array of all of the players (including inactive)
     * @return a string of the name of the player that will be selected
     */
    @Override
    public String decide(Player[] players) {
        Player[] activePlayers = Game.getActivePlayers(players);
        buildHashMap(players); // only build it once, but players are needed for nodes
        Double[] neatResults = NEATFunctions.getNEATOutput(players, name, 2, genomeID); // 2 is phrase for RevealingRound
        Set<String> choices = validOptions(activePlayers);
        System.out.println("Valid Choices :" + choices);
        fitness -= NEATFunctions.badSelectionFitness(neatResults, outputIndexes, choices); // based on error, update fitness from invalid choices
        return NEATFunctions.selectBestValidOptions(neatResults, outputIndexes, choices); // actually make decision based on which are valid
    }

        
    public static void main(String[] args) {
        
        // dummy players    
        /*Player[] players = new Player[4];
        players[0] = new NEATPlayer("Player 0",65);
        players[1] = new TerminalPlayer("Player 1");
        players[2] = new TerminalPlayer("Player 2");
        players[3] = new TerminalPlayer("Player 3");

        players[0].getPlacingRoundPlayer().placeCard(false);
        players[1].getPlacingRoundPlayer().placeCard(false);
        players[2].getPlacingRoundPlayer().placeCard(false);
        players[3].getPlacingRoundPlayer().placeCard(true);

        // note that outputs change after placing a card.
        Double[] outputs = NEATFunctions.getNEATOutput(players, "Player 0", 2, ((NEATPlayer) players[0]).getGenomeID());
        int n = players.length;
        for (int i=4+4*n; i<4+4*n+(n-1); ++i) { // relevant information
            double output = outputs[i];
            System.out.println(i + " " + output);
        }

        String decision = players[0].getRevealingRoundPlayer().decide(players);
        System.out.println(decision);*/

        // this recreates a bug scenario    
        Player[] players = new Player[2];
        players[0] = new NEATPlayer("Alice",46);
        players[1] = new NEATPlayer("Bob", 2);

        players[0].getPlacingRoundPlayer().placeCard(true); 
        players[1].getHand().removeCard((new Card("skull")));
        players[1].getHand().removeCard((new Card("flower")));
        players[1].getHand().removeCard((new Card("flower")));

        // note that outputs change after placing a card.
        Double[] outputs = NEATFunctions.getNEATOutput(players, "Bob", 2, ((NEATPlayer) players[0]).getGenomeID());
        int n = players.length;
        for (int i=4+4*n; i<4+4*n+(n-1); ++i) { // relevant information
            double output = outputs[i];
            System.out.println(i + " " + output);
        }

        System.out.println("Valid Choices: " + players[1].getRevealingRoundPlayer().validOptions(players));
        String decision = players[1].getRevealingRoundPlayer().decide(players);
        System.out.println("Decision " + decision);
    }
}
