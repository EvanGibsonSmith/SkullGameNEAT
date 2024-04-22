package src.game_objects;

import java.util.Stack;

public class Game {
    int startingPlayerCursor;
    Player[] players; // total players of the game, regardless of out or not
    Player[] activePlayers; // players stil within the game. 
    Player winner;
    Stack<FullRound> rounds = new Stack<>(); // might not technically need to track rounds, but they are nice to have for info

    public Game(Player[] players, int startingPlayerCursor) {
        this.players = players;
        this.activePlayers = players;
        this.startingPlayerCursor = startingPlayerCursor;
    }

    public Player getWinner() {
        return winner;
    }

    private void setWinner() {
        for (Player p: players) { // technically speaking, could probably iterate through ActivePlayers instead
            if (p.getPoints()>=2) { // to have 2 points should already be active player
                winner = p;
            }
            // if only one player left they win be default.
            // Technically speaking, this rule is not needed because the player 
            // could play a flower, then win following betting round. 
            // NOTE: Potentially, removing this rule could litmus test for AI strategy for 1 player games
            if (activePlayers.length==1) {
                winner = activePlayers[0];
            }
        }
    }

    public boolean isWinner() {
        for (Player p: players) {
            if (p.getPoints()>=2) {
                return true;
            }
            if (activePlayers.length==1) { // if only one player left, win by default
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the number of active players (ones that haven't lost) still in the game.
     * This is to say players with non empty hands.
     * 
     * @return int of numActivePlayers
     */
    private int numActivePlayers() {
        int numActive = 0;
        for (Player p: players) {
            if (!p.getHand().empty()) {
                ++numActive;
            }
        }
        return numActive;
    }

    /**
     * Rebuilds the active players array, removing players that are inactive. This new array 
     * has no gaps of inactive players. Each individual player object may be in a new index within the 
     * array because of the removal of players who now have empty hands (are inactive). 
     * Naturally, the ordering is still correct.
     * 
     * @return void, but sets the activePlayers for this class to the proper players.
     */
    private void rebuildActivePlayers() {
        // build active player
        this.activePlayers = new Player[numActivePlayers()];
        int newActivePlayersCursor = 0;
        for (int i=0; i<players.length; ++i) { // don't need to consider already inactive players
            Player player = players[i];
            if (!player.getHand().empty()) { // add player to newActivePlayers
                activePlayers[newActivePlayersCursor] = player;
                ++newActivePlayersCursor;
            }
        }
    }

    /**
     * Runs the skull game, running through each of the rounds. 
     * These rounds are added to the instance variable rounds.
     * 
     * @returns void but updates the rounds and player objects accordingly
     */
    public void runGame() {
        while (!isWinner()) {
            FullRound newRound = new FullRound(activePlayers, startingPlayerCursor);
            newRound.runRound();
            startingPlayerCursor = newRound.getCursor(); // get the new starting player for next round
            rounds.add(newRound); // stores information for this round if needed
            rebuildActivePlayers();
        }
        setWinner();
    }
}
