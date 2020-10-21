import numpy as np
import sys, math, time
import matplotlib.pyplot as plt


class SOM(object):
    def __init__(self):
        pass

    def create(self, width, height, input_dim):
        self.x = width  # Map width
        self.y = height  # Map height
        self.ch = input_dim
        self.trained = False

    def get_map_vectors(self):
        # Returns the map vectors.
        if self.trained:
            return self.node_vectors
        else:
            return False

    def distance(self, vect_a, vect_b):
        if self.dist_method == 'euclidean':
            dist = np.linalg.norm(vect_a - vect_b)
        elif self.dist_method == 'cosine':
            dist = 1. - np.dot(vect_a, vect_b) / (np.linalg.norm(vect_a) * np.linalg.norm(vect_b))
        return dist

    def find_maching_nodes(self, input_arr):
        # This is to be called only when the map is trained.
        if self.trained == False:
            return False

        n_data = input_arr.shape[0]
        locations = np.zeros((n_data, 2), dtype=np.int32)
        distances = np.zeros((n_data), dtype=np.float32)

        print_step = int(n_data / 20)
        print_count = 0
        for idx in range(n_data):

            if idx % print_step == 0:
                print_count += 1
                sys.stdout.write(f'\rFinding mathing nodes' +
                                 ' [' + '=' * (print_count) + '>' + '.' * (20 - print_count) + '] ')

            data_vect = input_arr[idx]
            min_dist = None
            x = None
            y = None
            for y_idx in range(self.y):
                for x_idx in range(self.x):
                    node_vect = self.node_vectors[y_idx, x_idx]
                    dist = self.distance(data_vect, node_vect)
                    if min_dist is None or min_dist > dist:
                        min_dist = dist
                        x = x_idx
                        y = y_idx

            locations[idx, 0] = y
            locations[idx, 1] = x
            distances[idx] = min_dist

        print('Done')
        return locations, distances

    def initialize_map(self):
        self.node_vectors = np.random.rand(self.y, self.x, self.ch) * 2 - 1

    def fit(self, input_arr, n_iter, batch_size=32, lr=0.25,
            neighbor_dist=None, dist_method='euclidean'):
        self.input_arr = input_arr
        self.n_iter = n_iter
        self.batch_size = batch_size
        self.dist_method = dist_method

        start_time = time.time()
        self.initialize_map()

        # Learning rate. This defines how fast the node weights are updated.
        self.lr = lr
        self.lr_decay = 0.995  # lr decay per iteration

        # Neighbor node coverage.
        # This tells that how far from the best matching node the
        # other nodes are updated.
        #if neighbor_dist is None:
        #    neighbor_dist =
        self.nb_dist = 2 * min(self.x, self.y) / 3# int(neighbor_dist)
        self.nb_decay = 0.995

        avg_dist = []



        # Add padding to avoid out of index in neighbours
        tmp_node_vects = np.zeros((self.y + 2 * int(np.ceil(self.nb_dist)), self.x + 2 * int(np.ceil(self.nb_dist)), self.ch))
        tmp_node_vects[int(np.ceil(self.nb_dist)): int(np.ceil(self.nb_dist)) + self.y,
        int(np.ceil(self.nb_dist)): int(np.ceil(self.nb_dist)) + self.x] = self.node_vectors.copy()
        self.node_vectors = tmp_node_vects


        n_data_pts = int(self.input_arr.shape[0])

        data_idx_arr = np.arange(self.input_arr.shape[0])
        n_per_report_step = int(n_data_pts / 20)

        for iteration in range(self.n_iter):

            # Shuffle the data indexes.
            np.random.shuffle(data_idx_arr)

            # Temporary variables
            total_dist = 0
            total_count = 0
            print_count = 0

            for idx in range(data_idx_arr.shape[0]):

                bm_node_idx_arr = np.zeros((1, 3), dtype=np.int32)

                if total_count % n_per_report_step == 0:
                    print_count += 1
                    sys.stdout.write(f'\rProcessing SOM iteration {iteration + 1}/{self.n_iter}' + \
                                     ' [' + '=' * (print_count) + '>' + '.' * (20 - print_count) + ']')
                total_count += 1

                # best matching node
                input_idx = data_idx_arr[idx]
                input_vect = self.input_arr[input_idx]
                y, x, dist = self.find_best_matching_node(input_vect)
                bm_node_idx_arr[0, 0] = y
                bm_node_idx_arr[0, 1] = x
                bm_node_idx_arr[0, 2] = input_idx
                total_dist += dist

                self.update_node_vectors(bm_node_idx_arr)

            # Print the average input data distance to the best matching node in the map.
            print(f' Average distance = {total_dist / n_data_pts:0.8f}')

            avg_dist.append(total_dist / n_data_pts)

            # Update the learnig rate
            self.lr *= self.lr_decay

            #Update Neighbours
            self.nb_dist = self.nb_dist * self.nb_decay
            if self.nb_dist < 1:
                self.nb_dist = 1

            print(f'Nb_dist = {self.nb_dist} ; lr = {self.lr}')

        # Remove padding from the vector map
        self.node_vectors = self.node_vectors[int(np.ceil(self.nb_dist)): int(np.ceil(self.nb_dist)) + self.y,
                            int(np.ceil(self.nb_dist)): int(np.ceil(self.nb_dist)) + self.x]

        del self.input_arr

        end_time = time.time()
        self.trained = True
        plot_error(avg_dist)

    def update_node_vectors(self, bm_node_idx_arr):

        for idx in range(bm_node_idx_arr.shape[0]):
            node_y = bm_node_idx_arr[idx, 0]
            node_x = bm_node_idx_arr[idx, 1]
            inp_idx = bm_node_idx_arr[idx, 2]
            input_vect = self.input_arr[inp_idx]

            for y_idx in range(self.y):
                for x_idx in range(self.x):
                    node_vect = self.node_vectors[y_idx + int(np.ceil(self.nb_dist)), x_idx + int(np.ceil(self.nb_dist))]

                    dist = (y_idx-node_y)*(y_idx-node_y) +  (x_idx-node_x)*(x_idx-node_x)
                    dist = np.sqrt(dist)

                    if dist <= self.nb_dist:

                        update_vect = self.lr * (np.expand_dims(input_vect, axis=0) - node_vect)
                        self.node_vectors[y_idx+ int(np.ceil(self.nb_dist)),
                        x_idx+ int(np.ceil(self.nb_dist)), :] += update_vect.reshape(-1)



    def find_best_matching_node(self, data_vect):
        # only for fitting purposes

        min_dist = None
        x = None
        y = None
        for y_idx in range(self.y):
            for x_idx in range(self.x):
                node_vect = self.node_vectors[y_idx + int(np.ceil(self.nb_dist)), x_idx + int(np.ceil(self.nb_dist))]
                dist = self.distance(data_vect, node_vect)
                if min_dist is None or min_dist > dist:
                    min_dist = dist
                    x = x_idx
                    y = y_idx

        return y, x, min_dist


    def get_umatrix(self):

        if not self.trained:
            return False

        umatrix = np.zeros((self.y, self.x))

        for map_y in range(self.y):
            for map_x in range(self.x):

                n_dist = 0
                total_dist = 0

                if map_y > 0:
                    dist_up = self.distance(self.node_vectors[map_y, map_x],
                                            self.node_vectors[map_y - 1, map_x])
                    total_dist += dist_up
                    n_dist += 1

                if map_y < self.y - 1:
                    dist_down = self.distance(self.node_vectors[map_y, map_x],
                                              self.node_vectors[map_y + 1, map_x])
                    total_dist += dist_down
                    n_dist += 1

                if map_x > 0:
                    dist_left = self.distance(self.node_vectors[map_y, map_x],
                                              self.node_vectors[map_y, map_x - 1])
                    total_dist += dist_left
                    n_dist += 1

                if map_x < self.x - 1:
                    dist_right = self.distance(self.node_vectors[map_y, map_x],
                                               self.node_vectors[map_y, map_x + 1])
                    total_dist += dist_right
                    n_dist += 1

                avg_dist = total_dist / n_dist
                umatrix[map_y, map_x] = avg_dist

        return umatrix

    def get_component_plane(self, component):
        if not self.trained:
            return False
        cplane = self.node_vectors[:, :, component].copy()
        return cplane

