package src.game_objects;

import java.util.Scanner;
import java.util.Stack;
import java.util.Set;

public class RevealingRoundPlayerTerminal extends RevealingRoundPlayer {

    public RevealingRoundPlayerTerminal(Hand hand, Stack<Card> playedCards) {
        super(hand, playedCards);
    }
    
    public String decide(Player[] players) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please input the name of the player you would like to pick.");
        Set<String> validOptions = validOptions(players);
        String decision = scnr.nextLine();
        while (!validOptions.contains(decision)) {
            System.out.println("That is not a valid option, please enter another option");
            decision = scnr.nextLine();
        }
        //scnr.close(); TODO not closing right now (which isn't great) so user can continue to give inputs
        return decision;
    }

}
