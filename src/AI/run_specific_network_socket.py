import socket
import json
import neat
import threading
import subprocess
import os

# TODO this socket code/run_model is also used in skull_neat, DRY
def run_model(genomeID, inputs):
    genome = p.population[genomeID]
    net = neat.nn.FeedForwardNetwork.create(genome, config)
    output = net.activate(inputs)
    return output

def run_socket():
    # Set up socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.bind(('localhost', 9999))
    sock.listen(1000)
    print("Server started. Listening on port 9999...")

    while True: # TODO not running socket right now, will need to have it running on thread, waiting for java requests
        conn, addr = sock.accept()
        #print(f"Connection established from {addr}")
        request = conn.recv(4096).decode()
        jsonRequest = json.loads(request) 
        response = run_model(jsonRequest[0], jsonRequest[1]) # TODO replace this real response in but just testing for now

        #print("Response: " + str(response))
        conn.send(str(response).encode())
        conn.close()


# TODO, the problem with this is that to use a Terminal Player you must run the game in java, but need nn setup on python side
local_dir = os.path.dirname(__file__)
config_file = os.path.join(local_dir, 'config-feedforward')
config = neat.Config(neat.DefaultGenome, neat.DefaultReproduction,
                    neat.DefaultSpeciesSet, neat.DefaultStagnation,
                    config_file)
filename = r'results\2-players-nn-more-speciation\neat-checkpoint-299'
p = neat.Checkpointer.restore_checkpoint(filename)
bestGenomeID = 23264 # TODO could save this, but this time just got it manually
bestGenome = p.population[bestGenomeID]

socketThread = threading.Thread(target=run_socket)
socketThread.start() # listen for requests from java

