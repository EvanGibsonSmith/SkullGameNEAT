import java.util.Stack;

// TODO potentially this would work best split into a bbetting player and a round player, useful for AI potentially as well
public class RoundPlayer {
    Hand hand;
    Stack<Card> playedCards;
    Stack<Round> rounds;

    public RoundPlayer() {
        hand = new Hand();
        playedCards = new Stack<>();
        rounds = new Stack<>();
    }

    private Card popCard(boolean isSkull) {
        if (isSkull) {
            return hand.popSkull();
        }
        else {
            return hand.popFlower();
        }
    }
    
    // TODO document. TODO catch exception potentially throws by popCard if there is no flower/skull and it is played
    public void playCard(boolean isSkull) {
        Card toPlay = popCard(isSkull);
        playedCards.add(toPlay);
    }

    /**
     *
     * Adds round to the stacks of rounds the RoundPlayer
     * has been through.
     * @param round round added to internal stack
     */
    public void addRound(Round round) {
        rounds.add(round);
    }
}
