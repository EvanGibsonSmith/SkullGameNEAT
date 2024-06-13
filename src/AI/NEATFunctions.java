package src.AI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

import src.game_objects.Card;
import src.game_objects.Game;
import src.game_objects.Player;
import src.game_objects.TerminalPlayer;

// NOTE could all of this live within the NEATPlayer class?
public class NEATFunctions { 

    public static double[] softmax(double[] values) {
        double softMaxTotal = 0;
        for (double value: values) {
            softMaxTotal += Math.exp(value);
        }
        double[] output = new double[values.length];
        for (int i=0; i<values.length; ++i) {
            output[i] = Math.exp(values[i]) / softMaxTotal;
        }
        return output;
    }

    // TODO document
    public static <T> double badSelectionFitness(double[] outputs, HashMap<Integer, T> outputIndexes, Set<T> choices) {
        double fitness = 0;
        for (int outputIndex=0; outputIndex<outputs.length; ++outputIndex) {
            if (outputIndexes.containsKey(outputIndex)) { // if NOT potential choice for this round add fitness
                if (choices.contains(outputIndexes.get(outputIndex))) { // if this choice is currrently valid
                    continue; // in this case, it is a valid choice, so mistake fitness not added
                }
            }
            // mistake fitness (shouldn't have any probability of selection) 
            fitness += Math.pow(outputs[outputIndex], 2);
        }
        return fitness;
    }
    
    // TODO could this and the function below be setup more nicely with the correspondence between indexes and values
    public static <T> T selectBestValidOptions(double[] outputs, HashMap<Integer, T> outputIndexes, Set<T> choices) {
        double maxValue = Double.MIN_VALUE; // to keep track of best output
        int maxIndex = -1;

        double[] softMaxOutputs = softmax(outputs);
        for (int outputIndex=0; outputIndex<softMaxOutputs.length; ++outputIndex) {
            if (outputIndexes.containsKey(outputIndex)) { // if this index is a possibility
                if (choices.contains(outputIndexes.get(outputIndex))) { // if this candidate is a valid choice
                    if (softMaxOutputs[outputIndex] > maxValue) { // if this is a new maximum
                        maxValue = softMaxOutputs[outputIndex];
                        maxIndex = outputIndex;
                    }
                }
            }
        }
        if (maxIndex==-1) { // if no valid choices for NEAT
            throw new Error("There were no valid choices in Round");
        }
        return outputIndexes.get(maxIndex); // return the corresponding move option for this index
    }

    // TODO document
    private static double[] parseNEATOutput(String str) {
        String[] strList = str.substring(1, str.length() - 1).split(",");
        double[] intList = new double[strList.length];
        for (int i=0; i<intList.length; ++i) {
            intList[i] = Double.parseDouble(strList[i]);
        }
        return intList;
    }

    public static double[] getNEATOutput(Player[] players, String name, int phase, int genomeID) {

        Double[] inputs = getNEATParameterInputs(players, name, phase); // uses this to give to AI inputs for decision
        String socketString = socketInput(genomeID, inputs);
        try {
            Socket socket = new Socket("localhost", 9999);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Send commands to the server
            writer.println(socketString);
            String line = reader.readLine();
            //System.out.println("Received from server: " + line);
            socket.close();
            return parseNEATOutput(line);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            throw new Error("Socket did not connect or work properly or Reader/Writer error");
        }
    }

    public static String socketInput(int genomeID, Double[] inputs) {
        return "[" + genomeID + ", " + arrayString(inputs) + "]";
    }   

    public static String arrayString(Object[] arr) {
        String str = "";
        for (Object obj: arr) {
            str += obj + ", ";
        }
        str = str.substring(0, str.length() - 2);
        return "[" + str + "]";
    }

