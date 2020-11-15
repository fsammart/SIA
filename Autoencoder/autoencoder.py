from __future__ import print_function, division
import matplotlib.pyplot as plt
import numpy as np
import copy
import pickle
import math

from DeepNetwork.optimizers import Adam, Adagrad, StochasticGradientDescent,NesterovAcceleratedGradient
from DeepNetwork.loss_functions import SquareLoss
from DeepNetwork.layers import Dense, Activation, BatchNormalization
from DeepNetwork import NeuralNetwork


class Autoencoder():
    """Autoencoder
    """
    def __init__(self, img_rows, img_cols, latent_dim):
        self.img_rows = img_rows
        self.img_cols = img_cols
        self.img_dim = self.img_rows * self.img_cols
        self.latent_dim = latent_dim # The dimension of the data embedding

        optimizer = StochasticGradientDescent(learning_rate=0.001, momentum=0.5)
        loss_function = SquareLoss

        self.encoder = self.create_encoder(optimizer, loss_function)
        self.decoder = self.create_decoder(optimizer, loss_function)

        self.autoencoder = NeuralNetwork(optimizer=optimizer, loss=loss_function)
        self.autoencoder.layers.extend(self.encoder.layers)
        self.autoencoder.layers.extend(self.decoder.layers)

        print ()
        self.autoencoder.summary(name="Variational Autoencoder")

    def create_encoder(self, optimizer, loss_function):

        encoder = NeuralNetwork(optimizer=optimizer, loss=loss_function)
        encoder.add(Dense(64, input_shape=(self.img_dim,)))
        encoder.add(Activation('leaky_relu'))
        encoder.add(BatchNormalization(momentum=0.8))
        encoder.add(Dense(32))
        encoder.add(Activation('leaky_relu'))
        encoder.add(BatchNormalization(momentum=0.8))
        encoder.add(Dense(self.latent_dim))
        encoder.add(Activation('tanh'))

        return encoder

    def create_decoder(self, optimizer, loss_function):

        decoder = NeuralNetwork(optimizer=optimizer, loss=loss_function)
        decoder.add(Dense(32, input_shape=(self.latent_dim,)))
        decoder.add(Activation('leaky_relu'))
        decoder.add(BatchNormalization(momentum=0.8))
        decoder.add(Dense(64))
        decoder.add(Activation('leaky_relu'))
        decoder.add(BatchNormalization(momentum=0.8))
        decoder.add(Dense(self.img_dim))
        decoder.add(Activation('tanh'))

        return decoder

    def train(self, n_epochs, batch_size=128, save_interval=50, train_data=None):
        min_error = 10
        best_auto = None
        best_enc = None
        best_dec = None

        X = train_data
        f = open("erros_epochs.tsv", "w+")

        # Rescale [-1, 1]. Not needed because input is {-1,1}

        for epoch in range(n_epochs):
            # Select a random half batch of images
            idx = np.random.randint(0, X.shape[0], batch_size)
            imgs = X[idx]

            # Train the Autoencoder
            loss, _ = self.autoencoder.train_on_batch(imgs, imgs)

            # Display the progress
            print ("%d [D loss: %f]" % (epoch, loss))
            if loss < min_error:
                best_auto = copy.deepcopy(self.autoencoder)
                best_enc = copy.deepcopy(self.encoder)
                best_dec = copy.deepcopy(self.decoder)
                min_error = loss
                f.write("%d %f\n" % (epoch, loss))
                #self.save_imgs(min_error, X)

            # If at save interval => save generated image samples
            if epoch % save_interval == 0:
                #self.save_imgs(epoch, X)
                a=3

        self.save_imgs2(min_error, X, best_auto)
        # Check latent space
        latent = best_enc.predict(X)
        self.plot_latent(latent)

        # from f to { 18 - 14. 2 points
        x_r,y_r = latent[27]
        x_n, y_n = latent[6]
        m = (y_r - y_n )/(x_r - x_n )
        d = x_r - x_n
        x,y = x_n,y_n

        for i in np.arange(0,d,d/10):
            self.generate_img(np.array([x + i,y + i *m]),i, best_dec,"f-{")

        self.generate_img(np.array([x_r, y_r]), d, best_dec,"f-{")

        # from r to n 18 - 14. 2 points
        x_r, y_r = latent[18]
        x_n, y_n = latent[14]
        m = (y_r - y_n) / (x_r - x_n)
        d = x_r - x_n
        x, y = x_n, y_n

        for i in np.arange(0, d, d / 10) :
            self.generate_img(np.array([x + i, y + i * m]), i, best_dec, "r-n")

        self.generate_img(np.array([x_r, y_r]), d, best_dec, "r-n")

        # from r to n 18 - 14. 2 points
        x_r, y_r = latent[21]
        x_n, y_n = latent[23]
        m = (y_r - y_n) / (x_r - x_n)
        d = x_r - x_n
        x, y = x_n, y_n

        for i in np.arange(0, d, d / 10) :
            self.generate_img(np.array([x + i, y + i * m]), i, best_dec, "u-w")

        self.generate_img(np.array([x_r, y_r]), d, best_dec, "u-w")

        # from r to n 18 - 14. 2 points
        x_r, y_r = latent[8]
        x_n, y_n = latent[11]
        m = (y_r - y_n) / (x_r - x_n)
        d = x_r - x_n
        x, y = x_n, y_n

        for i in np.arange(0, d, d / 10) :
            self.generate_img(np.array([x + i, y + i * m]), i, best_dec, "h-k")

        self.generate_img(np.array([x_r, y_r]), d, best_dec, "h-k")


        #from

        #self.save_object(best_auto, "encoder" + str(min_error) + ".nn")




    def plot_latent(self, X, names=None):
        plt.suptitle("Latent Space")
        fig, ax = plt.subplots()
        names = ['`','a','b','c','d','e',
                 'f','g','h','i','j','k',
                 'l','m','n','o','p','q',
                 'r','s','t','u','v','w','x','y','z','{','}','~',"DEL"]
        plt.scatter(X[:,0], X[:,1])
        for i, txt in enumerate(names) :
            ax.annotate(txt, (X[i,0], X[i,1]))
        plt.show()

    def generate_img(self, X, d, decoder, name="transition"):
        print(X)
        # Generate images and reshape to image shape
        gen_imgs = decoder.predict(X).reshape((-1, self.img_rows, self.img_cols))

        # Rescale images 0 - 1
        gen_imgs = 0.5 * gen_imgs + 0.5


        fig, axs = plt.subplots()
        plt.suptitle("Transition%f.png" % d)
        cnt = 0
        axs.imshow(gen_imgs[cnt, :, :], cmap='gray_r')
        axs.axis('off')

        fig.savefig("letters/" + name + "-trans_%f.png" % d)
        plt.close()
    def save_imgs2(self, epoch, X, autoencoder):
        r, c = 8, 4 # Grid size
        # Select a random half batch of images
        #idx = np.random.randint(0, X.shape[0], r*c)
        imgs = X
        # Generate images and reshape to image shape
        gen_imgs = autoencoder.predict(imgs).reshape((-1, self.img_rows, self.img_cols))

        # Rescale images 0 - 1
        gen_imgs = 0.5 * gen_imgs + 0.5

        fig, axs = plt.subplots(r, c)
        plt.suptitle("Autoencoder")
        cnt = 0
        for i in range(r):
            for j in range(c):
                axs[i,j].imshow(gen_imgs[cnt,:,:], cmap='gray_r')
                axs[i,j].axis('off')
                cnt += 1
        fig.savefig("ae_%f.png" % epoch)
        plt.close()

    def save_imgs(self, epoch, X):
        r, c = 8, 4 # Grid size
        # Select a random half batch of images
        #idx = np.random.randint(0, X.shape[0], r*c)
        imgs = X
        # Generate images and reshape to image shape
        gen_imgs = self.autoencoder.predict(imgs).reshape((-1, self.img_rows, self.img_cols))

        # Rescale images 0 - 1
        gen_imgs = 0.5 * gen_imgs + 0.5

        fig, axs = plt.subplots(r, c)
        plt.suptitle("Autoencoder")
        cnt = 0
        for i in range(r):
            for j in range(c):
                axs[i,j].imshow(gen_imgs[cnt,:,:], cmap='gray_r')
                axs[i,j].axis('off')
                cnt += 1
        fig.savefig("ae_%f.png" % epoch)
        plt.close()

    def save_object (self, obj, filename) :
        with open(filename, 'wb') as output :  # Overwrites any existing file.
            pickle.dump(obj, output)



