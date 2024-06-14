package src.game_objects;

import src.AI.NEATFunctions;

public class RecurrentNEATGame extends Game {
    
    public RecurrentNEATGame(Player[] players) {
        super(players);
    }

    public RecurrentNEATGame(Player[] players, int startingPlayerCursor) {
        super(players, startingPlayerCursor);
    }

    @Override
    // Sets a request to reset to socket
    public void runGame() {
        super.runGame(); // run game, but just send extra reset request
        NEATFunctions.sendSocketReset();
    }
}
