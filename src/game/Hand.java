import src.data_structures.MultiSet;

public class Hand {
    MultiSet<Card> hand;

    public Hand() {
        hand = new MultiSet<>();
        for (int i=0; i<3; ++i) {
            hand.add(new FlowerCard());
        }
        hand.add(new SkullCard());
    }

    public Card popSkull() {
        return hand.remove(new SkullCard()); // using equals method removes skullCard like one passed
    }

    public Card popFlower() {
        return hand.remove(new FlowerCard());
    }
}