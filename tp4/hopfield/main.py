from Hopfield import Hopfield
import os
import re
import numpy as np

letters = []

def main():

    current_path = os.getcwd()
    train_paths = []
    path = current_path+"/train_letters/"
    for i in os.listdir(path):
        if re.match(r'[0-9a-zA-Z-]*.txt',i):
            train_paths.append(path+i)

    test_paths = []
    path = current_path+"/test_letters/"
    for i in os.listdir(path):
        if re.match(r'[0-9a-zA-Z-_]*.txt',i):
            test_paths.append(path+i)

    added = generate_noise_patters(test_paths, current_path)
    for elem in added:
        test_paths.append(elem)
    #Hopfield network starts!
    Hopfield.hopfield(train_files=train_paths, test_files=test_paths, theta=0.5,time=20000,size=(5,5),threshold=60, current_path = current_path)

def choose_letters():
    current_path = os.getcwd()
    train_paths = []
    path = current_path + "/train_letters/"
    for i in os.listdir(path) :
        if re.match(r'[0-9a-zA-Z-]*.txt', i) :
            train_paths.append(path + i)
            letters.append(os.path.splitext(i)[0])


    dots = {}
    for idx1, elem1 in enumerate(train_paths) :
        for idx2, elem2 in enumerate(train_paths) :
            for idx3, elem3 in enumerate(train_paths) :
                for idx4, elem4 in enumerate(train_paths) :
                    if idx2 > idx1 and idx3>idx2 and idx4 > idx3:
                        aux = 0
                        if str(letters[idx2]) == "X" and  str(letters[idx1]) == "O" and str(letters[idx3]) == "D":
                            print("hola")
                        m1 = read_letter(elem1)
                        m2 = read_letter(elem2)
                        m3 = read_letter(elem3)
                        m4 = read_letter(elem4)
                        name = str(letters[idx1]) + "-" + str(letters[idx2]) + "-" + str(letters[idx3]) + "-" + str(letters[idx4])

                        auxes = [m1,m2,m3,m4]
                        for i,a in enumerate(auxes):
                            for j,b in enumerate(auxes):
                                if i > j:
                                    aux += abs(np.dot(a, b))
                        dots[name] = aux
                        #print(name + "->"+ str(aux))


    dot_sorted = {k : v for k, v in sorted(dots.items(), key=lambda item : abs(item[1]))}
    for elem in dot_sorted:
         print(elem + ":" + str(dot_sorted[elem]))
    #print(dot_sorted)


def random_noise(prob, v):
    changes = int(np.floor(len(v) * prob))
    idxs = np.random.choice(range(0,len(v)),size=changes)
    for idx in idxs :
        v[idx] = -1 * v[idx]

    return v

def generate_noise_patters(test_files, current_path):
    added =[]
    for path in test_files :
        matrixy = open(path).read()
        base = os.path.basename(path)
        name = os.path.splitext(base)[0]
        y = np.asarray(matrixy.split())
        y_vec = Hopfield.mat2vec(y)
        noises = [0.1,0.3,0.5,0.7,0.9,1]
        for noise in noises:
            v = random_noise(noise, y_vec.__copy__())
            v = v.reshape((5,5))
            outfile = current_path + "/test_letters/" + name + "-" + str(noise) + ".txt"
            added.append(outfile)
            with open(outfile, 'wb') as f :
                for line in v:
                    np.savetxt(f, line, fmt='%.0f', delimiter=' ', newline=' ')
                    f.write('\n'.encode())
    return added
def read_letter(path):
    m1 = open(path).read()
    m1 = np.asarray(m1.split())
    m1 = Hopfield.mat2vec(m1)
    return m1


if __name__ == '__main__':
    main()