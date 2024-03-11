package src.game_objects;

import java.util.Stack;

public class Game {
    int startingPlayerCursor;
    Player[] players;
    int winnerCursor = -1;
    Stack<FullRound> rounds = new Stack<>(); // NOTE: we might not technically need to track rounds, but they are nice to have

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

    public void runGame() {
        while (!isWinner()) {
            FullRound newRound = new FullRound(players, startingPlayerCursor);
            newRound.runRound();
            rounds.add(newRound); // stores information for this round if needed
        }
        winnerCursor = getWinnerCursor();
    }
}
