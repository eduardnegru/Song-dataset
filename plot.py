import numpy as np
from numpy.polynomial.polynomial import polyfit
import matplotlib.pyplot as plt

with open('merged.output', 'r') as f:
    lines = f.readlines()
    x = [float(line.split()[0]) for line in lines]
    y = [float(line.split()[1]) for line in lines]

b, m = polyfit(x, y, 1)

fig = plt.figure()
plt.plot(x ,y, linestyle='-', marker='o', color='b')
plt.plot(x , [b +  m * e for e in x], linestyle='--', color='r')
plt.xlabel('Year')
plt.ylabel('Loudness')
plt.show()
fig.savefig("task1.png")
