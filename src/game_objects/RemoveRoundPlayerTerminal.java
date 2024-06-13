package src.game_objects;

import java.util.Scanner;
import java.util.Set;

public class RemoveRoundPlayerTerminal extends RemoveRoundPlayer {

    public RemoveRoundPlayerTerminal(Hand hand, String name) {
        super(hand, name);
    }
    
    /**
     * Allows player to decide which of theirs to remove. This happens after a player failed a bet
     * and revealed another players skull.
     * 
     * @return int representing which card to remove, 0 for skull, 1 for flower
     */
    @Override
    public int decide(Player[] players) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("It was a skull!");
        System.out.println("Please input type of card to remove. 0 for skull, 1 for flower.");
        Set<Integer> validOptions = validOptions();
        int decision = scnr.nextInt();
        while (!validOptions.contains(decision)) {
            System.out.println("That is not a valid option, please enter another option");
            decision = scnr.nextInt();
        }
        //scnr.close(); NOTE scanner never closed
        return decision;
    }
}
