package src.game_objects;

import java.util.HashMap;
import java.util.Stack;

public class RevealingRound {
    Player[] players;
    HashMap<String, Integer> nameToIndex = new HashMap<>();
    int playerBetAmount;
    final int cursor; // cursor does not change, since there is only one player revealing
    // TODO make successful a field? Might make more sense

    public RevealingRound(Player[] players, int playerBetAmount, int cursor) {
        this.players = players;
        for (int i=0; i<players.length; ++i) {
            nameToIndex.put(players[i].getName(), i);
        }
        this.playerBetAmount = playerBetAmount;
        this.cursor = cursor;
    }
    
    /**
     * Helper function for the runRound method, returning if flipping the cards of 
     * other players was successful or not to determine if to reward points or remove a card.
     * @return
     */
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

    /**
     * Increments the points for the current player pointed at by the cursor.
     * This player is then given a point (maximum of 2).
     */
    private void incrementPoints() {players[cursor].getRevealingRoundPlayer().incrementPoints();} // TODO kind of weird to use getRevealingRoundPlayer() for this? probably not an issue but slightly weird

    /**
     * Runs the revealing round, assuming players have placed their cards.
     * This is done by revealing the cards chosen by the player than is doing the revealing.
     * This player in the player pointed to by cursor. The player is queried to determine
     * their choies. Then, based on the success of flipping these cards points are rewarded. If not succesful, 
     * the returned value of runRound can be used to force the player object to remove a card.
     * Then, all cards from other players are returned to their hands as they are no longer of use.
     * 
     * @return boolean representing if the player was successful (no skulls) or unsuccesful (skull flipped)
     */
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

    /**
     * Queries the player to decide which choice to make pertaining
     * to the revealing round.
     * @return
     */
    private String queryPlayer() {
        return players[cursor].getRevealingRoundPlayer().decide(players); // chooseCard adds that card to players stack
    }
}
