package src.game_objects;

import java.util.Scanner;
import java.util.Stack;

import src.AI.NEATFunctions;

import java.util.Set;

public class RevealingRoundPlayerTerminal extends RevealingRoundPlayer {

    public RevealingRoundPlayerTerminal(Hand hand, Stack<Card> playedCards, String name) {
        super(hand, playedCards, name);
    }
    
    /**
     * Decision for which card to reveal, using the terminal to query the player.
     * The players are passed in so that we can pick the player to reveal the card of.
     * 
     * @param players array of all of the players
     * @return a string of the name of the player that will be selected
     */
    public String decide(Player[] players) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please input the name of the player you would like to pick.");
        Set<String> validOptions = validOptions(players);
        String decision = scnr.nextLine();
        while (!validOptions.contains(decision)) {
            System.out.println("That is not a valid option, please enter another option");
            decision = scnr.nextLine();
        }
        //scnr.close(); NOTE scanner never closed
        return decision;
    }
}
