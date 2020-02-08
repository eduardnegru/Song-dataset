import numpy as np
import matplotlib.pyplot as plt

genres = ["Rock",
	"Pop",
	"Hip-hop",
	"Edm",
	"Jazz",
	"Folk",
	"Blues",
	"Funk",
	"Soul",
	"Ambient",
	"Reggae",
	"World",
	"Latin",
	"Classical",
	"R&B",
	"Other"]

matrix_size = 16

matrix=[[0]*matrix_size for i in range(matrix_size)]

i = 0
j = 0

with open('./data/merged.matrix.output', 'r') as f:
	for line in f:

		values = line.strip().split(" ")
		j = 0
		for v in values:
			matrix[i][j] = int(float(v))
			j += 1
		i+=1


fig, ax = plt.subplots()
im = ax.imshow(matrix)

# We want to show all ticks...
ax.set_xticks(np.arange(len(genres)))
ax.set_yticks(np.arange(len(genres)))
# ... and label them with the respective list entries
ax.set_xticklabels(genres)
ax.set_yticklabels(genres)

# Rotate the tick labels and set their alignment.
plt.setp(ax.get_xticklabels(), rotation=45, ha="right",
         rotation_mode="anchor")

# Loop over data dimensions and create text annotations.
for i in range(len(genres)):
    for j in range(len(genres)):
        text = ax.text(j, i, matrix[i][j],
                       ha="center", va="center", color="w")

ax.set_title("Crossover")
fig.tight_layout()
plt.show()
