package src.game_objects;

public class FullRound {
    PlacingRound placingRound;
    BettingRound bettingRound;
    RevealingRound revealingRound;

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
        boolean success = runRevealingRound(bettingPlayerCursor);
        if (!success) {
            // TODO these broken up playes might be kind of clunky. Maybe put all functions within player class that is composition of parts
            players[bettingPlayerCursor].getRemoveRoundPlayer().removeCard(); // removeCard includes decision from player
        }
    }

    public int getNumPlayedCards() {
        int sum = 0;
        for (Player p: players) {
            sum += p.getPlayedCards().size();
        }
        return sum;
    }

    private PlacingRoundPlayer[] getPlacingRoundPlayers() {
        PlacingRoundPlayer[] placingRoundPlayers = new PlacingRoundPlayer[players.length];
        for (int p=0; p<players.length; ++p) {
            placingRoundPlayers[p] = players[p].getPlacingRoundPlayer();
        }
        return placingRoundPlayers;
    }

    private void runPlacingRound() {
        placingRound = new PlacingRound(getPlacingRoundPlayers(), cursor); // TODO can just use normal players now
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
        bettingRound = new BettingRound(getBettingRoundPlayers(), getNumPlayedCards(), cursor); 
        return bettingRound.runRound();
    }

    private RevealingRoundPlayer[] getRevealingRoundPlayers() {
        RevealingRoundPlayer[] revealingRoundPlayers = new RevealingRoundPlayer[players.length];
        for (int p=0; p<players.length; ++p) {
            revealingRoundPlayers[p] = players[p].getRevealingRoundPlayer();
        }
        return revealingRoundPlayers;
    }

    private boolean runRevealingRound(int bettingPlayerCursor) {
        revealingRound = new RevealingRound(getRevealingRoundPlayers(), bettingRound.getBetValue(), bettingPlayerCursor);
        return revealingRound.runRound();
    }


}
