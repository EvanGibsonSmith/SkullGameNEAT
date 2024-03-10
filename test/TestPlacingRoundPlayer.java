package test;

import static org.junit.Assert.assertArrayEquals;

import org.junit.jupiter.api.Test;

import src.game_objects.Card;
import src.game_objects.Hand;
import src.game_objects.PlacingRoundPlayer;

public class TestPlacingRoundPlayer {

    @Test
    void options() {
        Hand emptyHand = new Hand();
        emptyHand.clearCards(); // give player empty hand

        PlacingRoundPlayer player = new PlacingRoundPlayer(emptyHand);

        // player should have no options (empty hand)
        assertArrayEquals(player.options(), new boolean[] {false, false});

        player = new PlacingRoundPlayer(new Hand()); // player now has full hand
        assertArrayEquals(player.options(), new boolean[] {true, true});
        // remove skull, should only be able to play flower
        player.placeCard(true);
        assertArrayEquals(player.options(), new boolean[] {false, true});

        // now player given empty hand 
        player = new PlacingRoundPlayer(emptyHand);
        player.getHand().addCard(new Card("skull")); // now player has only a skull
        assertArrayEquals(player.options(), new boolean[] {true, false});
    }

}
