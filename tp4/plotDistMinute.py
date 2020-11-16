#!/usr/bin/env python3

import matplotlib.pyplot as plt

import numpy as np


def toarray(file):
  f = open(file, "r")
  lines = f.readlines()
  a = []
  b = []
  k = 1000000000000000
  t = 0
  for l in lines:
  	# k = k + 1 
  	# print(k)
    array = l.split()
    # if float(array[1]) < k:
    	# k = float(array[1])
    	# t = float(array[0])/(60*60*24)
    a.append(float(array[0])/(60))
    b.append(float(array[1]))

  print(t)
  return a, b


x=[]
y=[]




x, y = toarray("excactDistanceMinute.txt")


plt.plot(x, y, color='blue')

fig = plt.gcf()
axes = fig.gca()

plt.xlabel('Minuto de despegue', fontsize=20)
plt.ylabel('Menor distancia a Marte (km)', fontsize=20)
axes.tick_params(axis='x', labelsize=16)
axes.tick_params(axis='y', labelsize=16)
axes.set_yscale('log')
plt.show()
