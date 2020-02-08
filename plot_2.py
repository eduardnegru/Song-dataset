import numpy as np
from numpy.polynomial.polynomial import polyfit
import matplotlib.pyplot as plt

def generateXY(fileName):
	with open(fileName, 'r') as f:
	    lines = f.readlines()
	    y = [float(x) for x in lines]
	    x = [v for v in range(0, len(y))]
	return x, y

def plot(metricType, c1, c2):
	x_minor, y_minor = generateXY("./data/merged.0." + str(metricType) + ".output")
	x_major, y_major = generateXY("./data/merged.1." + str(metricType) + ".output")
	plt.plot(x_minor, y_minor, linestyle='-', marker='o', color=c1, label="Minor-key-"+metricType)
	plt.plot(x_major, y_major, linestyle='-', marker='o', color=c2, label="Major-key-"+metricType)

fig = plt.figure()
plt.xlabel('Index')
plt.ylabel('Metric')
plot("tempo", 'b', 'r')
plot("loudness", 'g', 'm')
fig.suptitle('Minor-key vs major-keys song sadness analysis', fontsize=12)
plt.legend()
plt.show()
fig.savefig("task2.png")
