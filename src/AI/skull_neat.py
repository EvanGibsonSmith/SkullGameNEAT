"""
2-input XOR example -- this is most likely the simplest possible example.
"""

import os
import json
import neat
import socket
import subprocess

# 2-input XOR inputs and expected outputs.
xor_inputs = [(0.0, 0.0), (0.0, 1.0), (1.0, 0.0), (1.0, 1.0)]
xor_outputs = [(0.0,), (1.0,), (1.0,), (0.0,)]

def run_game(commandArgs: list) -> None:
    command = r'java -cp ".;src/*" src/game_objects/Game.java '
    command += str(len(commandArgs)//2)
    for c in commandArgs:
        command += " " + str(c)

    try:
        result = subprocess.check_output(command, stderr=subprocess.DEVNULL, shell=True, text=True)
    except Exception as e:
        print(f"Error occurred: {str(e)}")
        return 0 # Return 0 instead of NaN because if error occured, the objects stopped particles from appearing at all.
    
class RunNEAT:

    def  __init__(self, config_file) -> None:
        # Load configuration.
        self.config = neat.Config(neat.DefaultGenome, neat.DefaultReproduction,
                            neat.DefaultSpeciesSet, neat.DefaultStagnation,
                            config_file)
        
        # Create the population, which is the top-level object for a NEAT run.
        self.pop = neat.Population(self.config)

        # Add a stdout reporter to show progress in the terminal.
        self.pop.add_reporter(neat.StdOutReporter(True))
        stats = neat.StatisticsReporter()
        self.pop.add_reporter(stats)
        self.pop.add_reporter(neat.Checkpointer(5))

        # Set up socket
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.bind(('localhost', 9999))
        self.socket.listen(1000)
        print("Server started. Listening on port 9999...")

        # TODO put multiple sockets so that the requests can both be handled?
        while True: # TODO not running socket right now, will need to have it running on thread, waiting for java requests
            conn, addr = self.socket.accept()
            print(f"Connection established from {addr}")
            request = conn.recv(4096).decode()
            print(request)
            jsonRequest = json.loads(request) 
            response = self.run_model(jsonRequest[0], jsonRequest[1]) # TODO replace this real response in but just testing for now

            print("Response: " + str(response))
            conn.send(str(response).encode())
            conn.close()

    def run_model(self, genomeID, inputs): # TODO make this set up so that java only needs to provide the genome_id and the inputs
        genome = self.pop.population[genomeID]
        net = neat.nn.FeedForwardNetwork.create(genome, self.config)
        output = net.activate(inputs)
        return output

    # TODO make this the competition between different networks to determine fitness and ranking.
    def eval_genomes(self, genomes, config):
        for genome_id, genome in genomes:
            run_game()
            net = neat.nn.FeedForwardNetwork.create(genome, config)
            net.activate()
            for xi, xo in zip(xor_inputs, xor_outputs):
                output = net.activate(xi)
                genome.fitness -= (output[0] - xo[0]) ** 2


    def run(self):
        # Run for up to 300 generations.
        winner = self.pop.run(self.eval_genomes, 300)

        # Display the winning genome.
        print('\nBest genome:\n{!s}'.format(winner))

        # Show output of the most fit genome against training data.
        print('\nOutput:')
        winner_net = neat.nn.FeedForwardNetwork.create(winner, self.config)


if __name__ == '__main__':
    # Determine path to configuration file. This path manipulation is
    # here so that the script will run successfully regardless of the
    # current working directory.
    local_dir = os.path.dirname(__file__)
    config_path = os.path.join(local_dir, 'config-feedforward')      
    neatObj = RunNEAT(config_path)
    neatObj.run()