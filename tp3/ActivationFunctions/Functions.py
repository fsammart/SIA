import numpy as np


class Function:

    @staticmethod
    def sigmoid(z, beta):
        '''
        Sigmoid function
        z can be an numpy array or scalar
        '''
        result = 1.0 / (1.0 + np.exp(-z*beta))
        return result

    @staticmethod
    def range_transform(input_range, x, output_range):
        old_range = max(input_range) - min(input_range)
        new_range = max(output_range) - min(output_range)
        if x.shape == ():
            return (((x - min(input_range)) * new_range) / old_range) + min(output_range)
        for i in range(x.shape[0]):
            x[i] = (((x[i] - min(input_range)) * new_range) / old_range) + min(output_range)
        return x


    @staticmethod
    def relu(z, params):
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
    def sigmoid_derivative(z, beta):
        '''
        Derivative for Sigmoid function
        z can be an numpy array or scalar
        '''
        result = beta* z * (1 - z)
        return result

    @staticmethod
    def relu_derivative(z, params):
        '''
        Derivative for Rectified Linear function
        z can be an numpy array or scalar
        '''
        result = 1 * (z > 0)
        return result

    @staticmethod
    def linear(z, beta):
        return z

    @staticmethod
    def linear_derivative(z, beta):
        return 1
