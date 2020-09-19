import numpy as np
from ActivationFunctions import Function

def predict(X, w_, beta):
    """NonLinear.
    tanh
    """
    return net_input(X, w_, beta)


def net_input(X, w_, beta):
    """Calculate z"""
    # z = w · x + theta
    h = Function.net_input(X, w_)
    z = g(h, beta)
    return z

def g(z, beta):
    return np.tanh(z*beta)


def __str__():
    return "phi(z) = tanh(z*beta)"