package src.game_objects;

import java.util.Stack;

public class Game {
    int startingPlayerCursor;
    Player[] players; // total players of the game, regardless of out or not
    Player[] activePlayers; // players stil within the game. 
    int winnerCursor = -1;
    Stack<FullRound> rounds = new Stack<>(); // NOTE: we might not technically need to track rounds, but they are nice to have for info

    public Game(Player[] players, int startingPlayerCursor) {
        this.players = players;
        this.startingPlayerCursor = startingPlayerCursor;
    }

    public int getWinnerCursor() {
        for (int i=0; i<players.length; ++i) {
            Player p = players[i];
            if (p.getPoints()>=2) {
                return i;
            }
        }
        return -1;
    }

    public boolean isWinner() {
        for (Player p: players) {
            if (p.getPoints()>=2) {
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
            if (player.getHand().empty()) { // add player to newActivePlayers
                activePlayers[newActivePlayersCursor] = player;
                ++newActivePlayersCursor;
            }
        }
    }

    public void runGame() {
        while (!isWinner()) {
            FullRound newRound = new FullRound(players, startingPlayerCursor);
            newRound.runRound();
            rounds.add(newRound); // stores information for this round if needed
            rebuildActivePlayers();
        }
        winnerCursor = getWinnerCursor();
    }
}
