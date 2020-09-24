import models.MultiLayerPerceptron.Mlp as mlp
import numpy as np
from matplotlib import pyplot as plt
import matplotlib.cm as pl
from itertools import product
from ActivationFunctions.Functions import Function as f

def multi_layer_perceptron(eta, iterations):
    net = mlp.Mlp( [2,3,1],g=f.tanh, g_prima=f.tanh_derivative, momentum=0.5, eta = 0.42, eta_adaptative=False, iter_update_eta=5,
                   eta_increment=0.001, eta_decrement=0.5, precision=0.00001, min_eta=0.0001, params = 1,
                   first_eta_automatic=True, input_range=[0,1], output_range=[-1,1])

    X = np.array([[-1, 1], [1, -1], [-1, -1], [1, 1]])
    y = np.array([1, 1, -1,-1])
    y = np.reshape(y,(len(y),-1))

    print(net.predict(X))
    print("---")
    net.train(X, y, iterations=15000, reset=False, batch=True)






# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    multi_layer_perceptron(6, 1000)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
