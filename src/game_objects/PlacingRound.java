package src.game_objects;

public class PlacingRound {
    int cursor = -1;
    Player[] players;

    public PlacingRound(Player[] players, int startingPlayer) {
        this.players = players;
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
        cursor = (cursor + 1) % players.length;
    }

    /**
     * Decrements the cursor from this player to the previous, looping
     * around the player array as needed.
     * 
     * @return void, increments internal cursor
     */
    private void decrementCursor() {
        cursor = (cursor + (players.length-1)) % players.length; // loops around to decrement
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
        boolean placingRoundOverFlag = false; // raised when player decides, players cannot choose invalid options, so this is safe
        while (!placingRoundOverFlag) {
            System.out.println("Player " + players[cursor].getName());
            placingRoundOverFlag = queryPlayer(); // new player does turn
            incrementCursor(); // move to next player
        }
        decrementCursor(); // decrement cursor it points to final placement
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
        for (Player p: players) {
            PlacingRoundPlayer placingRoundPlayer = p.getPlacingRoundPlayer();
            if (placingRoundPlayer.getStackSize()==0) { // if any player has no cards played, cannot end round, otherwise yes
                return false;
            }
        }
        return true;
    }
    
    /**
     * 
     * @return
     */
    private boolean queryPlayer() {
        return players[cursor].getPlacingRoundPlayer().decide(canEndPlacingRound()); // chooseCard adds that card to players stack
    }
}
