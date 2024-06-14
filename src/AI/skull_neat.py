"""
2-input XOR example -- this is most likely the simplest possible example.
"""

import os
import json
import neat
import socket
import subprocess
import threading
from neat.six_util import iteritems

def run_population(commandArgs: list) -> None:
    command = r'java -cp ".;src/*" src/AI/RunNEATPopulationGames.java '
    for c in commandArgs:
        command += '\"' + str(c) + '\" '

    try:
        result = subprocess.check_output(command, stderr=subprocess.DEVNULL, shell=True, text=True)
    except Exception as e:
        print(f"Error occurred: {str(e)}")
        return -1
    
    return result

class RunNEAT:
    
    def  __init__(self, config_file, networkType) -> None:
        # Load configuration.
        self.config = neat.Config(neat.DefaultGenome, neat.DefaultReproduction,
                            neat.DefaultSpeciesSet, neat.DefaultStagnation,
                            config_file)
        
        self.networkType = networkType
        self.net = {} # networks for each genomeID
        
        # Create the population, which is the top-level object for a NEAT run.
        self.pop = neat.Population(self.config)

        # Add a stdout reporter to show progress in the terminal.
        self.pop.add_reporter(neat.StdOutReporter(True))
        stats = neat.StatisticsReporter()
        self.pop.add_reporter(stats)
        self.pop.add_reporter(neat.Checkpointer(5))

        socketThread = threading.Thread(target=self.run_socket)
        socketThread.start() # listen for requests from java

    def createNetwork(self, genome):
        if (self.networkType=="feedforward"):
            return neat.nn.FeedForwardNetwork.create(genome, self.config)
        elif (self.networkType=="recurrent"):
            return neat.nn.RecurrentNetwork.create(genome, self.config)
        else:
            raise RuntimeError("Invalid networkType")

    def rebuildNets(self, genome_ids):
        self.nets = {}
        for genome_id in genome_ids:
            genome = self.pop.population[genome_id]
            net = self.createNetwork(genome)
            self.nets[genome_id] = net

    def resetGenomes(self):
        if (self.networkType!="recurrent"): # if feedforward, no need to reset
            for net in self.nets:
                net.reset()

    def run_socket(self):
        
        # Set up socket
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.bind(('localhost', 9999))
        self.socket.listen(1000)
        print("Server started. Listening on port 9999...")

        while True: # TODO not running socket right now, will need to have it running on thread, waiting for java requests
            conn, addr = self.socket.accept()
            #print(f"Connection established from {addr}")
            request = conn.recv(4096).decode()
            if (request[0:5]=="reset"):
                self.resetGenomes() # reset genomes for next game
            else: # process json request
                jsonRequest = json.loads(request) 
                response = self.run_model(jsonRequest[0], jsonRequest[1]) # TODO replace this real response in but just testing for now

                #print("Response: " + str(response))
                conn.send(str(response).encode())
                conn.close()

    def run_model(self, genome_id, inputs):
        net = self.nets[genome_id]
        output = net.activate(inputs)
        return output
    
    def eval_genomes(self, genomes, config):
        genomes_dict = {t[0]: t[1] for t in genomes} # get dictionary to correspond
        genome_ids = [t[0] for t in genomes]
        self.rebuildNets(genome_ids) # rebuild nets for new population, and reset them in the process of building new ones

        fitnessesJson = run_population([genome_ids, 2, 1000, 1200]) # TODO make more flexible for configuring number of players per game and num games and seed
        fitnesses = json.loads(fitnessesJson)
        for genome_id, fitness in fitnesses.items(): # update fitness for each of those genomes
            genome = genomes_dict[int(genome_id)] 
            genome.fitness = fitness # give fitness calculated from java

    def run(self):
        # Run for up to 300 generations.
        winner = self.pop.run(self.eval_genomes, 300)
        
        # Display the winning genome.
        print('\nBest genome:\n{!s}'.format(winner))


if __name__ == '__main__':
    # Determine path to configuration file. This path manipulation is
    # here so that the script will run successfully regardless of the
    # current working directory.
    local_dir = os.path.dirname(__file__)
    config_path = os.path.join(local_dir, 'config-recurrent')      
    neatObj = RunNEAT(config_path, "recurrent")
    neatObj.run()
