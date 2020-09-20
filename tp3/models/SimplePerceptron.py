import numpy as np

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
        y:  [n_samples].

        """
        ''' Initialize weights with zeros.'''

        deviation = 2 / np.sqrt(X.shape[1]  + 1);
        self.w_ = deviation * np.random.rand(X.shape[1] + 1)
        self.errors_ = []
        self.last_error = 0
        self.decreasing_errors = 0
        self.increasing_errors = 0
        self.eta_increment = 0.001
        self.eta_decrement = 0.4
        self.min_eta = 0.00001
        self.iter_update_eta = 5
        self.momentum = 0.8

        self.deltas = [None] * (self.n_iter+1)
        self.deltas[0] = np.zeros(X.shape[1]  + 1)
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
