# Imports
import pandas as pd
import matplotlib.pyplot as plt

# Definiciones
labels = ["Granularidad Fina", "Sincronización Optimista", 
            "Sincronización Sin Locks"]

# Funciones
def leer_tiempos_csv(dataframe):
    tiempos = []
    for i in range(0,3):
        tiempos.append([int(x[:-4]) for x in dataframe.iloc[:,i]])
    return tiempos     

def leer_valores_csv(dataframe, indices):
    valores = []
    for i in indices:
        valores.append([int(x) for x in dataframe.iloc[:,i]])
    return valores

def plotear(exp, tiempos, valores, leyenda):
    fig, ax = plt.subplots()
    for i in range(0,3):
        ax.plot(valores, tiempos[i], label=leyenda[i])
    ax.legend()
    ax.set_title(f"Experimento {exp}")
    ax.set_ylabel('Tiempo (ms)')
    if (exp == 1):
        ax.set_xlabel('Porcentaje de hilos Agregar/Contiene/Remover')
    else:
        ax.set_xlabel('Cantidad de hilos')
    return fig

def creacion_png(exp, indices, leyenda):
    df = pd.read_csv(f"experimento_{exp}.csv", delimiter=',', header=0)
    tiempos = leer_tiempos_csv(df)
    valores = leer_valores_csv(df, indices)
    if (exp == 1):
        lista_aux = []
        for i in range(len(valores[0])):
            lista_aux.append(f"{int(valores[0][i]/100)}" + "/" + \
                             f"{int(valores[1][i]/100)}" + "/" + \
                             f"{int(valores[2][i]/100)}")
        valores = lista_aux
    else:
        valores = valores[0]
    figura = plotear(exp, tiempos, valores, leyenda)
    figura.savefig(f"experimento_{exp}.png")

# Cuerpo
if __name__ == '__main__':
    # Experimento 1
    creacion_png(1, [5,6,7], labels)
    # Experimento 2
    creacion_png(2, [3], labels)
    # Experimento 3
    creacion_png(3, [3], labels)

    
    

