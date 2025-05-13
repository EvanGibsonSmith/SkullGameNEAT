package src.game_objects;

import java.util.Scanner;
import java.util.Set;

public class BettingRoundPlayerTerminal extends BettingRoundPlayer {
    
    // TODO this code could be simplified, probably removing beganBetting since if the currentBet is 0,
    // that indicates that the betting has just begun (because the first player must bet higher than 0)
    // removing the need for the somewhat clunky extra boolean beganBetting
    // TODO would it be helpful to reorganize such that every decide function takes in the players array like the removal player
    // so we can get any information needed in the decide function?

    public BettingRoundPlayerTerminal(String name) {
        super(name);
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
    public boolean decide(Player[] players, boolean beganBetting, int currentBet, int maxBet) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("What would you like to bet? (must be above current value of " 
                            + currentBet + " and below or equal to " + maxBet + "). Can also choose not to bet (unless beginning betting round).");
        
        Set<Integer> validOptions = validOptions(beganBetting, currentBet, maxBet);
        int newBet = scnr.nextInt();
        while (!validOptions.contains(newBet)) {
            System.out.println("That is not a valid option, please enter another option");
            newBet = scnr.nextInt();
        }
        if (newBet==-1) {
            return false;
        }
        else if (newBet<=currentBet || newBet>maxBet) { 
            System.out.println("Invalid bet.");
        }

        this.bet = newBet;
        //scnr.close(); NOTE scanner never closed
        return true;
    }

}
