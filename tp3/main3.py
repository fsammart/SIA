import models.MultiLayerPerceptron.Mlp as mlp
import numpy as np
from matplotlib import pyplot as plt
import matplotlib.cm as pl

def multi_layer_perceptron(eta, iterations):
    net = mlp.Mlp( [2,2,1], momentum=0.8, eta = 0.42, eta_adaptative=True, iter_update_eta=5,
                   eta_increment=0.05, eta_decrement=0.5, precision=0.00001, min_eta=0.01)

    X = np.array([[-1, 1], [1, -1], [-1, -1], [1, 1]])
    y = np.array([1, 1, -1,-1])
    y = np.reshape(y,(len(y),-1))

    print(net.predict(X))
    print("---")
    net.train(X, y, iterations=15000, reset=False, batch=False)


    print("--")
    print(net.predict(X))

    net2 = mlp.Mlp([2, 1], momentum=0, eta=0.2, eta_adaptative=True, iter_update_eta=5,
                  eta_increment=0.05, eta_decrement=0.5, precision=0.00001, min_eta=0.01)

    X = np.array([[-1, 1], [1, -1], [-1, -1], [1, 1]])
    y = np.array([-1, -1, -1, 1])
    y = np.reshape(y, (len(y), -1))

    print(net2.predict(X))
    print("---")
    net2.train(X, y, iterations=15000, reset=False, batch=False)

    print("--")
    print(net2.predict(X))

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    multi_layer_perceptron(6, 1000)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
