package src.game_objects;

import java.util.Random;
import java.util.Stack;

public class Game {
    int startingPlayerCursor = 0;
    final Player[] players; // total players of the game, regardless of out or not
    Player[] activePlayers; // players stil within the game. 
    Player winner;
    Stack<FullRound> rounds = new Stack<>(); // might not technically need to track rounds, but they are nice to have for info

    public Game(Player[] players) {
        this.players = players;
        this.activePlayers = players;
        Random r = new Random();
        this.startingPlayerCursor = r.nextInt(players.length); // start on random player
    }

    public Game(Player[] players, int startingPlayerCursor) {
        this(players);
        this.startingPlayerCursor = startingPlayerCursor;
    }

    public Player getWinner() {
        return winner;
    }

    public Player[] getPlayers() {return this.players;}

    private void setWinner() {
        for (Player p: players) { // technically speaking, could probably iterate through ActivePlayers instead
            if (p.getPoints()>=2) { // to have 2 points should already be active player
                winner = p;
            }
            else if (activePlayers.length==1) {
                winner = activePlayers[0];
            }
        }
    }

    public boolean isWinner() {
        for (Player p: players) {
            if (p.getPoints()>=2) {
                return true;
            }
            else if (activePlayers.length==1) { // if only one player left, win by default
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the number of active players (ones that haven't lost) still in the game.
     * This is to say players with non empty hands.
     * 
     * @return int of numActivePlayers
     */
    public static int numActivePlayers(Player[] players) {
        int numActive = 0;
        for (Player p: players) {
            if (!(p.getHand().empty() & p.getPlayedCards().empty())) {
                ++numActive;
            }
        }
        return numActive;
    }

    /**
     * Rebuilds the active players array, removing players that are inactive. This new array 
     * has no gaps of inactive players. Each individual player object may be in a new index within the 
     * array because of the removal of players who now have empty hands (are inactive). 
     * Naturally, the ordering is still correct.
     * 
     * @return void, but sets the activePlayers for this class to the proper players.
     */
    public static Player[] getActivePlayers(Player[] players) {
        // build active player
        Player[] activePlayers = new Player[numActivePlayers(players)];
        int newActivePlayersCursor = 0;
        for (int i=0; i<players.length; ++i) { // don't need to consider already inactive players
            Player player = players[i];
            if (!(player.getHand().empty() & player.getPlayedCards().empty())) { // add player to newActivePlayers if they have cards
                activePlayers[newActivePlayersCursor] = player;
                ++newActivePlayersCursor;
            }
        }
        return activePlayers;
    }

    /**
     * Runs the skull game, running through each of the rounds. 
     * These rounds are added to the instance variable rounds.
     * 
     * @returns void but updates the rounds and player objects accordingly
     */
    public void runGame() {
        for (Player p: players) {
            p.resetCards(); // set up player hand and points for this game
        }

        while (!isWinner()) {
            FullRound newRound = new FullRound(players, startingPlayerCursor); // NOTE: round gets all players, but really only works with activePlayers
            newRound.runRound();
            startingPlayerCursor = newRound.getCursor(); // get the new starting player for next round
            rounds.add(newRound); // stores information for this round if needed
            this.activePlayers = getActivePlayers(players);
            // EDGE CASE: if starting cursor was final player and they were removed, set back to zero for next player to play
            // otherwise, the next cursor position correctly moves to the next player
            if (startingPlayerCursor==activePlayers.length) { // if at final person, shift to 0 for player to left to play next
                startingPlayerCursor = 0;
            }
        }
        setWinner();
        getWinner().incrementWonGames(); // add a won game to the winning player
        // add played game to all players
        for (Player p: players) {
            p.incrementGamesPlayed();
        }
    }

    /**
     * @param args Usage is numPlayers, player1Name, player2Name, player3Name, playerNName, type (either terminal or NEAT)
     */
    public static void main(String[] args) {
        // Ran game for terminal, now using this to run NEAT setup
        /*if (args.length==0) {
            System.out.println("Usage: <numPlayers> [player1Name] [player2Name] [player3Name] [playerName] [type]");
            System.exit(1);
        }
        int numPlayers = Integer.parseInt(args[0]);
        Player[] players = new Player[numPlayers];
        String type = "terminal"; // default
        if (args.length==2+numPlayers || args.length==2) { // in these cases, type has been included
            type = args[args.length-1];
        }

        if (args.length==1 || args.length==2) { // if no player names given
            for (int i=0; i<numPlayers; ++i) {
                players[i] = new TerminalPlayer("Player" + i);
            }
        }
        else if (args.length==2+numPlayers || args.length==1+numPlayers) { // if each player given a name
            for (int i=0; i<numPlayers; ++i) {
                players[i] = new TerminalPlayer(args[i+1]);
                System.out.println(players[i]);
            }
        }
        else {
            System.out.println("Usage: <numPlayers> [player1Name] [player2Name] [player3Name] [playerName] [type]");
            System.exit(1);
        }*/
        
        // setup NEAT players
        if (args.length==0) {
            System.out.println("Usage: <numPlayers> <player1Name> <player1GenomeID> ... <playerNName> <playerNGenomeID>");
            System.exit(2);
        }
        int numPlayers = Integer.parseInt(args[0]);
        if (args.length!=numPlayers*2+1) {
            System.out.println("Usage: <numPlayers> <player1Name> <player1GenomeID> ... <playerNName> <playerNGenomeID>");
            System.exit(3);
        }
        
        Player[] players = new Player[numPlayers];
        for (int i=0; i<numPlayers; ++i) {
            players[i] = new NEATPlayer(args[i*2+1], Integer.parseInt(args[i*2+2])); // Gets players in format give (name then id after numPlayers)
        }

        // run game
        Game game = new Game(players);
        game.runGame();
        // add game to winner
    }
}
