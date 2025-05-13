import gym
from gym import spaces
import numpy as np
from typing import Dict
from random import randint

import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..')))

from python.game.game import Game  # Import the classes from your existing code
from python.game.player import Player

class SkullEnv(gym.Env):
    def __init__(self, num_players: int = 4):
        super(SkullEnv, self).__init__()
        
        # Initialize the number of players and the game
        self.num_players = num_players
        self.players = []
        for i in range(num_players):
            self.players.append(Player(str(i)))

        self.game = Game(num_players)  # Your Game class
        self.round = None  # Will be set after calling reset()

        # Define the action space (Discrete = 0..N actions possible)
        self.action_space = spaces.Discrete(6)  # Example actions: 0-3 = play card, 4 = bid, 5 = pass

        # Define the observation space
        # Example: hand is a binary vector of card availability, phase is an integer, etc.
        self.observation_space = spaces.Dict({
            "hand": spaces.MultiBinary(4),  # For simplicity, assume max hand size of 4
            "bids": spaces.Box(low=0, high=5, shape=(self.num_players,), dtype=np.int32),
            "phase": spaces.Discrete(3),  # Phase: 0 = bidding, 1 = playing, 2 = revealing
        })

    def reset(self):
        # Reset the game and round
        self.game = Game(self.players)  # Reset the game state
        self.round = self.game.start_round()  # Start a new round
        return self._get_obs()  # Return the initial observation

    def step(self, action: int):
        # Get the current player (could be done by using `self.round.current_player`)
        current_player = self.round.players[self.round.turn % self.num_players]
        
        # Handle action
        if action < 4:  # Action to play a card
            self.game.round.play_card(current_player, action)
        elif action == 4:  # Bid
            self.game.round.bid(current_player, randint(1, 3))  # For simplicity, use a random bid
        elif action == 5:  # Pass
            self.game.round.pass_turn(current_player)

        # Now update the game state
        self.game.round.update()  # Handle logic like checking for round completion

        # Get observation for the next step
        obs = self._get_obs()
        
        # Calculate reward (you can make this more sophisticated based on game logic)
        reward = self._get_reward()

        # Check if the round is over
        done = self.game.round.is_game_over()

        return obs, reward, done, {}

    def _get_obs(self) -> Dict:
        # Construct the observation for the agent
        # Example observation could include:
        hand = np.array([1, 1, 1, 0])  # Simplified hand (1 for card, 0 for no card)
        bids = np.array([1, 2, 3, 1])  # Simplified bids
        phase = self.game.round.phase  # Current phase of the round

        return {"hand": hand, "bids": bids, "phase": phase}

    def _get_reward(self) -> float:
        # Define a reward based on game state
        # For simplicity, return a random reward for now
        return randint(-1, 1)  # Negative for loss, positive for win (simplified)

    def render(self):
        # Optionally, display the current game state for visualization
        print("Rendering game state:")
        print(f"Current phase: {self.round.phase}")
        print(f"Player hands: {[player.hand for player in self.round.players]}")
        print(f"Bids: {self.round.bids}")
