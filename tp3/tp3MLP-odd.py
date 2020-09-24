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
            aux= digit %2
            b[digit] = aux
            a[digit] = []
            for j in range(digit_map_height):
                x1,x2,x3,x4,x5 = [int(x) for x in lines[i*digit_map_height + j].split()]
                a[digit].append([x1,x2,x3,x4,x5])
            digit +=1

    X = np.reshape(a,(10,digit_map_width*digit_map_height))
    y = np.reshape(b,(X.shape[0],1))

    net = mlp.Mlp([digit_map_height*digit_map_width,3,1], momentum=0.5, eta=0.001, eta_adaptative=True, iter_update_eta=5,
                  eta_increment=0.001, eta_decrement=0.1, precision=0.0001, min_eta=0.001, params=0.5,  output_range=[0,1])

    print("train_size" + "epoch" + "\t" + "train_accu" + "\t" + "test_accu")

    for train_size in range(2,11,2):
        test_size = 10 - train_size
        x_train = X[:train_size, :]
        y_train = y[:train_size, :]

        x_test = X[train_size :, :]
        y_test = y[train_size:, :]

        net.train(x_train, y_train, iterations=800, reset=True, batch=True)
        test_predictions = (np.where(net.predict(x_test) < 0.5, 0, 1))
        train_predictions = (np.where(net.predict(x_train) < 0.5, 0, 1))
        TP = 0
        TN = 0
        FP = 0
        FN = 0
        for idx in range(test_predictions.shape[0]):
            if test_predictions[idx] == y_test[idx]:
                #true
                if test_predictions[idx] == 1:
                    TP+=1
                else:
                    TN+=1
            else:
                #false
                if test_predictions[idx] == 1:
                    FP+=1
                else:
                    FN+=1

        print(str(train_size) + "\t" + str(TP) + "\t" + str(TN) + "\t" + str(FP)+ "\t" + str(FN))





# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    multi_layer_perceptron(0.01, 1000)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
