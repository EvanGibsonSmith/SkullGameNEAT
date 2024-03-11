package test;

import src.game_objects.BettingRoundPlayer;

// NOTE: This "test" is just running it and determining through debugging/terminal it is functioning properly
public class TestBettingRoundPlayer {

    public static void main(String[] args) {
        BettingRoundPlayer player = new BettingRoundPlayer();
        int currentBet = 1;
        int maxBet = 3;
        player.decide(true, currentBet, maxBet);
    }

}
