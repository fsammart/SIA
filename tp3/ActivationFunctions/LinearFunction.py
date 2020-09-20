import numpy as np
from ActivationFunctions import Function

def predict(X, w_, beta):

    return net_input(X, w_, beta)


def net_input(X, w_, beta):
    """Calculate z"""
    # z = w · x + theta
    h = Function.net_input(X, w_)
    return h

def g(z, beta):
    return z

def g_prima(z, beta):
    return 1

def __str__():
    return "linear"