package src.game_objects;

public class BettingRound {
    Player[] players;
    int maxBet; // number of cards on table
    int currBet = 0;
    int cursor; 

    public BettingRound(Player[] players, int maxBet, int cursor) {
        this.players = players;
        this.maxBet = maxBet;
        this.cursor = cursor;
    }

    public int getBetValue() {
        return this.currBet;
    }

    private void incrementCursor() {
        cursor = (cursor + 1) % players.length;
    }

    public int getCursor() {
        return this.cursor;
    }

    public int runRound() {
        int lastRaisingPlayer = -1; // keep track so when round ends we know who must guess
        boolean[] playersIn = new boolean[players.length];
        int numPlayersIn = players.length;
        for (int i=0; i<players.length; ++i) {
            playersIn[i] = true;
        }

        // force first player to bet a value
        System.out.println("Player " + players[cursor].getName());
        // if bet is not above the size of the played cards of a player, they must bet at least the number of cards they have played
        players[cursor].getBettingRoundPlayer().decide(true, Math.max(currBet, players[cursor].getPlayedCards().size()-1), maxBet); 
        lastRaisingPlayer = cursor; // set this as lastRaisingPlayer (will always occur since first player must bet)
        currBet = players[cursor].getBettingRoundPlayer().getBet();
        incrementCursor();

        // if cursor==lastRaisingPlayer then we have looped without another raise
        while (cursor!=lastRaisingPlayer && numPlayersIn!=1 && currBet!=maxBet) { 
            if (playersIn[cursor]==false) { // skip player if not in betting anymore
                continue;
            }
            System.out.println("Player " + players[cursor].getName());
            boolean decision = queryPlayer(); // current bet is minimum, number of players max
            if (decision==false) { // could shorten to playersIn[cursor]=decision
                playersIn[cursor] = false;
                --numPlayersIn; // subtract number of players left
            }
            else {
                lastRaisingPlayer = cursor; // this player betted so update last raising player
                currBet = players[cursor].getBettingRoundPlayer().getBet(); // update currentBet value
            }
            incrementCursor();
        }
        return lastRaisingPlayer;
    }

    public boolean queryPlayer() {
        return players[cursor].getBettingRoundPlayer().decide(false, Math.max(currBet, players[cursor].getPlayedCards().size()-1), maxBet);
    }
}
