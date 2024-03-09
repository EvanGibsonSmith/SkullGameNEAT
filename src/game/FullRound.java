public class FullRound {
    PlacingRound placingRound;
    BettingRound bettingRound;

    // TODO below needed as instance variables? Probably, it makes sense through OO logic but look back when not jet lagged
    Player[] players;
    int cursor;

    public FullRound(Player[] players, int startingPlayerCursor) {
        this.players = players;
        this.cursor = startingPlayerCursor; // initial cursor to start placing round
    }
    
    public void runRound() {
        runPlacingRound(); // each of these sets up player variables properly
        int bettingPlayerCursor = runBettingRound(); // need output to know which player is flipping for point
        runRevealingRound(bettingPlayerCursor);
    }

    private PlacingRoundPlayer[] getPlacingRoundPlayers() {
        PlacingRoundPlayer[] placingRoundPlayers = new PlacingRoundPlayer[players.length];
        for (int p=0; p<players.length; ++p) {
            placingRoundPlayers[p] = players[p].getPlacingRoundPlayer();
        }
        return placingRoundPlayers;
    }

    private void runPlacingRound() {
        PlacingRound placingRound = new PlacingRound(getPlacingRoundPlayers(), cursor);
        placingRound.runRound(); // updates proper player variables
        cursor = placingRound.getCursor(); // update cursor field for bettingRound
    }

    private BettingRoundPlayer[] getBettingRoundPlayers() {
        BettingRoundPlayer[] bettingRoundPlayers = new BettingRoundPlayer[players.length];
        for (int p=0; p<players.length; ++p) {
            bettingRoundPlayers[p] = players[p].getBettingRoundPlayer();
        }
        return bettingRoundPlayers;
    }

    private int runBettingRound() {
        BettingRound bettingRound = new BettingRound(getBettingRoundPlayers(), cursor);
        return bettingRound.runRound();
    }

    private RevealingRoundPlayer[] getRevealingRoundPlayers() {
        RevealingRoundPlayer[] revealingRoundPlayers = new RevealingRoundPlayer[players.length];
        for (int p=0; p<players.length; ++p) {
            revealingRoundPlayers[p] = players[p].getRevealingRoundPlayer();
        }
        return revealingRoundPlayers;
    }

    private void runRevealingRound(int bettingPlayerCursor) {
        RevealingRound revealingRound = new RevealingRound(getRevealingRoundPlayers(), bettingPlayerCursor, cursor);
        revealingRound.runRound();
    }


}
