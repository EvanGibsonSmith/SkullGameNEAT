import java.util.Scanner;

public class BettingRoundPlayer {
    int bet;

    public BettingRoundPlayer() {
        bet = -1; // initial bet, before actual value 
    }

    // TODO do I need maxBet or should I just let players make impossible bets? Probably not a great setup for the AI later down the line
    // TODO make this return the int instead of boolean of success?
    public boolean decide(int currentBet, int maxBet) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("What would you like to bet? (must be above current value of " 
                            + currentBet + " and below or equal to" + maxBet + "). Can also choose not to bet.");
        int newBet = scnr.nextInt();
        scnr.close();
        if (newBet==-1) {
            return false;
        }
        else if (newBet<currentBet || newBet>maxBet) { 
            // TODO add exception throwing
        }

        this.bet = newBet;
        return true;
    }


}
