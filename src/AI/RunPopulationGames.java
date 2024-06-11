package src.AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import src.game_objects.Game;
import src.game_objects.NEATPlayer;
import src.game_objects.Player;

public class RunPopulationGames {

    public static void main(String[] args) {
        int numGenomes = Integer.parseInt(args[0]);
        Player[] players = new Player[numGenomes];
        // add new NEAT players for each genomes
        for (int i=0; i<players.length; ++i) {
            players[i] = new NEATPlayer(Integer.toString(i+1), i+1);
        }

        ArrayList<Game> games = new ArrayList<>();
        // TODO take every possible combination of 2 player games, with each starting player so 19800
        for (Player p1: players) {
            for (Player p2: players) {
                if (!p1.equals(p2)) {
                    // since order of players will be in both orderings, starting player cursor covers both
                    Game nextGame = new Game(new Player[] {p1, p2}, 0); // take those two random players
                    games.add(nextGame);
                    nextGame.runGame(); // run game and add proper fitness to each participating player
                    // TODO check if player is not being reset!!! After game finished before next game
                }
            }
        }

        // collect fitnesses of players
        System.out.println("Fitnesses");
        double[] fitnesses = new double[players.length];
        for (int i=0; i<players.length; ++i) {
            fitnesses[i] = ((NEATPlayer) players[i]).getFitness(); // note that this also is in order of genome (starting with 1)
        }
        Arrays.sort(fitnesses);
        for (int i=0; i<players.length; ++i) {
            System.out.println(fitnesses[i]);
        }
    }
}