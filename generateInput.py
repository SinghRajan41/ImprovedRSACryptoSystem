import os
import random
import string

# Function to generate random string of given size
def generate_random_string(size):
    return ''.join(random.choices(string.ascii_letters, k=size))

# Directory to store the generated files
output_directory = os.path.join(os.getcwd(), "PlainText")
os.makedirs(output_directory, exist_ok=True)

# Generate text files from 1KB to 10MB
file_sizes = [2**i for i in range(0,27)]  # Sizes from 1KB to 10MB
for size in file_sizes:
    filename = os.path.join(output_directory, f"file_{size // 1024}KB.txt")
    with open(filename, 'w') as f:
        # Write random characters to the file
        f.write(generate_random_string(size))
