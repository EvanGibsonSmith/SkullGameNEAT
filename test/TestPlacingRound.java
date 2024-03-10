package test;

import src.game_objects.PlacingRound;
import src.game_objects.Player;

public class TestPlacingRound {
    
    // NOTE this "test" just runs round with user input, no explicit cases to pass
    public static void main(String[] args) {
        int numPlayers = 3;
        Player[] players = new Player[numPlayers];
        // population players 
        for (int i=0; i<players.length; ++i) {
            players[i] = new Player();
        }

        // get placing round players 
        PlacingRound round = new PlacingRound(players, 0);
        round.runRound();
    }
}
