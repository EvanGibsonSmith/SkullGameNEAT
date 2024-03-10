package test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import src.game_objects.Card;
import src.game_objects.Hand;

public class TestHand {
    
    @Test
    void addCards() {
        Hand hand = new Hand();
        hand.clearCards();

        assertEquals(hand.getMultiSet().size(), 0);

        Card addedCard = new Card("skull");
        hand.addCard(addedCard);

        assertEquals(hand.getMultiSet().size(), 1);
        assertFalse(hand.getMultiSet().contains(new Card("flower")));
        assertTrue(hand.getMultiSet().contains(addedCard));
        assertTrue(hand.getMultiSet().contains(new Card("skull"))); // since addedCard should be equivalent 
        
        Card flowerCard1 = new Card("flower");
        Card flowerCard2 = new Card("flower");
        hand.addCard(flowerCard1);
        hand.addCard(flowerCard2);

        assertEquals(hand.getMultiSet().size(), 3);
        assertTrue(hand.getMultiSet().contains(flowerCard1));
        assertTrue(hand.getMultiSet().contains(flowerCard2));
        assertTrue(hand.getMultiSet().contains(new Card("flower")));
        assertTrue(hand.getMultiSet().contains(new Card("skull")));
    } 

    @Test 
    void removeCards() {
        Hand hand = new Hand();
        
        assertEquals(hand.size(), 4);

        hand.removeCard(new Card("skull"));
        assertEquals(hand.size(), 3);
        assertFalse(hand.getMultiSet().contains(new Card("skull")));
        assertTrue(hand.getMultiSet().contains(new Card("flower")));

        hand.removeCard(new Card("flower"));
        hand.removeCard(new Card("flower"));
        hand.removeCard(new Card("flower"));

        assertEquals(hand.size(), 0);
        assertFalse(hand.getMultiSet().contains(new Card("skull")));
        assertFalse(hand.getMultiSet().contains(new Card("flower")));
    }

    @Test 
    void addAndRemoveCards() {
        Hand hand = new Hand();

        assertEquals(hand.size(), 4);
        assertTrue(hand.getMultiSet().contains(new Card("skull")));
        assertTrue(hand.getMultiSet().contains(new Card("flower")));

        hand.removeCard(new Card("skull"));
        assertEquals(hand.size(), 3);
        assertFalse(hand.getMultiSet().contains(new Card("skull")));
        assertTrue(hand.getMultiSet().contains(new Card("flower")));

        hand.removeCard(new Card("flower"));
        assertEquals(hand.size(), 2);
        assertFalse(hand.getMultiSet().contains(new Card("skull")));
        assertTrue(hand.getMultiSet().contains(new Card("flower")));

        hand.addCard(new Card("skull"));
        assertEquals(hand.size(), 3);
        assertTrue(hand.getMultiSet().contains(new Card("skull")));
        assertTrue(hand.getMultiSet().contains(new Card("flower")));
    }

    @Test
    void pop() {
        Hand hand = new Hand();
        assertEquals(hand.popSkull(), new Card("skull"));
        assertEquals(hand.size(), 3);

        // no more skulls so expect error when another is popped
        assertThrows(IllegalArgumentException.class, () -> hand.popSkull());
        
        assertEquals(hand.popFlower(), new Card("flower"));
        assertEquals(hand.size(), 2);
        hand.removeCard(new Card("flower"));
        assertEquals(hand.size(), 1);
        assertEquals(hand.popFlower(), new Card("flower"));
        assertEquals(hand.size(), 0);
        
        // no more flowers so expect error when another is popped or removed
        assertThrows(IllegalArgumentException.class, () -> hand.popFlower());
        assertThrows(IllegalArgumentException.class, () -> hand.removeCard(new Card("flower")));
    }
}
