import models.MultiLayerPerceptron.Mlp as mlp
import numpy as np
import matplotlib.pyplot as plt
from ActivationFunctions import NonLinearFunction
from ActivationFunctions.Functions import Function as f_range

def multi_layer_perceptron(eta, iterations):
    a = [None] * 10
    b = [None] * 10
    digit_map_width = 5
    digit_map_height = 7
    digit = 0
    with open('TP3-ej3-mapa-de-pixeles-digitos-decimales.txt') as f:
        lines = f.readlines()
        for i in range(10):
            aux = [0] * 10
            aux[digit] = 1
            b[digit] = aux
            a[digit] = []
            for j in range(digit_map_height):
                x1,x2,x3,x4,x5 = [int(x) for x in lines[i*digit_map_height + j].split()]
                a[digit].append([x1,x2,x3,x4,x5])
            digit +=1

    X = np.reshape(a,(10,digit_map_width*digit_map_height))
    y = np.reshape(b,(X.shape[0],10))

    net = mlp.Mlp([digit_map_height*digit_map_width,5,3,10], momentum=0.5, eta=0.01, eta_adaptative=True, iter_update_eta=5,
                  eta_increment=0.01, eta_decrement=0.1, precision=0.00001, min_eta=0.00001, params=0.5, output_range=[0,1])


    y

    net.train(X, y, iterations=20000, reset=False, batch=True)
    #print(R[i])
    predictions = net.predict(X)
    print(predictions)



# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    multi_layer_perceptron(0.01, 1000)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
