from stable_baselines3 import PPO
from env import SkullEnv

# Create environment
env = SkullEnv(num_players=4)

# Train a PPO agent on this environment
model = PPO("MultiInputPolicy", env, verbose=1)
model.learn(total_timesteps=10000)

# Save the trained model
model.save("skull_model")

# To evaluate, load the model
model = PPO.load("skull_model")
obs = env.reset()
done = False
while not done:
    action, _states = model.predict(obs)
    obs, reward, done, _info = env.step(action)
    env.render()  # To print the game state
