import numpy as np

def net_input(X, w_):
    """Calculate z"""
    # z = w · x 
    z = np.dot(X, w_)
    return z
