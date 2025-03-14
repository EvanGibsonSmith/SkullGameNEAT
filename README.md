## Skull

Skull is a reasonably simple bluffing game that still allows for meaningful strategy (https://www.board-game.co.uk/how-to-play-skull/).

## NEAT

NEAT (Neuro Evolution of Augmenting Topologies) is a neuro evolutionary algorithm to generate good networks weights and a good topology.
In this context NEAT is being used to create a population of "players" that can compete against each other in a tournament hall style, collecting points for winning and losing. 

This allows for diversity of players and can hopefully prevent networks from forgetting past strategies and lead to more robust results, since in this game the strategy of your opponent is especially important
(compared to something like chess, for example).

## Work In Progress

This is a work in progress. At the moment simple neural networks trained with NEAT have been implemented for Skull, and they seem to give reasonable results. At this point the networks play for only a single game, 
so the network plays similarly every time you play it. In the future, it is obviously important to play many games to get a "feel" for the opponents strategy, like in a game of Poker.
