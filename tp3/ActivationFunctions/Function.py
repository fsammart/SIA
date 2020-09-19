import numpy as np

def net_input(X, w_):
    """Calculate z"""
    # z = w · x + w0
    z = np.dot(X, w_[1:]) + w_[0]
    return z
