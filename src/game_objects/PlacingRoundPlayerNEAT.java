package src.game_objects;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

import src.AI.NEATFunctions;

public class PlacingRoundPlayerNEAT extends PlacingRoundPlayer {
    int genomeID = -1;
    double fitness = 0; // only negative fitness from mistakes in guessing
    HashMap<Integer, Integer> outputIndexes = new HashMap<>(); // correspondence between output index and move value

    public PlacingRoundPlayerNEAT(Hand hand, Stack<Card> playedCards, String name, int genomeID) {
        super(hand, playedCards, name);
        this.genomeID = genomeID;
    }

    public PlacingRoundPlayerNEAT(Hand hand, String name, int genomeID) {
        super(hand, name);
        this.genomeID = genomeID;
    }

    public void reset() {
        outputIndexes.clear(); // clear HashMap, ready to be built for next game
    }

    private void buildHashMap() {
        if (!outputIndexes.isEmpty()) { // don't built if already built
            return;
        }
        outputIndexes.put(0, 0); // first output corresponds to placing flower
        outputIndexes.put(1, 1); // second output corresponds to placing skull (1 comes from decisions)
        outputIndexes.put(2, 2); // third output corresponds to ending the placing round (2)
    }
    
    public double getFitness() {
        return this.fitness;
    }
    
    /**
     * Decision for which card to play, using scanner to query from player in console.
     * 
     * @param canBeginBetting If the player has the option to not place any card and begin betting round. Passed in because the round objects
     *                        is needed for this information
     * @return boolean on if this player ended the round or not.
     */
    @Override
    public int decide(Player[] players, boolean canBeginBetting) { 
        buildHashMap();
        Double[] neatResults = NEATFunctions.getNEATOutput(players, name, 0, genomeID); // 0 is the PlacingRound phase
        Set<Integer> choices = validOptions(canBeginBetting);
        fitness -= NEATFunctions.badSelectionFitness(neatResults, outputIndexes, choices); // based on error, update fitness from invalid choices
        return NEATFunctions.selectBestValidOptions(neatResults, outputIndexes, choices); // actually make decision based on which are valid
    }

    public static void main(String[] args) {
        /*Double[] inputs = new Double[112];
        for (int i=0; i<112; ++i) {
            inputs[i] = ((double) i)/112.0; // TODO make this input proper, just for debugging here
        }
        for (int genomeID=10; genomeID<11; ++genomeID) {
            String socketString = NEATFunctions.socketInput(genomeID, inputs);
            try {
                Socket socket = new Socket("localhost", 9999);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                // Send commands to the server
                writer.println(socketString);
                // TODO need to be blocking here?
                System.out.println("Received from server: " + reader.readLine()); // Output: 1

                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } */

        // TODO make this testing nicer and more rigorous
        // dummy players    
        Player[] players = new Player[4];
        players[0] = new NEATPlayer("Player 0", 49);
        players[1] = new TerminalPlayer("Player 1");
        players[2] = new TerminalPlayer("Player 2");
        players[3] = new TerminalPlayer("Player 3");

        PlacingRoundPlayerNEAT placingRoundPlayer = ((PlacingRoundPlayerNEAT) players[0].getPlacingRoundPlayer());

        placingRoundPlayer.placeCard(true); // TODO in formal testing do test were only flower, only skull, and no options
        placingRoundPlayer.placeCard(false);
        placingRoundPlayer.placeCard(false);

        // have to check outputs after placing card since it changes the outputs
        Double[] outputs = NEATFunctions.getNEATOutput(players, "Player 0", 0, ((NEATPlayer) players[0]).getGenomeID());
        for (double output: outputs) {
            System.out.println(output + ",");
        }

        int decision = placingRoundPlayer.decide(players, false);
        System.out.println(decision);
        System.out.println(placingRoundPlayer.validOptions(false));
        System.out.println(placingRoundPlayer.fitness);
    }
}