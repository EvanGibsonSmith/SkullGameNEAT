package test;

import src.game_objects.Game;
import src.game_objects.NEATPlayer;
import src.game_objects.Player;
import src.game_objects.TerminalPlayer;

public class TestGame {
    
    public static void main(String[] args) {
        Player[] players = new Player[4];
        // TODO make this a test, but a nice sanity check is that two players with the same genome, flip winner based on who is first and have flipped fitness values
        players[0] = new NEATPlayer("Alice", 3); 
        players[1] = new NEATPlayer("Bob", 3);
        players[2] = new NEATPlayer("Eve", 3);
        players[3] = new NEATPlayer("Zack", 3);

        Game game = new Game(players);
        game.runGame();
        ((NEATPlayer) game.getWinner()).incrementWonGames();
        
        // print player information
        for (int i=0; i<players.length; ++i) { // not showing played cards since they are returned to hand
            Player p = players[i]; 
            System.out.println("Player " + p.getName());
            System.out.println("\t Hand: " + p.getHand());
            System.out.println("\t Points " + p.getPoints());
        }
        System.out.println("Winner Info!");
        Player winner = game.getWinner();
        System.out.println("\t Name: " + winner.getName());
        System.out.println("\t Hand: " + winner.getHand());
        System.out.println("\t Points: " + winner.getPoints());
 
        System.out.println("Player Fitnesses");
        for (Player p: players) {
            System.out.println(p.getName() + " has fitness " + ((NEATPlayer) p).getFitness());
        }
    }
}
