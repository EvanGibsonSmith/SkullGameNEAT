from typing import List, Optional


import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..')))

from python.game.player import Player
from python.game.card import Card, CardType
import random

class Game:
    def __init__(self, player_names: List[str]):
        self.players: List[Player] = [Player(name) for name in player_names]
        self.turn_index: int = 0
        self.current_bid: int = 0
        self.highest_bidder: Optional[Player] = None
        self.revealing_player: Optional[Player] = None
        self.in_revealing_phase: bool = False

    def start_round(self) -> None:
        print("== New Round Begins ==")
        for player in self.players:
            player.reset_for_new_round()
        self.turn_index = 0
        self.current_bid = 0
        self.highest_bidder = None
        self.in_revealing_phase = False

    def lay_card(self, player: Player, card: Card) -> None:
        player.play_card(card)
        print(f"{player.name} plays a card.")

    def bidding_phase(self) -> None:
        print("== Bidding Phase Begins ==")
        num_played_total = sum(len(p.played_cards) for p in self.players)
        self.current_bid = 0
        self.highest_bidder = None

        players_in_bidding = self.players[:]
        current_index = self.turn_index

        while len([p for p in players_in_bidding if not p.has_passed]) > 1:
            player = players_in_bidding[current_index % len(players_in_bidding)]
            if player.has_passed:
                current_index += 1
                continue

            max_bid = sum(len(p.played_cards) for p in self.players)
            # Placeholder: Replace with real bidding input
            bid_or_pass = random.choice(["pass", "bid"])
            if bid_or_pass == "pass":
                player.has_passed = True
                print(f"{player.name} passes.")
            else:
                new_bid = self.current_bid + 1
                if new_bid > max_bid:
                    print(f"{player.name} cannot bid more than max ({max_bid}).")
                    player.has_passed = True
                else:
                    self.current_bid = new_bid
                    self.highest_bidder = player
                    print(f"{player.name} bids {new_bid}.")

            current_index += 1

        print(f"{self.highest_bidder.name} wins the bid with {self.current_bid}.")
        self.revealing_player = self.highest_bidder
        self.in_revealing_phase = True

    def revealing_phase(self) -> None:
        print(f"== {self.revealing_player.name} Begins Revealing ==")
        remaining_to_reveal = self.current_bid

        # Start by revealing their own cards
        while self.revealing_player.played_cards and remaining_to_reveal > 0:
            card = self.revealing_player.pop_top_played()
            print(f"{self.revealing_player.name} reveals their own card: {card}")
            if card.is_skull():
                print("💀 Skull! They lose a random card from their hand.")
                self.lose_random_card(self.revealing_player)
                return
            remaining_to_reveal -= 1

        # Now reveal from other players
        other_players = [p for p in self.players if p != self.revealing_player and p.played_cards]
        random.shuffle(other_players)

        for player in other_players:
            while player.played_cards and remaining_to_reveal > 0:
                card = player.pop_top_played()
                print(f"{self.revealing_player.name} reveals {player.name}'s card: {card}")
                if card.is_skull():
                    print("💀 Skull! They lose a random card from their hand.")
                    self.lose_random_card(self.revealing_player)
                    return
                remaining_to_reveal -= 1

            if remaining_to_reveal == 0:
                break

        if remaining_to_reveal == 0:
            print(f"{self.revealing_player.name} successfully revealed {self.current_bid} cards!")
            self.revealing_player.score += 1
        else:
            print("Not enough cards to reveal. Round ends without scoring.")

    def lose_random_card(self, player: Player) -> None:
        if not player.hand:
            print(f"{player.name} has no cards left to lose.")
            return
        lost_card = random.choice(player.hand)
        player.hand.remove(lost_card)
        print(f"{player.name} loses a card from their hand: {lost_card}")
        if not player.hand:
            print(f"{player.name} is eliminated!")

    def play_round(self) -> None:
        self.start_round()

        # Card laying: each player plays one card (random here for demo)
        print("== Card Laying Phase ==")
        for player in self.players:
            if player.hand:
                card = random.choice(player.hand)
                self.lay_card(player, card)

        self.bidding_phase()
        if self.revealing_player:
            self.revealing_phase()
