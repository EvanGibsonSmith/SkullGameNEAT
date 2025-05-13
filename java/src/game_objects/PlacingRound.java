package src.game_objects;

public class PlacingRound {
    int cursor = -1; // cursor is cursor within array of active players
    Player[] players;
    Player[] activePlayers;

    public PlacingRound(Player[] players, int startingPlayer) {
        this.players = players;
        this.activePlayers = Game.getActivePlayers(players);
        this.cursor = startingPlayer;
    }

    public int getCursor() {return cursor;}

    /**
     * Increments the cursor from this player to the next, looping
     * around the player array as needed.
     * 
     * @returns void, increments internal cursor
     */
    private void incrementCursor() {
        cursor = (cursor + 1) % activePlayers.length;
    }

    /**
     * Decrements the cursor from this player to the previous, looping
     * around the player array as needed.
     * 
     * @return void, increments internal cursor
     */
    private void decrementCursor() {
        cursor = (cursor + (activePlayers.length-1)) % activePlayers.length; // loops around to decrement
    }

    /**
     * Runs the placing round, looping through the players until 
     * everybody has finished placing cards. This is done by querying the player objects
     * for their decisions, keeping track of if we are done yet. 
     * 
     * Assumes that the players have not placed any cards already (each of their stacks are empty).
     * 
     * @return void but configures the player objects instance variables of placing round
     */
    public void runRound() {
        int decision = -1; // decision is which card to place, or if 2, to exit
        while (true) { // breaks when decision is 2
            decision = queryPlayer(); // new player does turn
            if (decision==2) {
                break; // notice this skips incrementing cursor, so that it remains on this player for betting round
            }
            activePlayers[cursor].getPlacingRoundPlayer().placeCard(decision); // place correct card type
            incrementCursor(); // move to next player
        }
    }

    /**
     * If the current player pointed to by the cursor (or technically any player, since this is independent of the cursor) 
     * can end the round and move to the betting round. Each player must have played at least one card, after this,
     * any following player can move into the betting round, and may be forced to if they
     * do not have any more cards to stack. While this is an individual player option
     * handled in the PlacingRoundPlayer class, specifically decided upon in the decide function,
     * the player object does not have the scope to determine if they allowed to do this.
     * Thus, this is passed into the decide function to provide the player that information.
     * @return boolean, if the player cursor points to has the ability to shift into the betting round
     */
    public boolean canEndPlacingRound() {
        for (Player p: activePlayers) {
            PlacingRoundPlayer placingRoundPlayer = p.getPlacingRoundPlayer();
            if (placingRoundPlayer.getStackSize()==0) { // if any player has no cards played, cannot end round, otherwise yes
                return false;
            }
        }
        return true;
    }
    
    /**
     * Queries the player at the current cursor location for a decision
     * @return
     */
    private int queryPlayer() {
        return activePlayers[cursor].getPlacingRoundPlayer().decide(players, canEndPlacingRound()); // chooseCard adds that card to players stack
    }
}
