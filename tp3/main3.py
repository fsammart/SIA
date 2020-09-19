import models.MultiLayerPerceptron.Mlp as mlp
import numpy as np
from matplotlib import pyplot as plt
import matplotlib.cm as pl

def multi_layer_perceptron(eta, iterations):
    net = mlp.Mlp( [2,2,1], momentum=0.8, eta = 0.42)
    X = np.array([[-1, 1], [1, -1], [-1, -1], [1, 1]])
    y = np.array([1, 1, 0,0])
    y = np.reshape(y,(len(y),-1))

    print(net.predict(X))
    print("---")
    net.train(X, y, iterations=1000, reset=False, batch=False)


    print("--")
    print(net.predict(X))




# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    multi_layer_perceptron(6, 1000)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
