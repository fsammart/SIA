import models.MultiLayerPerceptron.Mlp as mlp
import numpy as np
import matplotlib.pyplot as plt
from ActivationFunctions import NonLinearFunction
from ActivationFunctions.Functions import Function as f_range
from sklearn.model_selection import KFold

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

    net = mlp.Mlp([3, 1], momentum=0, eta=0.01, eta_adaptative=True, iter_update_eta=5,
                  eta_increment=0.001, eta_decrement=0.5, precision=0.00033, min_eta=0.00001, params=1)
    max_y = np.max(y)
    max_x = np.ceil(np.max(X))
    min_x = np.floor(np.min(X))
    t1 = lambda x: f_range.range_transform([min_x, max_x], x, [-1, 1])
    t = lambda x: f_range.range_transform([0,max_y], x, [-1,1])
    t2 = lambda x: f_range.range_transform([-1,1], x, [0,max_y])
    y_copy = y.__copy__()
    y = t(y)
    R = X.__copy__()
    X = t1(X)


    percentage = 0.7
    ix = np.random.choice(range(X.shape[0]), int(percentage*X.shape[0]), replace=False)



    kf5 = KFold(n_splits=2, shuffle=False)
    i = 0
    print("set" + "\t" + "epoch" + "\t" + "test_error" + "\t" + "train_error")
    for train_index, test_index in kf5.split(X,y):
        x_train = np.take(X, train_index, axis=0)
        y_train = np.take(y, train_index, axis=0)

        x_test = np.take(X, test_index, axis=0)
        y_test = np.take(y, test_index, axis=0)

        for epoch in range(500):
            net.train(x_train, y_train, iterations=epoch, reset=True, batch=True)
            test_predictions = net.predict(x_test)
            train_predictions = net.predict(x_train)
            test_errors = 0.5 * np.sum(np.power(test_predictions-y_test,2)) / y_test.shape[0]
            train_errors = 0.5 * np.sum(np.power(train_predictions-y_train, 2)) / y_train.shape[0]
            print(str(i) + "\t" +  str(epoch) + "\t" + str(test_errors) + "\t" + str(train_errors))
        i+=1

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    multi_layer_perceptron(0.01, 1000)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
