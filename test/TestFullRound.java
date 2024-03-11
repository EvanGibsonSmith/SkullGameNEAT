package test;

import src.game_objects.FullRound;
import src.game_objects.Player;

public class TestFullRound {
    
    // NOTE: Tested by using debugger and terminal to verify it is behvaing as intended
    public static void main(String[] args) {
        Player[] players = new Player[2];
        for (int i=0; i<players.length; ++i) {
            players[i] = new Player();
        }
        FullRound round = new FullRound(players, 0);
        round.runRound();
        
        // print player information
        for (int i=0; i<players.length; ++i) { // not showing played cards since they are returned to hand
            Player p = players[i]; 
            System.out.println("Player " + i);
            System.out.println("\t Hand: " + p.getHand());
            System.out.println("\t Points " + p.getPoints());
        }
    }
}
