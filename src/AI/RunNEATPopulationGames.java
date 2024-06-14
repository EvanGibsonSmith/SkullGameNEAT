package src.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import src.game_objects.Game;
import src.game_objects.NEATPlayer;
import src.game_objects.Player;
import src.game_objects.RecurrentNEATGame;

public class RunNEATPopulationGames {

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

    private static int[] parseGenomesString(String genomesString) {
        // parse genomes string
        String[] genomesArray = genomesString.substring(1, genomesString.length()-1).split(",");
        int[] genomeIDs = new int[genomesArray.length];
        for (int i=0; i<genomesArray.length; ++i) {
            String genomeIDString = genomesArray[i];
            genomeIDs[i] = Integer.parseInt(genomeIDString.strip());
        }
        return genomeIDs;
    }

    public static void main(String[] args) {
        if (args.length!=4) {
            System.out.println("Usage: [genomes] [numPlayersPerGame] [numGames] [seed]");
            System.exit(2);
        }
        int[] genomes = parseGenomesString(args[0]);
        int numPlayersPerGame = Integer.parseInt(args[1]);
        int numGames = Integer.parseInt(args[2]);
        Random r = new Random(Integer.parseInt(args[3]));

        if (numPlayersPerGame>genomes.length) {
            System.out.println("Must have more genomes (players in pool) than number of players per game");
            System.exit(2);
        }

        NEATPlayer[] players = new NEATPlayer[genomes.length];
        // add new NEAT players for each genomes
        for (int i=0; i<players.length; ++i) {
            players[i] = new NEATPlayer(Integer.toString(genomes[i]), genomes[i]);
        }

        ArrayList<Game> games = new ArrayList<>();
        Set<ArrayList<String>> playedCombinations = new HashSet<>();
        ArrayList<String> playerNames = new ArrayList<>();
        for (int gameNum=0; gameNum<numGames; ++gameNum) {
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
            Game game = new RecurrentNEATGame(thisGamePlayers); // play game with those participants
            game.runGame();
            games.add(game);
        }

        // collect fitnesses of players
        String jsonString = ""; // represents fitness for each player after this game
        for (NEATPlayer p: players) {
            jsonString += String.format("\"%d\": %f, ", p.getGenomeID(), p.getFitness());
        }
        jsonString = "{" + jsonString.substring(0, jsonString.length()-2) + "}";
        System.out.println(jsonString);
    }
}