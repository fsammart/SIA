import numpy as np
from ActivationFunctions import Function

def predict(X, w_, beta):
    """Step function.
    phi(z) = 1 si z >= theta; -1 en otro caso
    """
    h = Function.net_input(X,w_)
    phi = np.where(h >= 0.0, 1, -1)
    return phi

def g(z, beta):
    """Step function.
    phi(z) = 1 si z >= theta; -1 en otro caso
    """
    return  np.where(z >= 0.0, 1, -1)


def g_prima(z, beta):

    return  1

def __str__():
    return "phi(z) = 1 if z>= 0 else 0"

