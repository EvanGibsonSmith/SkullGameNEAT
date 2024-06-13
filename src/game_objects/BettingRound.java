package src.game_objects;

public class BettingRound {
    Player[] players;
    Player[] activePlayers;
    int maxBet; // number of cards on table
    int currBet = 0;
    int cursor; 

    public BettingRound(Player[] players, int maxBet, int cursor) {
        this.players = players;
        this.activePlayers = Game.getActivePlayers(players);
        this.maxBet = maxBet;
        this.cursor = cursor;
    }

    public int getBetValue() {
        return this.currBet;
    }

    private void incrementCursor() {
        cursor = (cursor + 1) % activePlayers.length;
    }

    public int getCursor() {
        return this.cursor;
    }

    public int runRound() {
        int lastRaisingPlayer = -1; // keep track so when round ends we know who must guess
        boolean[] playersIn = new boolean[activePlayers.length];
        int numPlayersIn = activePlayers.length;
        for (int i=0; i<activePlayers.length; ++i) {
            playersIn[i] = true;
        }

        // if bet is not above the size of the played cards of a player, they must bet at least the number of cards they have played
        activePlayers[cursor].getBettingRoundPlayer().decide(players, true, Math.max(currBet, activePlayers[cursor].getPlayedCards().size()-1), maxBet); 
        lastRaisingPlayer = cursor; // set this as lastRaisingPlayer (will always occur since first player must bet)
        currBet = activePlayers[cursor].getBettingRoundPlayer().getBet();
        incrementCursor();

        // if cursor==lastRaisingPlayer then we have looped without another raise
        while (cursor!=lastRaisingPlayer && numPlayersIn!=1 && currBet!=maxBet) { 
            if (playersIn[cursor]==false) { // skip player if not in betting anymore
                continue;
            }
            boolean decision = queryPlayer(); // current bet is minimum, number of players max
            if (decision==false) { // could shorten to playersIn[cursor]=decision
                playersIn[cursor] = false;
                --numPlayersIn; // subtract number of players left
            }
            else {
                lastRaisingPlayer = cursor; // this player betted so update last raising player
                currBet = activePlayers[cursor].getBettingRoundPlayer().getBet(); // update currentBet value
            }
            incrementCursor();
        }

        // set all bets back to -1
        for (Player player: activePlayers) {
            player.getBettingRoundPlayer().bet = -1; // TODO could make this set bet or something instead?
        }

        return lastRaisingPlayer;
    }

    // TODO add a check to make sure the decision is valid?
    public boolean queryPlayer() {
        return activePlayers[cursor].getBettingRoundPlayer().decide(players, false, Math.max(currBet, activePlayers[cursor].getPlayedCards().size()-1), maxBet);
    }
}
