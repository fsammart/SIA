import models.MultiLayerPerceptron.MultiLayerPerceptron as p
import models.MultiLayerPerceptron.Backpropagation as bp
import numpy as np
import matplotlib.pyplot as plt
from ActivationFunctions import StepFunction, NonLinearFunction

def multi_layer_perceptron(eta, iterations):
    ppn = p.MultiLayerPerceptron(iterations, NonLinearFunction, layers=[2,1,1])
    X = np.array([[-1, 1], [1, -1], [-1, -1], [1, 1]])
    y = np.array([0, 0, 0,1])
    print(X[1])
    print(ppn.compute(X[0]))
    print(ppn.compute(X[1]))
    print(ppn.compute(X[2]))
    print(ppn.compute(X[3]))
    print("---")
    bpe = bp.Backpropagation(ppn, 0.2, 0.005)

    for i in range(500):
        bpe.iterate(X, y)

    print("--")
    print(ppn.compute(X[0]))
    print(ppn.compute(X[1]))
    print(ppn.compute(X[2]))
    print(ppn.compute(X[3]))




# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    multi_layer_perceptron(6, 1000)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
