from typing import List


import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..')))
from python.game.card import Card, CardType
import random

class Player:
    def __init__(self, name: str):
        self.name = name
        self.hand: List[Card] = self._starting_hand()
        self.played_cards: List[Card] = []
        self.has_passed: bool = False
        self.score: int = 0

    def _starting_hand(self) -> List[Card]:
        return [Card(CardType.SKULL)] + [Card(CardType.FLOWER) for _ in range(3)]

    def play_card(self, card: Card) -> None:
        if card in self.hand:
            self.hand.remove(card)
            self.played_cards.append(card)

    def pop_top_played(self) -> Card:
        return self.played_cards.pop()  # top = last

    def reset_for_new_round(self) -> None:
        self.played_cards.clear()
        self.has_passed = False
        # If needed, reshuffle deck or keep as-is

    def is_active(self) -> None:
        if len(self.hand) == 0 and len(self.played_cards) == 0: # If no cards, out of game
            return False 
        return True
    
    def reset(self) -> None:
        # TODO STUB to reset cards for new game
        return

    def __str__(self) -> str:
        return self.name
