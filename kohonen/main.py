from som import SOM, plot_data_on_map
from numpy import genfromtxt
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import time

def main():

    europe_data = pd.read_csv('europe.csv', delimiter=",")
    country_names = europe_data["Country"]
    del europe_data['Country']
    europe_data = europe_data.to_numpy()

    # standardize
    europe_data = (europe_data - np.mean(europe_data, axis=0)) / np.std(europe_data, axis=0)

    som = SOM()
    som.create(7,7, 7)

    som.fit(europe_data, lr=0.9, n_iter=500)

    data_locations, vect_distances = som.find_maching_nodes(europe_data)

    umatrix = som.get_umatrix()

    tmp_umatrix = umatrix.copy()
    tmp_umatrix -= np.min(tmp_umatrix)
    tmp_umatrix /= np.max(tmp_umatrix)
    tmp_umatrix = 1 - tmp_umatrix

    plt.imshow(tmp_umatrix, cmap='gray')
    plt.colorbar()

    palette = np.array([[0.90196078, 0.09803922, 0.29411765],
                        [0.23529412, 0.70588235, 0.29411765],
                        [1., 0.88235294, 0.09803922],
                        [0., 0.50980392, 0.78431373],
                        [0.96078431, 0.50980392, 0.18823529],
                        [0.2745098, 0.94117647, 0.94117647],
                        [0.94117647, 0.19607843, 0.90196078],
                        [0.98039216, 0.74509804, 0.74509804],
                        [0., 0.50196078, 0.50196078],
                        [0.90196078, 0.74509804, 1.],
                        [0.66666667, 0.43137255, 0.15686275],
                        [1., 0.98039216, 0.78431373],
                        [0.50196078, 0., 0.],
                        [0.66666667, 1., 0.76470588],
                        [0., 0., 0.50196078],
                        [0.50196078, 0.50196078, 0.50196078],
                        [1., 1., 1.]])


    colors = np.zeros((28, 3))
    for i in range(28):
        colors[i] = palette[0]

    filename = 'U_Matrix_' + str(int(time.time())) + '.png'
    plt.savefig(filename)

    plot_data_on_map(umatrix, data_locations, data_colors=colors,
                     node_width=100, data_marker_size=100, names=country_names)


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
   main()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
