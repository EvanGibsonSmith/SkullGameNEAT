package src.game_objects;

import java.util.Scanner;
import java.util.Set;

public class BettingRoundPlayerTerminal extends BettingRoundPlayer {
    
    public boolean decide(boolean beganBetting, int currentBet, int maxBet) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("What would you like to bet? (must be above current value of " 
                            + currentBet + " and below or equal to " + maxBet + "). Can also choose not to bet (unless beginning betting round).");
        
        Set<Integer> validOptions = validOptions(beganBetting, currentBet, maxBet);
        int newBet = scnr.nextInt();
        //scnr.close(); TODO not closing right now (which isn't great) so user can continue to give inputs
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
        return true;
    }


}
