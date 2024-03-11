package test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import src.game_objects.Player;
import src.game_objects.RevealingRound;


public class TestRevealingRound {
    
    // NOTE this "test" just runs round with user input, no explicit cases to pass
    public static void main(String[] args) {
        Player[] players = new Player[3];
        for (int i=0; i<players.length; ++i) {
            players[i] = new Player();
        }
        players[0].getPlacingRoundPlayer().placeCard(false);
        players[1].getPlacingRoundPlayer().placeCard(false);
        players[2].getPlacingRoundPlayer().placeCard(false);

        players[0].getPlacingRoundPlayer().placeCard(false);
        players[1].getPlacingRoundPlayer().placeCard(true);
        players[2].getPlacingRoundPlayer().placeCard(false);

        int playerBetAmount = 6;
        int cursor = 0;
        RevealingRound round = new RevealingRound(players, playerBetAmount, cursor);
        round.runRound();

        // print player information
        for (int i=0; i<players.length; ++i) { // not showing played cards since they are returned to hand
            Player p = players[i]; 
            System.out.println("Player " + i);
            System.out.println("\t Hand: " + p.getHand());
            System.out.println("\t Points " + p.getPoints());
        }
    }
    
    @Test
    void flipOwnCard() {
        Player[] players = new Player[3];
        for (int i=0; i<players.length; ++i) {
            players[i] = new Player();
            players[i].getPlacingRoundPlayer().placeCard(false); // every player has played a flower
        }

        RevealingRound round = new RevealingRound(players, 1, 1);
        round.runRound(); // player 1 flips own card automatically, gets point
        assertEquals(players[1].getPoints(), 1);

        // no other players get points and hands returned
        assertEquals(players[0].getPoints(), 0);
        assertEquals(players[2].getPoints(), 0);
        
        assertEquals(players[0].getHand().size(), 4);
        assertEquals(players[1].getHand().size(), 4);
        assertEquals(players[2].getHand().size(), 4);
    }

    @Test
    void flipOwnCards() {
        Player[] players = new Player[3];
        for (int i=0; i<players.length; ++i) {
            players[i] = new Player();
            players[i].getPlacingRoundPlayer().placeCard(false); // every player has played a flower
        }
        players[1].getPlacingRoundPlayer().placeCard(false); // player 1 has two flowers

        RevealingRound round = new RevealingRound(players, 2, 1);// player only need to filp own cards
        round.runRound(); // player should now have point, no removed cards (they flipped their own automatically)
        assertEquals(players[1].getHand().size(), 4);
        assertEquals(players[1].getPoints(), 1);
        assertEquals(players[0].getPoints(), 0);
        assertEquals(players[2].getPoints(), 0);

        // other players have all cards returned
        assertEquals(players[0].getHand().size(), 4);
        assertEquals(players[2].getHand().size(), 4);
    }

    @Test 
    void flipOwnSingleSkull() {

    }

    @Test
    void flipOwnStackedSkull() {

    }

    // TODO doesn't work (the content doesn't fail, the testing code does)
    @Test
    void flipOneOtherPlayerSuccess() {
        Player[] players = new Player[3];
        for (int i=0; i<players.length; ++i) {
            players[i] = new Player();
        }
        players[0].getPlacingRoundPlayer().placeCard(false); // player 0 flower
        players[1].getPlacingRoundPlayer().placeCard(true); // player 1 skull
        players[2].getPlacingRoundPlayer().placeCard(false); // player 2 flower

        // set input stream to manually control for test
        RevealingRound round = new RevealingRound(players, 2, 0);
        // first flip automatic, need to select another player
        System.setIn(new ByteArrayInputStream("1\n1\n1\n1".getBytes()));
        Scanner scnr = new Scanner(System.in);
        System.out.println(scnr.nextInt());

        //round.runRound(); // selects 2, so success
    }

}
