import numpy as np
from ActivationFunctions.StepFunction import predict as predictStep
from ActivationFunctions.StepFunction import predict
"""
Params
----------
    eta
    n_iter = number of iterations.
    g = activation function
"""
class SimplePerceptron:

    def __init__(self, eta=0.1, n_iter=10, g=predict, params = None):
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
        self.w_ = np.zeros(1 + X.shape[1])
        self.errors_ = []

        for _ in range(self.n_iter):
            errors = 0
            for xi, target in zip(X, y):
                error_predictions = (target - self.g.predict(xi, self.w_, self.params))
                update = self.eta * error_predictions
                self.w_[1:] += update * xi
                self.w_[0] += update
                errors += int(update != 0.0)
            self.errors_.append(errors)
            if errors == 0 or (np.abs(error_predictions)) < 0.01:
                break
        return self


