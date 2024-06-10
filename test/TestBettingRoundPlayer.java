package test;

import src.game_objects.BettingRoundPlayer;
import src.game_objects.BettingRoundPlayerTerminal;
import src.game_objects.Player;

// NOTE: This "test" is just running it and determining through debugging/terminal it is functioning properly
public class TestBettingRoundPlayer {

    public static void main(String[] args) {
        BettingRoundPlayer player = new BettingRoundPlayerTerminal("BettingRoundPlayer0");
        int currentBet = 1;
        int maxBet = 3;
        player.decide(new Player[] {}, true, currentBet, maxBet);
    }

}
