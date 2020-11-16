import numpy as np
import matplotlib.pyplot as plt


from ActivationFunctions.StepFunction import predict
from ActivationFunctions.Function import net_input

"""
Params
----------
    eta
    n_iter = number of iterations.
    g = activation function
"""


class SimplePerceptron:

    def __init__(self, eta=0.1, n_iter=10, g=predict, params=None):
        self.eta = eta
        self.n_iter = n_iter
        self.g = g
        self.params = params

    def fit(self, X, y):
        """
        Params
        ----------
        X:  [n_samples, n_features]

        """

        deviation = 2 / np.sqrt(X.shape[1]  + 1);
        self.w_ = deviation * np.random.rand(X.shape[1] + 1)
        self.errors_ = []
        self.last_error = 0
        self.decreasing_errors = 0
        self.increasing_errors = 0
        self.eta_increment = 0.0005
        self.eta_decrement = 0.5
        self.min_eta = 0.00002
        self.iter_update_eta = 2
        self.momentum = 0.7

        self.deltas = [None] * (self.n_iter+1)
        self.deltas[0] = np.zeros(X.shape[1]  + 1)
        # batch
        for _ in range(self.n_iter):
            errors = 0
            update = np.zeros(X.shape[1]  + 1)
            self.errors_ = []
            for xi, target in zip(X, y):
                z = net_input(xi, self.w_)
                error_predictions = target - self.g.g(z, self.params)
                update += np.reshape(self.eta * error_predictions * self.g.g_prima(z,self.params)* np.append([1],xi),X.shape[1]  + 1)
                self.errors_.append(error_predictions)
            self.w_ = update + self.w_ + self.momentum *  self.deltas[_]
            self.deltas[_ + 1] = update

            self.adapt_eta(0.5 * np.sum(np.power(self.errors_, 2)))
            print(self.eta)
            print(0.5 * np.sum(np.power(self.errors_, 2)))
            if 0.5 * np.sum(np.power(self.errors_, 2)) < 0.0001:
                print("error reached")
                break
        return self


    def trainOjaRule(self, X):
        """
        Params
        ----------
        X:  [n_samples, n_features]
        y:  [n_samples].

        """

        a = []
        b = []
        deviation = 2 / np.sqrt(X.shape[1]);
        self.w_ = deviation * np.random.rand(X.shape[1])
        # self.w_ = np.zeros(X.shape[1])
        self.error = 1
        self.last_error = 0
        self.decreasing_errors = 0
        self.increasing_errors = 0
        self.eta_increment = 0.0005
        self.eta_decrement = 0.5
        self.min_eta = 0.00002
        self.iter_update_eta = 2
        self.momentum = 0.7
        component = [-0.1248739, 0.50050586, -0.40651815, 0.48287333, -0.18811162, 0.47570355, -0.27165582]

        self.deltas = [None] * (self.n_iter+1)
        self.deltas[0] = np.zeros(X.shape[1])
        # batch
        for _ in range(self.n_iter):

            distance = np.linalg.norm(self.w_ - component)
            a.append(_)
            b.append(distance)

            update = np.zeros(X.shape[1])
            for xi in X:
                y = net_input(xi, self.w_)
                update += np.reshape(self.eta * y * ( xi - y * self.w_), X.shape[1])
            self.w_ = update + self.w_ 
            #+ self.momentum *  self.deltas[_]
            #self.deltas[_ + 1] = update

            # print(self.w_)
            w_norm = np.linalg.norm(self.w_, 2)
            error = abs(1 - w_norm)  
            # print(w_norm)
            self.adapt_eta(error)
            # print(self.eta)
            # print(0.5 * np.sum(np.power(self.errors_, 2)))
            # if error < 0.00000000001:
            #     print("error reached")
            #     break



        plt.scatter(a, b, color='blue')

        fig = plt.gcf()
        axes = fig.gca()

        plt.xlabel('Epoca', fontsize=15)
        plt.ylabel('distancia entre vector de pesos a la primer componente', fontsize=15)
        axes.tick_params(axis='x', labelsize=10)
        axes.tick_params(axis='y', labelsize=10)
        # axes.set_yscale('log')
        plt.show()

        return self

    def adapt_eta(self, error):
        if self.last_error == 0:
            self.last_error = error
        elif self.last_error < error:
            # increasing
            self.decreasing_errors = 0
            self.increasing_errors += 1
        elif self.last_error > error:
            # decreasing
            self.decreasing_errors += 1
            self.increasing_errors = 0
        if self.decreasing_errors >= self.iter_update_eta:
            self.eta += self.eta_increment
            self.decreasing_errors = 0
        if self.increasing_errors >= self.iter_update_eta:
            self.eta *= self.eta_decrement
            if self.eta < self.min_eta:
                self.eta = self.min_eta
            self.increasing_errors = 0
        self.last_error = error
        return
