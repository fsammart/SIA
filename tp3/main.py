import models.SimplePerceptron as p
import numpy as np
import matplotlib.pyplot as plt
from ActivationFunctions import StepFunction, NonLinearFunction

def simple_perceptron(eta, iterations):
    ppn = p.SimplePerceptron(eta, iterations, StepFunction, 0.1)
    X = np.array([[-1, 1], [1, -1], [-1, -1], [1, 1]])
    y = np.array([1, 1, 1, -1])

    ppn.fit(X, y)
    plt.figure(1)
    # Graficamos el número de errores en cada iteración
    plt.plot(range(1, len(ppn.errors_) + 1), ppn.errors_, marker='o')
    plt.xticks(np.arange(1,  len(ppn.errors_) + 1, 1.0))
    plt.xlabel('Epochs')
    plt.ylabel('Número de actualizaciones')

    plt.figure(2)
    plt.scatter(X[:,0], X[:,1], c=y)

    plt.tight_layout()
    plt.show()

    print("Wo: " + str(ppn.w_[0]))
    print("W: " + str(ppn.w_[1:]))
    print("X: [x Coordinate, y Coordinate]")
    print("z = W · X + Wo ")
    print(ppn.g.__str__())


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    simple_perceptron(1, 1000)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
