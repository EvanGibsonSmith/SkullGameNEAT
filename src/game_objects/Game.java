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

    // TODO document gets the number of active players to build active players array
    private int numActivePlayers() {
        int numActive = 0;
        for (Player p: players) {
            if (!p.getHand().empty()) {
                ++numActive;
            }
        }
        return numActive;
    }

    // TODO document, but creates a list of active players with no gaps for next round
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

    public void runGame() {
        while (!isWinner()) {
            FullRound newRound = new FullRound(activePlayers, startingPlayerCursor);
            newRound.runRound();
            rounds.add(newRound); // stores information for this round if needed
            rebuildActivePlayers();
        }
        setWinner();
    }
}
