package src.game_objects;

public class FullRound {
    PlacingRound placingRound;
    BettingRound bettingRound;
    RevealingRound revealingRound;
    Player[] players;
    int cursor;

    public FullRound(Player[] players, int startingPlayerCursor) {
        this.players = players;
        this.cursor = startingPlayerCursor; // initial cursor to start placing round
    }

    public int getCursor() {
        return this.cursor;
    }
    
    public void runRound() {
        runPlacingRound(); // each of these sets up player variables properly
        runBettingRound(); // need updated cursor to know which player is flipping for point
        boolean success = runRevealingRound(); // uses cursor from bettingRound to be on proper player
        if (!success) {
            players[cursor].getRemoveRoundPlayer().removeCard(); // removeCard includes decision from player
        }
    }

    public int getNumPlayedCards() {
        int sum = 0;
        for (Player p: players) {
            sum += p.getPlayedCards().size();
        }
        return sum;
    }

    private void runPlacingRound() {
        placingRound = new PlacingRound(players, cursor);
        placingRound.runRound(); // updates proper player variables
        cursor = placingRound.getCursor(); // update cursor field for bettingRound
    }

    private void runBettingRound() {
        bettingRound = new BettingRound(players, getNumPlayedCards(), cursor); 
        cursor = bettingRound.runRound();
    }

    private boolean runRevealingRound() {
        revealingRound = new RevealingRound(players, bettingRound.getBetValue(), cursor);
        return revealingRound.runRound(); // cursor doesn't need to change since revealing round is just on one player
    }


}
