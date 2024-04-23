package src.game_objects;

import java.util.Stack;
import java.util.Scanner;
import java.util.Set;

public class PlacingRoundPlayerTerminal extends PlacingRoundPlayer {
    
    public PlacingRoundPlayerTerminal(Hand hand, Stack<Card> playedCards) {
        super(hand, playedCards);
    }

    public PlacingRoundPlayerTerminal(Hand hand) {
        super(hand);
    }

    /**
     * Decision for which card to play, using scanner to query from player in console.
     * 
     * @param canBeginBetting If the player has the option to not place any card and begin betting. Passed in because the round objects
     *                          is needed for this information
     * @return boolean on if this player ended the round or not.
     */
    public boolean decide(boolean canBeginBetting) { // TODO exception?
        Set<Integer> validOptions = validOptions(canBeginBetting);
    
        // for human player this is just querying to the terminal, could be implemneted by other players in other ways
        Scanner scnr = new Scanner(System.in);
        System.out.println("Would you like to play Skull, Flower, or begin betting round? Type 0 for skull, 1 for flower and 2 for beginning betting round");
        int decision = scnr.nextInt();
        //scnr.close(); TODO not closing right now (which isn't great) so user can continue to give inputs
        while (!validOptions.contains(decision)) {
            System.out.println("That is not a valid option, please enter another option");
            decision = scnr.nextInt();
        }
        // process decision
        if (decision==2) {
            return true; // placing round over flag is raised
        }
        else {
            placeCard(decision);
        }
        return false;
    }
}