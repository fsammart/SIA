import numpy as np
from ActivationFunctions.Functions import Function as f


class Mlp():
    def __init__(self, size_layers, g=f.sigmoid, g_prima=f.sigmoid_derivative, momentum=0, eta=0.01):
        '''
        Constructor method. Defines MLP characteristics
        Arguments:
            size_layers : List with the number of Units for:
                [Input, Hidden1, Hidden2, ... HiddenN, Output] Layers.
            momemtum: momentum param. Default =0
            eta: size of step
        '''
        self.size_layers = size_layers
        self.n_layers = len(size_layers)
        self.momentum = momentum
        self.eta = eta
        self.g = g
        self.g_prima = g_prima

        # Ramdomly initialize theta (MLP weights)
        self.initialize_theta_weights()

    def train(self, X, Y, iterations=800, reset=False, batch=True):
        '''
        Fits model usign X input and Y expected output.
            X          : Feature input matrix [n_examples, n_features]
            Y          : Expected Output
            iterations : Number of times Backpropagation is performed. Default 800
            reset      : If set, initialize Theta Weights before training
                default = False
        '''
        n_examples = Y.shape[0]

        if reset:
            self.initialize_theta_weights()
        self.previous_gradients = None
        if batch:
            for iteration in range(iterations):
                self.gradients, last_delta = self.backpropagation(X, Y)
                if self.previous_gradients == None:
                    self.previous_gradients = self.gradients
                for i in range(len(self.theta_weights)):
                    self.theta_weights[i] = self.theta_weights[i] - self.eta * self.gradients[i] \
                                            + self.momentum*self.previous_gradients[i]
                    self.previous_gradients[i] = - self.eta *  self.gradients[i]
                error = np.mean(np.power(last_delta, 2))
                print(error)
        else:
            for iteration in range(iterations):
                errors = [None] * n_examples
                for i in range(n_examples):
                    patron = X[i, :].reshape((1, X.shape[1]))
                    patron_y = Y[i, :].reshape((1, Y.shape[1]))
                    self.gradients, last_delta = self.backpropagation(patron, patron_y)
                    errors[i] = last_delta
                    if self.previous_gradients == None:
                        self.previous_gradients = self.gradients
                    for i in range(len(self.theta_weights)):
                        self.theta_weights[i] = self.theta_weights[i] - self.eta * self.gradients[i] \
                                                + self.momentum*self.previous_gradients[i]
                        self.previous_gradients[i] = - self.eta * self.gradients[i]
                error = np.mean(np.power(errors, 2))

    def predict(self, X):
        # Predict output value for X input.

        A = self.feedforward(X)
        # output is last value
        output = A[-1]
        return output

    def backpropagation(self, X, Y):
        # Implementation of the Backpropagation algorithm with regularization

        g_dz = lambda x: self.g_prima(x)

        # Feedforward
        A = self.feedforward(X)

        # Backpropagation
        deltas = [None] * self.n_layers
        last_delta = (A[-1] - Y)
        # deltas for last layer
        deltas[-1] = (A[-1] - Y) * g_dz(A[-1])
        # For the second last layer to the second one
        for ix_layer in np.arange(self.n_layers - 1 - 1, 0, -1):
            theta_tmp = self.theta_weights[ix_layer]
            # Removing weights for bias
            theta_tmp = np.delete(theta_tmp, np.s_[0], 1)
            # Remove from A line related to bias
            deltas[ix_layer] = (np.dot(deltas[ix_layer + 1], theta_tmp)) * g_dz(np.delete(A[ix_layer], np.s_[0], 1))

        # Compute gradients
        gradients = [None] * (self.n_layers - 1)
        for ix_layer in range(self.n_layers - 1):
            grads_tmp = np.matmul(deltas[ix_layer + 1].transpose(), A[ix_layer])
            grads_tmp = grads_tmp
            # Regularize weights, except for bias weigths
            grads_tmp[:, 1:] = grads_tmp[:, 1:]
            gradients[ix_layer] = grads_tmp
        return gradients, last_delta

    def feedforward(self, X):
        # Implementation of the Feedforward

        g = lambda x: self.g(x)

        A = [None] * self.n_layers
        input_layer = X
        output_layer = input_layer

        for ix_layer in range(self.n_layers - 1):
            n_examples = input_layer.shape[0]
            # Add bias element to every example in input_layer
            input_layer = np.concatenate((np.ones([n_examples, 1]), input_layer), axis=1)
            A[ix_layer] = input_layer
            # Multiplying input_layer by theta_weights for this layer
            aux = np.matmul(input_layer, self.theta_weights[ix_layer].transpose())
            # Activation Function
            output_layer = g(aux)
            # Current output_layer will be next input_layer
            input_layer = output_layer

        A[self.n_layers - 1] = output_layer
        return A

    def initialize_theta_weights(self):
        # Initialize theta_weights. Depending on layer size.

        self.theta_weights = []
        size_next_layers = self.size_layers.copy()
        size_next_layers.pop(0)
        for size_layer, size_next_layer in zip(self.size_layers, size_next_layers):
            #initialize weights from normal distribution
            #with mean 0 and standard deviation equal to m^-1/2
            deviation = 1 / np.sqrt(size_layer + 1);
            theta_tmp = deviation* np.random.rand(size_next_layer, size_layer + 1)
            self.theta_weights.append(theta_tmp)

        return self.theta_weights
