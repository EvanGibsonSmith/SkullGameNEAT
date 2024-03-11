package src.game_objects;

import java.util.Stack;

public class RevealingRound {
    RevealingRoundPlayer[] players;
    int playerBetAmount;
    int cursor; // cursor does not change, since there is only one player revealing TODO make constant

    // TODO: not sure about this now, but consider having the full player class getting used instead of the individual type? 
    // this allows the full capabilites of the player class to be used, but may decrease the encapsulation of using the
    // individual types of players that are designed to handle each round
    public RevealingRound(Player[] players, int playerBetAmount, int cursor) {
        this.players = new RevealingRoundPlayer[players.length];
        for (int i=0; i<players.length; ++i) {
            this.players[i] = players[i].getRevealingRoundPlayer();
        }
        this.playerBetAmount = playerBetAmount;
        this.cursor = cursor;
    }

    public RevealingRound(RevealingRoundPlayer[] players, int playerBetAmount, int cursor) {
        this.players = players;
        this.playerBetAmount = playerBetAmount;
        this.cursor = cursor;
    }
    
    // TODO document, helper for runRound(). Returns if successful
    private boolean flipCards() {
        // flip all of own cards first
        Stack<Card> ownStack = players[cursor].getPlayedCards();
        int initialOwnStackSize = ownStack.size();
        while (!ownStack.empty()) {
            boolean isOwnCardSkull = players[cursor].flip(); // flip the player that needs to guess
            if (isOwnCardSkull) { // if first card was skull, short circuit, otherwise let player start guessing
                return false; 
            }
        }
        // flip other cards at choice until done
        for (int flipCount=0; flipCount<playerBetAmount-initialOwnStackSize; ++flipCount) {
            int decision = queryPlayer(); // TODO does not catch if the player has no cards to flip
            boolean isFlippedSkull = players[decision].flip(); 
            if (isFlippedSkull) { // short circuit exit if a skull is flipped
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
                removalCard = new Card("skull"); // TODO instead of skull and flower card just make a card with field of either skull or flower since there are no methods to differentiate the two
                break;
            case 1:
                removalCard = new Card("flower");
                break;

            default:
                break;
        }
        players[cursor].getHand().removeCard(removalCard);
    }

    // TODO does not cover that the player must flip their OWN card first
    public void runRound() {
        boolean successful = flipCards();
        if (successful) {
            incrementPoints();
        }
        else {
            removeCard();
        }
        // return each players cards back to their hand
        for (RevealingRoundPlayer p: players) {
            p.returnCards();
        }
    }

    private int queryPlayer() {
        return players[cursor].decideFlip(players); // chooseCard adds that card to players stack
    }
}