def plot_error(errors):
    x_coord = [i for i in range(len(errors))]
    plt.plot(x_coord, errors)
    plt.ylabel("Distancia Media a Neurona Ganadora")
    plt.xlabel("Epoca")
    plt.show()

def plot_data_on_map(umatrix, data_locations, data_colors, data_labels=None,
                     node_width=20,
                     node_edge_color=0,
                     data_marker_size=100,
                     invert_umatrix=True,
                     plot_labels=False,
                     dpi=200,
                     names=None):
    map_x = umatrix.shape[1]
    map_y = umatrix.shape[0]
    canvas = np.zeros((map_y * node_width, map_x * node_width))


    tmp_umatrix = umatrix.copy()
    tmp_umatrix -= np.min(tmp_umatrix)
    tmp_umatrix /= np.max(tmp_umatrix)

    if invert_umatrix:
        tmp_umatrix = 1 - tmp_umatrix

    for y in range(map_y):
        for x in range(map_x):
            canvas[y * node_width: (y + 1) * node_width,
            x * node_width: (x + 1) * node_width] = tmp_umatrix[y, x]

    if not node_edge_color is None:

        for y in range(map_y):
            canvas[y * node_width, :] = node_edge_color

        for x in range(map_x):
            canvas[:, x * node_width] = node_edge_color

            # Plot the SOM u-matrix as background
    plt.figure(figsize=(map_x * node_width / dpi, map_y * node_width / dpi), dpi=dpi)

    plt.imshow(canvas, cmap='gray', interpolation='hanning')

    # Initialize some temp variables
    item_count_map = np.zeros(umatrix.shape)
    n_data_pts = data_locations.shape[0]

    for i in range(n_data_pts):

        x = data_locations[i, 1]
        y = data_locations[i, 0]
        items_in_cell = item_count_map[y, x]
        item_count_map[y, x] += 1
        x = x * node_width + node_width // 2 + items_in_cell * 5
        y = y * node_width + node_width // 2 + items_in_cell * 5
        plt.scatter(x, y, s=data_marker_size, color=data_colors[i], edgecolors=[0, 0, 0])
        plt.text(x, y, names[i], color="blue", fontsize=5)

        if plot_labels:
            plt.annotate(str(data_labels[i]), (x + 8, y), size='small')

        plt.axis('off')

    filename = 'SOM_mapping_' + str(int(time.time())) + '.png'
    plt.savefig(filename)
    plt.show()
    print(f'Image saved to {filename}')