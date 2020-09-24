import models.MultiLayerPerceptron.Mlp as mlp
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
    y = np.reshape(a,(X.shape[0],1))
    max_y = np.max(y)

    net = mlp.Mlp([3, 1], momentum=0, eta=0.01, eta_adaptative=False, iter_update_eta=5,
                  eta_increment=0.0001, eta_decrement=0.5, precision=0.00033, min_eta=0.00001, params=1,
                  first_eta_automatic=True,input_range=[-1, 1], output_range=[0, max_y])

    max_x = np.ceil(np.max(X))
    min_x = np.floor(np.min(X))
    t1 = lambda x: f_range.range_transform([min_x, max_x], x, [0, 1])

    R = X.__copy__()
    X = X


    net.train(X, y, iterations=15000, reset=False, batch=True)
    #print(R[i])
    #predictions = net.predict(X)
    #print(net.theta_weights)
    #i = 0
    #for elem in predictions:
    #    print(str(i) + "\t" + str(float(elem)) + "\t" + str(float(y[i])))
    #    i +=1




# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    multi_layer_perceptron(0.01, 1000)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
