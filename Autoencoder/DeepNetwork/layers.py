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
    """A fully-connected layer. Each neuron connects with every input neuron
    Parameters:
    -----------
    n_units: int
        The number of neurons in the dense layer.
    input_shape: tuple
        Just to be specified in case its the first layer
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


class BatchNormalization(Layer):
    """Batch normalization.
    """
    def __init__(self, momentum=0.99):
        self.momentum = momentum
        self.trainable = True
        self.eps = 0.01
        self.running_mean = None
        self.running_var = None

    def __deepcopy__ (self, memo) :
        l = BatchNormalization(self.momentum)
        l.eps= self.eps
        l.running_mean = self.running_mean
        l.running_var = self.running_var
        l.trainable=self.trainable
        l.momentum = self.momentum
        l.gamma = copy.deepcopy(self.gamma, memo)
        l.beta = copy.deepcopy(self.beta, memo)
        l.gamma_opt = copy.deepcopy(self.gamma_opt, memo)
        l.beta_opt = copy.deepcopy(self.beta_opt, memo)
        return l

    def initialize(self, optimizer):
        # Initialize the parameters
        self.gamma  = np.ones(self.input_shape)
        self.beta = np.zeros(self.input_shape)
        # parameter optimizers
        self.gamma_opt  = copy.copy(optimizer)
        self.beta_opt = copy.copy(optimizer)

    def parameters(self):
        return np.prod(self.gamma.shape) + np.prod(self.beta.shape)

    def forward_pass(self, X, training=True):

        # Initialize running mean and variance if first run
        if self.running_mean is None:
            self.running_mean = np.mean(X, axis=0)
            self.running_var = np.var(X, axis=0)

        if training and self.trainable:
            mean = np.mean(X, axis=0)
            var = np.var(X, axis=0)
            self.running_mean = self.momentum * self.running_mean + (1 - self.momentum) * mean
            self.running_var = self.momentum * self.running_var + (1 - self.momentum) * var
        else:
            mean = self.running_mean
            var = self.running_var

        # Statistics saved for backward pass
        self.X_centered = X - mean
        self.stddev_inv = 1 / np.sqrt(var + self.eps)

        X_norm = self.X_centered * self.stddev_inv
        output = self.gamma * X_norm + self.beta

        return output

    def backward_pass(self, accum_grad):

        # Save parameters used during the forward pass
        gamma = self.gamma

        # If the layer is trainable the parameters are updated
        if self.trainable:
            X_norm = self.X_centered * self.stddev_inv
            grad_gamma = np.sum(accum_grad * X_norm, axis=0)
            grad_beta = np.sum(accum_grad, axis=0)

            self.gamma = self.gamma_opt.update(self.gamma, grad_gamma)
            self.beta = self.beta_opt.update(self.beta, grad_beta)

        batch_size = accum_grad.shape[0]

        # The gradient of the loss with respect to the layer inputs (use weights and statistics from forward pass)
        accum_grad = (1 / batch_size) * gamma * self.stddev_inv * (
            batch_size * accum_grad
            - np.sum(accum_grad, axis=0)
            - self.X_centered * self.stddev_inv**2 * np.sum(accum_grad * self.X_centered, axis=0)
            )

        return accum_grad

    def output_shape(self):
        return self.input_shape



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



def determine_padding(filter_shape, output_shape="same"):
    if output_shape == "valid":
        return (0, 0), (0, 0)
    elif output_shape == "same":
        filter_height, filter_width = filter_shape
        pad_h1 = int(math.floor((filter_height - 1)/2))
        pad_h2 = int(math.ceil((filter_height - 1)/2))
        pad_w1 = int(math.floor((filter_width - 1)/2))
        pad_w2 = int(math.ceil((filter_width - 1)/2))

        return (pad_h1, pad_h2), (pad_w1, pad_w2)


def get_im2col_indices(images_shape, filter_shape, padding, stride=1):
    # First figure out what the size of the output should be
    batch_size, channels, height, width = images_shape
    filter_height, filter_width = filter_shape
    pad_h, pad_w = padding
    out_height = int((height + np.sum(pad_h) - filter_height) / stride + 1)
    out_width = int((width + np.sum(pad_w) - filter_width) / stride + 1)

    i0 = np.repeat(np.arange(filter_height), filter_width)
    i0 = np.tile(i0, channels)
    i1 = stride * np.repeat(np.arange(out_height), out_width)
    j0 = np.tile(np.arange(filter_width), filter_height * channels)
    j1 = stride * np.tile(np.arange(out_width), out_height)
    i = i0.reshape(-1, 1) + i1.reshape(1, -1)
    j = j0.reshape(-1, 1) + j1.reshape(1, -1)

    k = np.repeat(np.arange(channels), filter_height * filter_width).reshape(-1, 1)

    return (k, i, j)


def image_to_column(images, filter_shape, stride, output_shape='same'):
    filter_height, filter_width = filter_shape

    pad_h, pad_w = determine_padding(filter_shape, output_shape)
    images_padded = np.pad(images, ((0, 0), (0, 0), pad_h, pad_w), mode='constant')

    k, i, j = get_im2col_indices(images.shape, filter_shape, (pad_h, pad_w), stride)

    cols = images_padded[:, k, i, j]
    channels = images.shape[1]
    cols = cols.transpose(1, 2, 0).reshape(filter_height * filter_width * channels, -1)
    return cols

def column_to_image(cols, images_shape, filter_shape, stride, output_shape='same'):
    batch_size, channels, height, width = images_shape
    pad_h, pad_w = determine_padding(filter_shape, output_shape)
    height_padded = height + np.sum(pad_h)
    width_padded = width + np.sum(pad_w)
    images_padded = np.zeros((batch_size, channels, height_padded, width_padded))


    k, i, j = get_im2col_indices(images_shape, filter_shape, (pad_h, pad_w), stride)

    cols = cols.reshape(channels * np.prod(filter_shape), -1, batch_size)
    cols = cols.transpose(2, 0, 1)
    np.add.at(images_padded, (slice(None), k, i, j), cols)

    return images_padded[:, :, pad_h[0]:height+pad_h[0], pad_w[0]:width+pad_w[0]]


