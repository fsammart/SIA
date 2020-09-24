import numpy as np
from ActivationFunctions.Functions import Function as f


class Mlp():
    def __init__(self, size_layers, g=f.sigmoid, g_prima=f.sigmoid_derivative,
                 momentum=0, eta=0.01, eta_adaptative=True, iter_update_eta=5,
                 eta_increment=0.01, eta_decrement=0.1, precision=0.01, min_eta=0.01,
                  input_range=[0,1], output_range=[-1,1], params=None, first_eta_automatic=False):

        self.size_layers = size_layers
        self.n_layers = len(size_layers)
        self.momentum = momentum
        self.eta = eta
        self.g = g
        self.g_prima = g_prima
        self.eta_adaptative = eta_adaptative
        self.iter_update_eta = iter_update_eta
        self.eta_increment = eta_increment
        self.eta_decrement = eta_decrement
        self.precision = precision
        self.min_eta=min_eta
        self.output_range = output_range
        self.input_range = input_range
        self.range_transform = f.range_transform
        self.params = params
        self.first_eta_automatic = first_eta_automatic

        # Ramdomly initialize theta (MLP weights)
        self.initialize_theta_weights()

    def train(self, X, y, iterations=800, reset=False, batch=True):
        '''
        Fits model usign X input and Y expected output.
            X          : Feature input matrix [n_examples, n_features]
            Y          : Expected Output
            iterations : Number of times Backpropagation is performed. Default 800
            reset      : If set, initialize Theta Weights before training
                default = False
        '''
        n_examples = y.shape[0]
        self.last_error = 0
        self.increasing_errors = 0
        self.decreasing_errors = 0

        error = 1
        Y = np.copy(y)
        # transform output range
        t = lambda x: self.range_transform(self.output_range, x, self.input_range)
        Y = t(Y)

        self.gradients, last_delta, A = self.backpropagation(X, Y)
        self.previous_gradients = self.gradients
        if self.first_eta_automatic: self.eta = self.get_best_alpha(A,Y)
        if reset:
            self.initialize_theta_weights()
        if batch:
            for iteration in range(iterations):
                if error < self.precision:
                    break
                self.gradients, last_delta, A = self.backpropagation(X, Y)
                for i in range(len(self.theta_weights)):
                    self.theta_weights[i] = self.theta_weights[i] - self.eta * self.gradients[i] \
                                            + self.momentum*self.previous_gradients[i]
                    self.previous_gradients[i] = - self.eta *  self.gradients[i]

                error = 0.5 * np.sum(np.power(last_delta, 2))
                #print(str(iteration) + "\t" + str(self.eta) + "\t" + str(error))
                if self.eta_adaptative: self.adapt_eta(error)
        else:
            for iteration in range(iterations):
                if error < self.precision:
                    break
                errors = [None] * n_examples
                for i in range(n_examples):
                    patron = X[i, :].reshape((1, X.shape[1]))
                    patron_y = Y[i, :].reshape((1, Y.shape[1]))
                    self.gradients, last_delta, A = self.backpropagation(patron, patron_y)
                    errors[i] = last_delta

                    for i in range(len(self.theta_weights)):
                        self.theta_weights[i] = self.theta_weights[i] - self.eta * self.gradients[i] \
                                                + self.momentum*self.previous_gradients[i]
                        self.previous_gradients[i] = - self.eta * self.gradients[i]
                error = 0.5 * np.sum(np.power(errors, 2))
                if self.eta_adaptative: self.adapt_eta(error)
                #print(str(iteration) + "\t" + str(self.eta) + "\t" + str(error))

    def get_best_alpha(self, A, Y):
        g = lambda x: self.g(x, self.params)

        i = len(self.theta_weights) - 1
        min_eta = 0
        max_eta = 5
        step = 0.001
        best_eta = self.eta
        best_error = -1
        for x in np.arange(min_eta, max_eta, step):
            w = self.theta_weights[i] - x * self.gradients[i] \
                + self.momentum * self.previous_gradients[i]
            aux = np.matmul(A[-2], w.transpose())
            # Activation Function
            output_layer = g(aux)
            error_now = 0.5 * np.sum(np.power(Y - output_layer, 2))
            if best_error == -1 or error_now < best_error:
                best_eta = x
                best_error = error_now

        return best_eta


    def adapt_eta(self, error):
        if self.last_error == 0:
            self.last_error = error
        elif self.last_error < error:
            #increasing
            self.decreasing_errors = 0
            self.increasing_errors +=1
        elif self.last_error > error:
            #decreasing
            self.decreasing_errors += 1
            self.increasing_errors = 0
        if self.decreasing_errors >= self.iter_update_eta:
            self.eta += self.eta_increment
            self.decreasing_errors = 0
        if self.increasing_errors >= self.iter_update_eta:
            self.eta *= self.eta_decrement
            if self.eta < self.min_eta:
                self.eta = self.min_eta
            self.increasing_errors = 0
        self.last_error = error
        return

    def predict(self, X):
        # Predict output value for X input.
        t = lambda x: self.range_transform(self.input_range, x, self.output_range)

        A = self.feedforward(X)
        # output is last value
        output = A[-1]
        return t(output)

    def backpropagation(self, X, Y):
        # Implementation of the Backpropagation algorithm with regularization

        g_dz = lambda x: self.g_prima(x, self.params)

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
        return gradients, last_delta, A

    def feedforward(self, X):
        # Implementation of the Feedforward

        g = lambda x: self.g(x, self.params)

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
