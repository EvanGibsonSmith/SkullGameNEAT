from typing import List

import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..')))

from python.game.player import Player

class FullRound:
    def __init__(self, players: List['Player'], starting_player_cursor: int):
        # Initialize players, active players, and the starting cursor
        self.players = players
        self.active_players = self.get_active_players(players)
        self.cursor = starting_player_cursor  # Initial cursor to start placing round
        
        self.placing_round = None
        self.betting_round = None
        self.revealing_round = None

    def get_cursor(self) -> int:
        return self.cursor
    
    def get_num_played_cards(self) -> int:
        # Get the total number of played cards across all active players
        return sum(len(player.played_cards) for player in self.active_players)

    def get_active_players(self, players: List['Player']) -> List['Player']:
        # Assuming there's a function to get active players based on some condition
        return [player for player in players if player.is_active()]

    def run_round(self):
        # Run the full round with all phases
        self.run_placing_round()
        self.run_betting_round()
        success = self.run_revealing_round()
        
        # If revealing round was not successful, remove a card from the player
        if not success:
            self.active_players[self.cursor].remove_card(self.players)

    def run_placing_round(self):
        # Run placing round, which updates player variables and cursor
        self.placing_round = PlacingRound(self.players, self.cursor)
        self.placing_round.run_round()  # This updates the cursor
        self.cursor = self.placing_round.get_cursor()

    def run_betting_round(self):
        # Run betting round, passing the number of played cards and the current cursor
        self.betting_round = BettingRound(self.players, self.get_num_played_cards(), self.cursor)
        self.cursor = self.betting_round.run_round()

    def run_revealing_round(self) -> bool:
        # Run revealing round with bet value from betting round and the current cursor
        self.revealing_round = RevealingRound(self.players, self.betting_round.get_bet_value(), self.cursor)
        return self.revealing_round.run_round()
    
# Sample implementation of the round classes (PlacingRound, BettingRound, RevealingRound)
# These will need to match your game logic (I'll provide basic structure)

class PlacingRound:
    def __init__(self, players: List['Player'], cursor: int):
        self.players = players
        self.cursor = cursor

    def run_round(self):
        # Place cards or perform any actions related to the placing phase
        # For example, updating player variables
        self.cursor = (self.cursor + 1) % len(self.players)

    def get_cursor(self) -> int:
        return self.cursor

class BettingRound:
    def __init__(self, players: List['Player'], num_played_cards: int, cursor: int):
        self.players = players
        self.num_played_cards = num_played_cards
        self.cursor = cursor

    def run_round(self) -> int:
        # Implement the logic for betting phase (updating the cursor based on actions)
        return (self.cursor + 1) % len(self.players)

    def get_bet_value(self) -> int:
        # Return the bet value based on the current round logic
        return 1  # Placeholder, should be the actual bet value

class RevealingRound:
    def __init__(self, players: List['Player'], bet_value: int, cursor: int):
        self.players = players
        self.bet_value = bet_value
        self.cursor = cursor

    def run_round(self) -> bool:
        # Implement the logic for the revealing phase
        # Return False if the round was unsuccessful, True otherwise
        return True  # Placeholder, should be the actual success condition
