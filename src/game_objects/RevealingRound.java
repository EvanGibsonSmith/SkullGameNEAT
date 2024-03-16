package src.game_objects;

import java.util.HashMap;
import java.util.Stack;

public class RevealingRound {
    Player[] players;
    HashMap<String, Integer> nameToIndex = new HashMap<>();
    int playerBetAmount;
    int cursor; // cursor does not change, since there is only one player revealing TODO make constant
    // TODO make successful a field? Might make more sense

    public RevealingRound(Player[] players, int playerBetAmount, int cursor) {
        this.players = players;
        for (int i=0; i<players.length; ++i) {
            nameToIndex.put(players[i].getName(), i);
        }
        this.playerBetAmount = playerBetAmount;
        this.cursor = cursor;
    }
    
    // TODO document, helper for runRound(). Returns if successful
    private boolean flipCards() {
        // flip all of own cards first
        Stack<Card> ownStack = players[cursor].getPlayedCards();
        int initialOwnStackSize = ownStack.size();
        while (!ownStack.empty()) {
            boolean isOwnCardSkull = players[cursor].getRevealingRoundPlayer().flip(); // flip the player that needs to guess
            if (isOwnCardSkull) { // if first card was skull, short circuit, otherwise let player start guessing
                return false; 
            }
        }
        // flip other cards at choice until done
        for (int flipCount=0; flipCount<playerBetAmount-initialOwnStackSize; ++flipCount) {
            String decision = queryPlayer(); // TODO does not catch if the player has no cards to flip
            int decisionIndex = nameToIndex.get(decision);
            boolean isFlippedSkull = players[decisionIndex].getRevealingRoundPlayer().flip(); 
            if (isFlippedSkull) { // short circuit exit if a skull is flipped
                return false;
            }
        }
        return true;
    }

    // TODO document, potentially, helper for runRound(), ran when succesful
    private void incrementPoints() {players[cursor].getRevealingRoundPlayer().incrementPoints();}

    // TODO does not cover that the player must flip their OWN card first
    public boolean runRound() {
        boolean successful = flipCards();
        if (successful) {
            incrementPoints();
        }
        // return cards to players
        for (Player p: players) {
            p.getRevealingRoundPlayer().returnCards();
        }
        return successful;
    }

    private String queryPlayer() {
        return players[cursor].getRevealingRoundPlayer().decide(players); // chooseCard adds that card to players stack
    }
}
