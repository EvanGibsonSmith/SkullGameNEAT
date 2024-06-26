package src.game_objects;

import java.util.HashSet;
import java.util.Set;

public abstract class BettingRoundPlayer {
    int bet;
    String name;

    public BettingRoundPlayer(String name) {
        bet = -1; // initial bet, before actual value 
        this.name = name;
    }

    public int getBet() {
        return this.bet;
    }

    public void resetBet() {
        this.bet = 0;
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

    public abstract boolean decide(Player[] players, boolean beganBetting, int currentBet, int maxBet);

}
