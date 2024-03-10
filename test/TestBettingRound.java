package test;

import src.game_objects.BettingRound;
import src.game_objects.Player;

// NOTE: This "test" is just running it and using debugger and terminal verifying it is working as intended
public class TestBettingRound {
    
    public static void main(String[] args) {
        Player[] players = new Player[3];
        for (int i=0; i<players.length; ++i) {
            players[i] = new Player();
        }
        BettingRound round = new BettingRound(players, 3, 0); // Pretend 3 cards were played (as max value allowed to bet)
        int bettingRoundWinnerCursor = round.runRound();
        System.out.println("Winning Cursor: " + bettingRoundWinnerCursor);
        // the cursor should point to the player with the max bet, which should be the same as the max bet from the round
        System.out.println("Max Player's bet " + players[bettingRoundWinnerCursor].getBettingRoundPlayer().getBet());
        System.out.println("Max bet from betting round object " + round.getBetValue());
    }
}
