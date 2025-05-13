from game import Game

def main():
    print("Welcome to Skull!")
    player_names = ["Alice", "Bob", "Carol"]
    game = Game(player_names)

    num_rounds = 3
    for i in range(num_rounds):
        print(f"\n=== ROUND {i+1} ===")
        game.play_round()

    print("\n== Final Scores ==")
    for player in game.players:
        print(f"{player.name}: {player.score} points")

if __name__ == "__main__":
    main()
