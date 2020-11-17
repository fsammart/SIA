from __future__ import print_function, division
import math
import numpy as np
import copy
from DeepNetwork.activation_functions import Sigmoid, ReLU, SoftPlus, LeakyReLU
from DeepNetwork.activation_functions import TanH, ELU, SELU, Softmax

class Layer(object):

    def layer_name(self):
        return self.__class__.__name__

    def set_input_shape(self, shape):
        """ Input shape the layer expects"""
        self.input_shape = shape

    def output_shape (self) :
        """ Output shape of network layer"""
        raise NotImplementedError()


    def parameters(self):
        """ Number of paramteres, useful for printing """
        return 0

    def forward_pass(self, X, training):
        """ Forward propgation, to know the output of netowork"""
        raise NotImplementedError()

    def backward_pass(self, accum_grad):
        """ Backpropagation of network"""
        raise NotImplementedError()

    def __deepcopy__ (self, memo) :
        raise NotImplementedError()


class Dense(Layer):
    """ Dense layer
    """
    def __init__(self, n_units, input_shape=None):
        self.trainable = True
        self.W = None
        self.w0 = None
        self.layer_input = None
        self.input_shape = input_shape
        self.n_units = n_units

    def __deepcopy__ (self, memo) :
        l = Dense(n_units=self.n_units, input_shape=self.input_shape)
        l.W = copy.deepcopy(self.W, memo)
        l.w0 = copy.deepcopy(self.w0, memo)
        self.W_opt = copy.deepcopy(self.W_opt)
        self.w0_opt = copy.deepcopy(self.w0_opt)
        l.trainable=self.trainable
        return l

    def initialize(self, optimizer):
        # Initialize the weights of the layer
        #Initialization weights (layer+1) ^ (½)

        limit = 1 / math.sqrt(self.input_shape[0] + 1)
        self.W  = np.random.uniform(-limit, limit, (self.input_shape[0], self.n_units))
        self.w0 = np.zeros((1, self.n_units))
        # Weight optimizers, shallow  copy
        self.W_opt  = copy.copy(optimizer)
        self.w0_opt = copy.copy(optimizer)

    def parameters(self):
        # Number of parameters, in this case weights
        return np.prod(self.W.shape) + np.prod(self.w0.shape)

    def forward_pass(self, X, training=True):
        self.layer_input = X
        return X.dot(self.W) + self.w0  #adds bias

    def backward_pass(self, accum_grad):
        # Weights saved during forward propagation
        W = self.W

        if self.trainable:
            # some layers do not adjust
            grad_w = self.layer_input.T.dot(accum_grad)
            grad_w0 = np.sum(accum_grad, axis=0, keepdims=True)

            # Update the layer weights using update from optimizer
            self.W = self.W_opt.update(self.W, grad_w)
            self.w0 = self.w0_opt.update(self.w0, grad_w0)

        # Return accumulated gradient difference for next layer to backprogate
        accum_grad = accum_grad.dot(W.T)
        return accum_grad

    def output_shape(self):
        return (self.n_units, )


activation_functions = {
    'relu': ReLU,
    'sigmoid': Sigmoid,
    'selu': SELU,
    'elu': ELU,
    'softmax': Softmax,
    'leaky_relu': LeakyReLU,
    'tanh': TanH,
    'softplus': SoftPlus
}

class Activation(Layer):
    """Applies function to the input"""

    def __init__(self, name):
        self.activation_name = name
        self.activation_func = activation_functions[name]()
        self.trainable = True

    def layer_name(self):
        return "Activation Function Layer (%s)" % (self.activation_func.__class__.__name__)

    def forward_pass(self, X, training=True):
        self.layer_input = X
        return self.activation_func(X)

    def backward_pass(self, accum_grad):
        return accum_grad * self.activation_func.gradient(self.layer_input)

    def output_shape(self):
        return self.input_shape

    def __deepcopy__ (self, memo) :
        a = Activation(self.activation_name)
        return a



