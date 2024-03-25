package test;

import src.game_objects.Game;
import src.game_objects.Player;

public class TestGame {
    
    public static void main(String[] args) {
        Player[] players = new Player[3];
        players[0] = new Player("Alice");
        players[1] = new Player("Bob");
        players[2] = new Player("Eve");

        Game game = new Game(players, 0);
        game.runGame();
        
        // print player information
        for (int i=0; i<players.length; ++i) { // not showing played cards since they are returned to hand
            Player p = players[i]; 
            System.out.println("Player " + p.getName());
            System.out.println("\t Hand: " + p.getHand());
            System.out.println("\t Points " + p.getPoints());
        }
        System.out.println("Winner Info!");
        Player winner = game.getWinner();
        System.out.println("\t Hand: " + winner.getHand());
        System.out.println("\t Points: " + winner.getPoints());
    }
}
