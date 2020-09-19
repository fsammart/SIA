import numpy as np


class Function:

    @staticmethod
    def sigmoid(z):
        '''
        Sigmoid function
        z can be an numpy array or scalar
        '''
        result = 1.0 / (1.0 + np.exp(-z))
        return result

    @staticmethod
    def relu(z):
        '''
        Rectified Linear function
        z can be an numpy array or scalar
        '''
        if np.isscalar(z):
            result = np.max((z, 0))
        else:
            zero_aux = np.zeros(z.shape)
            meta_z = np.stack((z, zero_aux), axis=-1)
            result = np.max(meta_z, axis=-1)
        return result

    @staticmethod
    def sigmoid_derivative(z):
        '''
        Derivative for Sigmoid function
        z can be an numpy array or scalar
        '''
        result = z * (1 - z)
        return result

    @staticmethod
    def relu_derivative(z):
        '''
        Derivative for Rectified Linear function
        z can be an numpy array or scalar
        '''
        result = 1 * (z > 0)
        return result
