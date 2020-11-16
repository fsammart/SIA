import models.SimplePerceptron as p
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from ActivationFunctions import LinearFunction

def multi_layer_perceptron(eta, iterations):
    
    with open('europe.csv') as f:
        lines = f.readlines()
        aux = []
        for i in range(1, len(lines)):
            line = lines[i].split(sep=',')
            del line[0]
            x1,x2,x3,x4,x5,x6,x7 = [float(x) for x in line]
            aux.append([x1,x2,x3,x4,x5,x6,x7])


    df = pd.DataFrame(aux)

    X = standarize(df).to_numpy()

    # eta2 = 0.5 / np.dot(X[0], X[0])

    # print(eta2)
   
    ppn = p.SimplePerceptron(eta, n_iter=iterations, g=LinearFunction, params=1)


    ppn.trainOjaRule(X)
    print(ppn.w_)


def standarize(df):
    result = df.copy()
    for variable in df.columns:
        mean = df[variable].mean()
        std = df[variable].std()
        result[variable] = (df[variable] - mean) / (std)
    return result

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    multi_layer_perceptron(0.0001, 50)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
