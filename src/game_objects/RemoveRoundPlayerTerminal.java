package src.game_objects;

import java.util.Scanner;
import java.util.Set;

public class RemoveRoundPlayerTerminal extends RemoveRoundPlayer {

    public RemoveRoundPlayerTerminal(Hand hand) {
        super(hand);
    }

    public int decide() {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please input type of card to remove. 0 for skull, 1 for flower.");
        Set<Integer> validOptions = validOptions();
        int decision = scnr.nextInt();
        while (!validOptions.contains(decision)) {
            System.out.println("That is not a valid option, please enter another option");
            decision = scnr.nextInt();
        }
        //scnr.close(); TODO not closing right now because System.in needed later
        return decision;
    }
}
