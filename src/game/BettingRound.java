public class BettingRound {
    BettingRoundPlayer[] players;
    int maxBet;
    int cursor;

    public BettingRound(BettingRoundPlayer[] players, int cursor) {
        this.players = players;
        this.maxBet = -1;
        this.cursor = cursor;

        // TODO sanity check to make sure if cursor if within player range if I'm feeling benevolent?
    }

    public int runRound() {
        int lastRaisingPlayer = -1;
        int currentBet = -1;
        boolean[] playersIn = new boolean[players.length];
        for (int i=0; i<players.length; ++i) {
            playersIn[i] = true;
        }
        while (cursor!=lastRaisingPlayer) { // if cursor=lastRaisingPlayer then we have looped without another raise
            if (playersIn[cursor]==false) { // skip player if not in
                continue;
            }
            boolean decision = players[cursor].decide(currentBet, players.length); // current bet is minimum, number of players max
            if (decision==false) { // could shorten to playersIn[cursor]=decision
                playersIn[cursor] = false;
            }
            else {
                lastRaisingPlayer = cursor;
            }
            ++cursor;
        }
        return lastRaisingPlayer;
    }

    public void queryPlayer() {
        players[cursor].decide(maxBet, players.length);
    }
}
