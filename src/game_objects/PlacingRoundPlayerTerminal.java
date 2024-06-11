package src.game_objects;

import java.util.Stack;
import java.util.Scanner;
import java.util.Set;

public class PlacingRoundPlayerTerminal extends PlacingRoundPlayer {
    
    public PlacingRoundPlayerTerminal(Hand hand, Stack<Card> playedCards, String name) {
        super(hand, playedCards, name);
    }

    public PlacingRoundPlayerTerminal(Hand hand, String name) {
        super(hand, name);
    }

    /**
     * Decision for which card to play, using scanner to query from player in console.
     * 
     * @param players object to use relevant information for decision
     * @param canBeginBetting If the player has the option to not place any card and begin betting round. Passed in because the round objects
     *                        is needed for this information
     * @return boolean on if this player ended the round or not.
     */
    // TODO could this decide just take in the players, similar to comment on the other ones
    public int decide(Player[] players, boolean canBeginBetting) { 
        Set<Integer> validOptions = validOptions(canBeginBetting);
    
        // for human player this is just querying to the terminal, could be implemneted by other players in other ways
        Scanner scnr = new Scanner(System.in);
        System.out.println("Player " + name);
        System.out.println("Would you like to play Skull, Flower, or begin betting round? Type 0 for skull, 1 for flower and 2 for beginning betting round");
        int decision = scnr.nextInt();
        while (!validOptions.contains(decision)) {
            System.out.println("That is not a valid option, please enter another option");
            decision = scnr.nextInt();
        }

        return decision;
    }
}