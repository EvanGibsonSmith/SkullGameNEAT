package src.AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import src.game_objects.Game;
import src.game_objects.NEATPlayer;
import src.game_objects.Player;

public class RunPopulationGames {

    private static Set<ArrayList<Player>> playerOrderedCombinations(Set<Player> players, int combinationSize) {
        // base case    
        if (combinationSize==0) {
            HashSet<ArrayList<Player>> set = new HashSet<>();
            set.add(new ArrayList<>());
            return set;
        }

        // recursive step
        Set<ArrayList<Player>> outSet = new HashSet<>();
        for (Player p: players) {
            for (ArrayList<Player> perm: playerOrderedCombinations(players, combinationSize-1)) {
                if (!perm.contains(p)) { // TODO doing this on an ArrayList can be kind of slow for a large number of players per game
                    perm.addFirst(p); // could add last as well, but makes more intuitive sense to me first
                    outSet.add(perm); 
                }
            }
        }

        return outSet;
    }

    private static <T> Map<Integer, T> setToMap(Set<T> set) {
        HashMap<Integer, T> map = new HashMap<>();
        int count = 0;
        for (T element: set) {
            map.put(count, element);
            ++count;
        }
        return map;
    }

    private static ArrayList<Player> getRandomCombination(Random r, Player[] players, int combinationSize) {
        ArrayList<Player> combination = new ArrayList<>();
        Player[] playersClone = players.clone();

        int playersCloneSize = playersClone.length;
        for (int i=0; i<combinationSize; ++i) {
            int nextPlayerIdx = r.nextInt(playersCloneSize);
            Player nextPlayer = playersClone[nextPlayerIdx];
            // swap nextPlayer with end element, and decrement size to "remove" nextPlayer from reselection
            playersClone[nextPlayerIdx] = playersClone[playersCloneSize-1]; // end index now randomly selected one
            combination.add(nextPlayer);
            --playersCloneSize;
        }
        return combination;
    }

    public static void main(String[] args) {
        if (args.length!=4) {
            System.out.println("Usage: [numGenomes] [numPlayersPerGame] [numGames] [seed]");
            System.exit(2);
        }
        int numGenomes = Integer.parseInt(args[0]);
        int numPlayersPerGame = Integer.parseInt(args[1]);
        int numGames = Integer.parseInt(args[2]);
        Random r = new Random(Integer.parseInt(args[3]));

        if (numPlayersPerGame>numGenomes) {
            System.out.println("Must have more genomes (players in pool) than number of players per game");
            System.exit(2);
        }

        Player[] players = new Player[numGenomes];
        // add new NEAT players for each genomes
        for (int i=0; i<players.length; ++i) {
            players[i] = new NEATPlayer(Integer.toString(i+1), i+1);
        }

        System.out.println("Beginning games");
        ArrayList<Game> games = new ArrayList<>();
        Set<ArrayList<String>> playedCombinations = new HashSet<>();
        ArrayList<String> playerNames = new ArrayList<>();
        for (int gameNum=0; gameNum<numGames; ++gameNum) {
            System.out.println("Game Num: " + gameNum);
            Player[] thisGamePlayers;
            do { // get unique new players
                playerNames.clear();
                thisGamePlayers = getRandomCombination(r, players, numPlayersPerGame).toArray(new Player[0]);
                for (Player p: thisGamePlayers) {
                    playerNames.add(p.getName());
                }
            }
            while (playedCombinations.contains(playerNames));

            // add this ordering of names to HashSet
            playedCombinations.add(playerNames);
            Game game = new Game(thisGamePlayers); // play game with those participants
            game.runGame();
            games.add(game);
        }

        // collect fitnesses of players
        System.out.println("Fitnesses");
        double[] fitnesses = new double[players.length];
        for (int i=0; i<players.length; ++i) {
            fitnesses[i] = ((NEATPlayer) players[i]).getFitness(); // note that this also is in order of genome (starting with 1)
        }

        // Send back information about performance of each genome
        String fitnessesString = "";
        for (int i=0; i<players.length; ++i) {
            fitnessesString += fitnesses[i] + ", ";
        }
        fitnessesString = "[" + fitnessesString.substring(0, fitnessesString.length()-2) + "]";
        System.out.println(fitnessesString);
    }
}