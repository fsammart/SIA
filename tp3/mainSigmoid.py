import models.SimplePerceptron as p
import numpy as np
import matplotlib.pyplot as plt
from ActivationFunctions import LinearFunction

def multi_layer_perceptron(eta, iterations):
    a = []

    with open('TP3-ej2-Conjuntoentrenamiento.txt') as f:
        lines = f.readlines()
        for i in range(0, len(lines)):
            x1,x2,x3 = [float(x) for x in lines[i].split()]
            a.append([x1,x2,x3])
    X = np.array(a)
    a = []
    with open(' TP3-ej2-Salida-deseada.txt') as f:
        lines = f.readlines()
        for i in range(0, len(lines)):
            x1 = [float(x) for x in lines[i].split()]
            a.append([x1])
    y = np.array(a)

    ppn = p.SimplePerceptron(eta, n_iter=iterations, g=LinearFunction, params=0.1)

    ppn.fit(X,y)
    print(X[1])
    print(ppn.g.predict(X[1,:], ppn.w_,0))




# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    multi_layer_perceptron(0.002, 1000)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
