package src.game_objects;

public class PlacingRound {
    int cursor = -1;
    PlacingRoundPlayer[] players;

    public PlacingRound(Player[] players, int startingPlayer) {
        this.players = new PlacingRoundPlayer[players.length];
        for (int i=0; i<players.length; ++i) {
            this.players[i] = players[i].getPlacingRoundPlayer();
        }
        this.cursor = startingPlayer;
    }
    
    public PlacingRound(PlacingRoundPlayer[] players, int startingPlayer) {
        this.players = players;
        this.cursor = startingPlayer;

        // TODO sanity check to make sure if cursor if within player range if I'm feeling benevolent?
    }
    
    public int getCursor() {return cursor;}

    // TODO document? Simple but could probably use it
    private void incrementCursor() {
        cursor = (cursor + 1) % players.length;
    }

    // TODO complete me (should this be automatically  fonr within the constructor)
    public void runRound() {
        // start with starting player and then cycle.
        // initially, all stacks should be empty (this is assumed) TODO document this
        boolean placingRoundOverFlag = false; // raised when player decides, players cannot choose invalid options, so this is safe
        while (!placingRoundOverFlag) {
            System.out.println("Player " + cursor);
            placingRoundOverFlag = queryPlayer(); // new player does turn
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
        for (PlacingRoundPlayer p: players) {
            if (p.getStackSize()==0) { // if any player has no cards played, cannot end round, otherwise yes
                return false;
            }
        }
        return true;
    }
    
    // TODO document. Returns flag if PlacingRound is over.
    private boolean queryPlayer() {
        return players[cursor].decide(canEndPlacingRound()); // chooseCard adds that card to players stack
    }
}