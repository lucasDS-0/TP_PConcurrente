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

def plotear(tiempos, valores, leyenda):
    fig, ax = plt.subplots()
    for i in range(0,3):
        ax.plot(valores, tiempos[i], label=leyenda[i])
    ax.legend()
    return fig

def creacion_svg(exp, indices, leyenda):
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
    figura = plotear(tiempos, valores, leyenda)
    figura.savefig(f"experimento_{exp}.svg")

# Cuerpo
if __name__ == '__main__':
    # Experimento 1
    creacion_svg(1, [5,6,7], labels)
    # Experimento 2
    creacion_svg(2, [3], labels)
    # Experimento 3
    creacion_svg(3, [3], labels)

    
    

