import models.SimplePerceptron as p
import numpy as np
import matplotlib.pyplot as plt
from ActivationFunctions import NonLinearFunction
from ActivationFunctions.Functions import Function as f_range

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

    ppn = p.SimplePerceptron(eta, n_iter=iterations, g=NonLinearFunction, params=1)
    max_y = max(y)[0][0]
    max_x = np.ceil(np.max(X))
    min_x = np.floor(np.min(X))
    t1 = lambda x: f_range.range_transform([min_x, max_x], x, [-1, 1])
    t = lambda x: f_range.range_transform([0,max_y], x, [-1,1])
    t2 = lambda x: f_range.range_transform([-1,1], x, [0,max_y])
    y = t(y)
    R = X.__copy__()
    X = t1(X)
    ppn.fit(X,y)
    for i in range(X.shape[0]):
        print(R[i])
        print(t2(ppn.g.predict(X[i,:], ppn.w_,1)))




# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    multi_layer_perceptron(0.01, 1000)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
