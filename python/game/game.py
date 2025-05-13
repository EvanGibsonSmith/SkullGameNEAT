import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..')))

from python.game.round import FullRound  # Import the FullRound class

class Game:
    def __init__(self, players):
        self.players = players
        self.phase = 'betting'  # Initial phase of the game (can be 'betting', 'revealing', etc.)
        self.round = None  # To be initialized during the round start
        self.cursor = 0  # Starting cursor for the first player (you can modify this logic as needed)

    def start_round(self):
        # Create a FullRound instance for this round
        self.round = FullRound(self.players, self.cursor)
        self.round.run_round()  # Run the round, which will handle all phases
        self.phase = 'end'  # After the round, set the phase to 'end'

    def advance_phase(self):
        # This is now part of FullRound, so we don't need to manually advance phases in this method
        # After starting the round, it handles its own transitions.
        if self.round:
            # If we wanted to handle phase transitions explicitly, we could check here
            print(f"Round has ended. Current phase: {self.phase}")
        else:
            print("No round has been started.")
