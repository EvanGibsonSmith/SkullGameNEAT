package src.game_objects;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class BettingRoundPlayer {
    int bet;

    public BettingRoundPlayer() {
        bet = -1; // initial bet, before actual value 
    }

    public int getBet() {
        return this.bet;
    }

    public Set<Integer> validOptions(boolean beganBetting, int currentBet, int maxBet) {
        Set<Integer> options = new HashSet<>();
        for (int b=currentBet+1; b<=maxBet; ++b) {
            options.add(b);
        }
        if (!beganBetting) { // if didn't begin betting, can end betting round
            options.add(-1);
        }
        return options;
    }
    
    // TODO make this return the int instead of boolean of success?
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
