
import matplotlib.pyplot as plt
from matplotlib.ticker import LinearLocator
from mpl_toolkits.mplot3d import axes3d
import numpy as np

def plot():
    a = []
    with open('TP3-ej2-Conjuntoentrenamiento.txt') as f:
        lines = f.readlines()
        for i in range(0, len(lines)):
            x1, x2, x3 = [float(x) for x in lines[i].split()]
            a.append([x1, x2, x3])
    X = np.array(a)
    a = []
    with open(' TP3-ej2-Salida-deseada.txt') as f:
        lines = f.readlines()
        for i in range(0, len(lines)):
            x1 = [float(x) for x in lines[i].split()]
            a.append([x1])
    y = np.array(a)

    fig, ax = plt.subplots()


    # Plot the surface.
    surf = ax.scatter(np.reshape(X[:,2],(1,200)), np.reshape(y,(1,200)),
                           linewidth=0, antialiased=False)



    # Add a color bar which maps values to colors.
    fig.colorbar(surf, shrink=0.5, aspect=5)

    plt.show()


if __name__ == '__main__':
    plot()