    // TODO could not use ArrayList for this application, potentially. Could also be useful as a HashMap to more easily keep track, but that is more overhead, so likely not
    /**
     * Takes the players of a game and gets information needed as inputs be sent to the NEAT AI.
     * This includes other player points, cards, which cards are placed (of course, the only ones that are known) 
     * and so on. It includes this players cards, including the number total and their placement in the stack.
     */
    public static Double[] getNEATParameterInputs(Player[] players, String thisPlayerName, int phase) {
        ArrayList<Double> outputInfo = new ArrayList<>();
        ArrayList<Double> thisPlayerInfo = new ArrayList<>();
        ArrayList<Double> otherPlayersInfo = new ArrayList<>();

        // TODO could break this up into more readable chunks
        for (int p=0; p<players.length; ++p) {
            Player player = players[p];
            
            // this player has 9 inputs
            if (player.getName().equals(thisPlayerName)) { // collect info for this player 
                int bet = player.getBettingRoundPlayer().getBet();
                if (bet==-1) { // if no bet, set to 0 to align with for loop
                    bet = 0;
                }

                // add all of the betting nodes, 0.0 for those which are not bet, 1.0 for current bet value
                for (int betValue=0; betValue<=players.length*4; ++betValue) {
                    if (bet==betValue) {
                        thisPlayerInfo.add(1.0); // add the current bet value
                    }
                    else {
                        thisPlayerInfo.add(0.0); // otherwise 0
                    }
                }

                // number of played cards so it knows which other inputs to regard
                int stackCards = player.getPlacingRoundPlayer().getStackSize();
                @SuppressWarnings("unchecked")
                Stack<Card> cards = (Stack<Card>) player.getPlayedCards().clone();
                
                // one hot encode cards
                for (int cardAmount=0; cardAmount<=4; ++cardAmount) {
                    if (cardAmount==stackCards) {
                        thisPlayerInfo.add(1.0);
                    }
                    else {
                        thisPlayerInfo.add(0.0);
                    }
                }

                // one hot encode hand size TODO make one hot encoding function to handle all of this stuff
                int handSize = player.getPlacingRoundPlayer().getHand().size();
                for (int handSizeValue=0; handSizeValue<=4; ++handSizeValue) {
                    if (handSizeValue==handSize) {
                        thisPlayerInfo.add(1.0);
                    }
                    else {
                        thisPlayerInfo.add(0.0);
                    }
                }

                // which cards are played (0 if card not available)
                for (int cardIndex=0; cardIndex<4; ++cardIndex) {
                    if (cards.empty()) { // if empty we ran out of cards so just add 0s
                        thisPlayerInfo.add(0.0);
                    }
                    else if (cards.pop().getType()=="skull") {
                        thisPlayerInfo.add(1.0);
                    }
                    else {
                        thisPlayerInfo.add(0.0);
                    }
                }

                thisPlayerInfo.add((double) player.getPoints());
            }

            // other players
            else {
                int bet = player.getBettingRoundPlayer().getBet();
                if (bet==-1) { // if no bet
                    bet = 0;
                }
                // add all of the betting nodes, 0.0 for those which are not bet, 1.0 for current bet value
                for (int betValue=0; betValue<=players.length*4; ++betValue) {
                    if (bet==betValue) {
                        otherPlayersInfo.add(1.0); // add the current bet value
                    }
                    else {
                        otherPlayersInfo.add(0.0); // otherwise 0
                    }
                }

                int stackCards = player.getPlacingRoundPlayer().getStackSize();
                for (int cardAmount=0; cardAmount<=4; ++cardAmount) {
                    if (cardAmount==stackCards) {
                        otherPlayersInfo.add(1.0);
                    }
                    else {
                        otherPlayersInfo.add(0.0);
                    }
                }

                int handSize = player.getPlacingRoundPlayer().getHand().size();
                for (int handSizeValue=0; handSizeValue<=4; ++handSizeValue) {
                    if (handSizeValue==handSize) {
                        otherPlayersInfo.add(1.0);
                    }
                    else {
                        otherPlayersInfo.add(0.0);
                    }
                }

                otherPlayersInfo.add((double) player.getPoints());
            }
        }
        outputInfo.addAll(thisPlayerInfo);
        outputInfo.addAll(otherPlayersInfo);

        // 4 nodes for the phase information
        for (int p=0; p<4; ++p) { // add one for the correct phase, 0 for the other phases
            if (phase==p) {
                outputInfo.add(1.0);
            }
            else {
                outputInfo.add(0.0);
            }
        }

        return outputInfo.toArray(new Double[0]); // new Integer[0] passes type used 
    }

    public static void main(String[] args) {
        Player[] players = new Player[4];
        for (int i=0; i<4; ++i) {
            players[i] = new TerminalPlayer("Player" + i);
        }
        Game game = new Game(players);
        game.runGame();
        Double[] reallyJava = getNEATParameterInputs(players, "Player1", 3);
        for (Double item: reallyJava) {
            System.out.println(item);
        }

    }
}
