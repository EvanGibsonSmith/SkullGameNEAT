from enum import Enum

class CardType(Enum):
    SKULL = "Skull"
    FLOWER = "Flower"

class Card:
    def __init__(self, card_type: CardType):
        self.card_type = card_type

    def is_skull(self) -> bool:
        return self.card_type == CardType.SKULL

    def __repr__(self) -> str:
        return self.card_type.value
