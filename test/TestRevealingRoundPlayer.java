package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Stack;

import org.junit.jupiter.api.Test;

import src.game_objects.Card;
import src.game_objects.Hand;
import src.game_objects.RevealingRoundPlayer;

public class TestRevealingRoundPlayer {

    @Test 
    void flipPlayersCardSkull() {
        // usually hand and stack are given from the player class 
        Hand hand = new Hand();
        hand.removeCard(new Card("skull"));
        Stack<Card> playedCards = new Stack<>();
        playedCards.add(new Card("skull"));
        // this player has all of their card, and placed a skull
        RevealingRoundPlayer player = new RevealingRoundPlayer(hand, playedCards);
        assertTrue(player.flip()); // player has skull on top, so true
    }

    @Test 
    void flipPlayersCardFlower() {
        // usually hand and stack are given from the player class 
        Hand hand = new Hand();
        hand.removeCard(new Card("flower"));
        hand.removeCard(new Card("flower"));
        Stack<Card> playedCards = new Stack<>();
        playedCards.add(new Card("flower"));
        // this player has 3 of their cards, and placed a flower
        RevealingRoundPlayer player = new RevealingRoundPlayer(hand, playedCards);
        assertEquals(player.getHand().size(), 2);
        assertFalse(player.flip()); // player has flower on top, so false
        assertEquals(player.getHand().size(), 3); // returned to hand
    }

    @Test 
    void flipPlayersCardStacked() {
        // usually hand and stack are given from the player class 
        Hand hand = new Hand();
        hand.removeCard(new Card("skull"));
        hand.removeCard(new Card("flower"));
        Stack<Card> playedCards = new Stack<>();
        playedCards.add(new Card("skull"));
        playedCards.add(new Card("flower"));
        // this player has all of their cards, placed skull on BOTTOM, then flower
        RevealingRoundPlayer player = new RevealingRoundPlayer(hand, playedCards);
        assertEquals(player.getHand().size(), 2);
        assertFalse(player.flip()); // player has flower on top, so false
        // now that flipped card has been returned to hand
        assertEquals(player.getHand().size(), 3);
        assertTrue(player.flip()); // now skull on top of stack
        assertEquals(player.getHand().size(), 4);
    }
    
    // TODO add return cards test
}