if __name__ == '__main__':
    fonts = np.array([
        [0x04, 0x04, 0x02, 0x00, 0x00, 0x00, 0x00],
        [0x00, 0x0e, 0x01, 0x0d, 0x13, 0x13, 0x0d],
        [0x10, 0x10, 0x10, 0x1c, 0x12, 0x12, 0x1c],
        [0x00, 0x00, 0x00, 0x0e, 0x10, 0x10, 0x0e],
        [0x01, 0x01, 0x01, 0x07, 0x09, 0x09, 0x07],
        [0x00, 0x00, 0x0e, 0x11, 0x1f, 0x10, 0x0f],
        [0x06, 0x09, 0x08, 0x1c, 0x08, 0x08, 0x08],
        [0x0e, 0x11, 0x13, 0x0d, 0x01, 0x01, 0x0e],
        [0x10, 0x10, 0x10, 0x16, 0x19, 0x11, 0x11],
        [0x00, 0x04, 0x00, 0x0c, 0x04, 0x04, 0x0e],
        [0x02, 0x00, 0x06, 0x02, 0x02, 0x12, 0x0c],
        [0x10, 0x10, 0x12, 0x14, 0x18, 0x14, 0x12],
        [0x0c, 0x04, 0x04, 0x04, 0x04, 0x04, 0x04],
        [0x00, 0x00, 0x0a, 0x15, 0x15, 0x11, 0x11],
        [0x00, 0x00, 0x16, 0x19, 0x11, 0x11, 0x11],
        [0x00, 0x00, 0x0e, 0x11, 0x11, 0x11, 0x0e],
        [0x00, 0x1c, 0x12, 0x12, 0x1c, 0x10, 0x10],
        [0x00, 0x07, 0x09, 0x09, 0x07, 0x01, 0x01],
        [0x00, 0x00, 0x16, 0x19, 0x10, 0x10, 0x10],
        [0x00, 0x00, 0x0f, 0x10, 0x0e, 0x01, 0x1e],
        [0x08, 0x08, 0x1c, 0x08, 0x08, 0x09, 0x06],
        [0x00, 0x00, 0x11, 0x11, 0x11, 0x13, 0x0d],
        [0x00, 0x00, 0x11, 0x11, 0x11, 0x0a, 0x04],
        [0x00, 0x00, 0x11, 0x11, 0x15, 0x15, 0x0a],
        [0x00, 0x00, 0x11, 0x0a, 0x04, 0x0a, 0x11],
        [0x00, 0x11, 0x11, 0x0f, 0x01, 0x11, 0x0e],
        [0x00, 0x00, 0x1f, 0x02, 0x04, 0x08, 0x1f],
        [0x06, 0x08, 0x08, 0x10, 0x08, 0x08, 0x06],
        [0x04, 0x04, 0x04, 0x00, 0x04, 0x04, 0x04],
        [0x0c, 0x02, 0x02, 0x01, 0x02, 0x02, 0x0c],
        [0x08, 0x15, 0x02, 0x00, 0x00, 0x00, 0x00],
        [0x1f, 0x1f, 0x1f, 0x1f, 0x1f, 0x1f, 0x1f]
    ])
    fonts2 = np.zeros(shape=(32,35))
    for idx, elem in enumerate(fonts):
        for idx2,line in enumerate(elem):
            #10000
            mask = 0b10000
            value = mask & line >0
            fonts2[idx][idx2 * 5 + 0] = 1 if value else -1
            #01000
            mask = 0b01000
            value = mask & line >0
            fonts2[idx][idx2* 5  + 1] =1 if value else -1
            #00100
            mask = 0b00100
            value = mask & line >0
            fonts2[idx][idx2* 5  + 2] = 1 if value else -1
            #00010
            mask = 0b00010
            value = mask & line >0
            fonts2[idx][idx2 * 5 + 3] = 1 if value else -1
            #00001
            mask = 0b00001
            value = mask & line>0
            fonts2[idx][idx2 * 5 + 4] = 1 if value else -1
    print(fonts2)

    ae = Autoencoder(img_rows=7, img_cols=5,latent_dim=2)
    ae.train(n_epochs=150001, batch_size=32, save_interval=1000, train_data=fonts2)



