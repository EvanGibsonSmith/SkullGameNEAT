package src.game_objects;

public class BettingRound {
    BettingRoundPlayer[] players;
    int maxBet; // number of cards on table
    int currBet = 0;
    int cursor;

    public BettingRound(Player[] players, int maxBet, int cursor) {
        this.players = new BettingRoundPlayer[players.length];
        for (int i=0; i<players.length; ++i) {
            this.players[i] = players[i].getBettingRoundPlayer();
        }
        this.maxBet = maxBet;
        this.cursor = cursor;
    }

    public BettingRound(BettingRoundPlayer[] players, int maxBet, int cursor) {
        this.players = players;
        this.maxBet = maxBet;
        this.cursor = cursor;

        // TODO sanity check to make sure if cursor if within player range if I'm feeling benevolent?
    }

    public int getBetValue() {
        return this.currBet;
    }

    private void incrementCursor() {
        cursor = (cursor + 1) % players.length;
    }

    public int runRound() {
        int lastRaisingPlayer = -1; // keep track so when round ends we know who must guess
        boolean[] playersIn = new boolean[players.length];
        int numPlayersIn = players.length;
        for (int i=0; i<players.length; ++i) {
            playersIn[i] = true;
        }

        // force first player to bet a value
        System.out.println("Player " + cursor);
        players[cursor].decide(true, currBet, maxBet); 
        lastRaisingPlayer = cursor; // set this as lastRaisingPlayer (will always occur since first player must bet)
        currBet = players[cursor].getBet();
        incrementCursor();

        // if cursor==lastRaisingPlayer then we have looped without another raise
        while (cursor!=lastRaisingPlayer && numPlayersIn!=1 && currBet!=maxBet) { 
            if (playersIn[cursor]==false) { // skip player if not in betting anymore
                continue;
            }
            System.out.println("Player " + cursor);
            boolean decision = queryPlayer(); // current bet is minimum, number of players max
            if (decision==false) { // could shorten to playersIn[cursor]=decision
                playersIn[cursor] = false;
                --numPlayersIn; // subtract number of players left
            }
            else {
                lastRaisingPlayer = cursor; // this player betted so update last raising player
                currBet = players[cursor].getBet(); // update currentBet value
            }
            incrementCursor();
        }
        return lastRaisingPlayer; // TODO consider having this be a field in the class that can then be gotten with a getter
    }

    public boolean queryPlayer() {
        return players[cursor].decide(false, currBet, maxBet);
    }
}
