
public class RevealingRound {
    RevealingRoundPlayer[] players;
    int playerBetAmount;
    int cursor; // cursor does not change, since there is only one player revealing TODO make constant

    public RevealingRound(RevealingRoundPlayer[] players, int playerBetAmount, int cursor) {
        this.players = players;
        this.playerBetAmount = playerBetAmount;
        this.cursor = cursor;
    }
    
    // TODO document, helper for runRound(). Returns if successful
    private boolean flipCards() {
        for (int flipCount=0; flipCount<playerBetAmount; ++flipCount) {
            int decision = queryPlayer();
            boolean isFlippedSkull = players[decision].flip(); 
            if (isFlippedSkull) {
                return false;
            }
        }
        return true;
    }

    // TODO document, potentially, helper for runRound(), ran when succesful
    private void incrementPoints() {players[cursor].incrementPoints();}

    private void removeCard() {
        int cardRemoveType = players[cursor].decideRemoveCard();
        Card removalCard = null;
        switch (cardRemoveType) {
            case 0:
                removalCard = new SkullCard(); // TODO instead of skull and flower card just make a card with field of either skull or flower since there are no methods to differentiate the two
                break;
            case 1:
                removalCard = new FlowerCard();
                break;

            default:
                break;
        }
        players[cursor].getHand().removeCard(removalCard);
    }

    public void runRound() {
        boolean successful = flipCards();
        if (successful) {
            incrementPoints();
        }
        else {
            removeCard();
        }
    }

    private int queryPlayer() {
        return players[cursor].decideFlip(); // chooseCard adds that card to players stack
    }
}
