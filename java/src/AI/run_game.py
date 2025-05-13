import os
import subprocess
import socket

def run_java_file(java_file_path, java_args):
    try:
        # Extract directory and filename without extension
        java_dir, java_file = os.path.split(java_file_path)
        java_file_name = os.path.splitext(java_file)[0]

        # Compile Java file
        compile_process = subprocess.Popen(['javac', java_file], cwd=java_dir)
        compile_process.communicate()  # Wait for compilation to finish

        # If compilation was successful, run the Java program
        if compile_process.returncode == 0:
            run_process = subprocess.Popen(['java', java_file_name] + list(java_args), 
                                            cwd=java_dir,
                                            stdout=subprocess.PIPE,
                                            stderr=subprocess.PIPE)
            stdout, stderr = run_process.communicate()  # Capture stdout and stderr
            if run_process.returncode == 0:
                print("Java program executed successfully.")
                if stdout:
                    print("Standard Output:\n", stdout.decode())
                if stderr:
                    print("Standard Error:\n", stderr.decode())
            else:
                print("Java program execution failed.")
        else:
            print("Compilation failed.")
    except Exception as e:
        print("Error:", e)

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

if __name__=="__main__":
    run_game(["Player1", 1, "Player2", 2, "Player3", 3])