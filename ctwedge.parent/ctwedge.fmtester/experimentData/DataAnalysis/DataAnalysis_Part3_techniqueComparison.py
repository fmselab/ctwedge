# -*- coding: utf-8 -*-
"""
Created on Thu Jun 30 17:24:59 2022

@author: Luca Parimbelli
"""

#%%
import pandas as pd
import matplotlib.pyplot as plt

cols2skip = [0]  
cols = [i for i in range(36) if i not in cols2skip]

ACTS_Time = pd.read_excel(r"..\ACTS_Generation_from_scratch.xlsx", sheet_name="TIME (ms) for generating TS", header=1,  usecols=cols )
ACTS_Size = pd.read_excel(r"..\ACTS_Generation_from_scratch.xlsx", sheet_name="SIZE of the generated TS", header=1,  usecols=cols )

pMediciNormal_Time = pd.read_excel(r"..\pMEDICI_Generation_from_scratch.xlsx", sheet_name="TIME (ms) for generating TS", header=1,  usecols=cols )
pMediciNormal_Size = pd.read_excel(r"..\pMEDICI_Generation_from_scratch.xlsx", sheet_name="REDUCED SIZE of the TS", header=1,  usecols=cols )

pMediciPlus_Time = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_2Threads.xlsx", sheet_name="TIME (ms) for generating TS", header=1,  usecols=cols )
pMediciPlus_Size = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_2Threads.xlsx", sheet_name="REDUCED SIZE of the TS", header=1,  usecols=cols )

# siccome pMEDICI+ ha solo 25 evoluzioni, devo selezionare solo quelle anche per ACTS e pMEDICI
evolutionModelNames = ["AmbientAssistedLivingv2", "AutomotiveMultimediav2", "AutomotiveMultimediav3","Boeingv2","Boeingv3","CarBodyv2","CarBodyv3","CarBodyv4","LinuxKernelv2","LinuxKernelv3","ParkingAssistantv2","ParkingAssistantv3","ParkingAssistantv4", "ParkingAssistantv5", "PPUv2","PPUv3","PPUv4","PPUv5","PPUv6","PPUv7","PPUv8","PPUv9","SmartHotelv2","Smartwatchv2","WeatherStationv2"]
ACTS_Time = ACTS_Time[evolutionModelNames]
ACTS_Size =  ACTS_Size[evolutionModelNames]
pMediciNormal_Time = pMediciNormal_Time[evolutionModelNames]
pMediciNormal_Size = pMediciNormal_Size[evolutionModelNames]

black = "#000000"
grey = "#b4bbc4"
midGrey = "#56595d"

#%%
# 3.1 pMEDICI e ACTS vs pMEDICI+: impact of the technique 2 on the generation time


## setting subplot info
fig = plt.figure()

# Divide the figure into a 1x2 grid, and give me the first section
ax1 = fig.add_subplot(121)

# Divide the figure into a 1x2 grid, and give me the second section
ax2 = fig.add_subplot(122)

plt.subplots_adjust(left=0.125, bottom=0.9, right=1.2 , top=1.6 , wspace= 0.2, hspace= 0.25)

# TEMPO (grafico a sinistra)

# calcolo il tempo medio TOTALE (somma di tutti i tempi diviso numero totale di tutti i tempi)

dataSize_ACTS = ACTS_Time.shape[0]* ACTS_Time.shape[1]
ACTS_timeMean = ( (ACTS_Time.to_numpy().sum()) / dataSize_ACTS ).round(2)

dataSize_pMEDICI =  pMediciNormal_Time.shape[0]* pMediciNormal_Time.shape[1]
pMEDICINormal_timeMean = ( (pMediciNormal_Time.to_numpy().sum()) / dataSize_pMEDICI ).round(2)

dataSize_pMEDICIPlus = pMediciPlus_Time.shape[0]*pMediciPlus_Time.shape[1]
pMediciPlus_timeMean = ( (pMediciPlus_Time.to_numpy().sum()) / dataSize_pMEDICIPlus ).round(2)

colors=["#000000","#56595d","#b4bbc4"]

indexes = ["ACTS","pMEDICI", "pMEDICI+"]
df = pd.DataFrame([ACTS_timeMean,pMEDICINormal_timeMean, pMediciPlus_timeMean], index=indexes).transpose()
colors={"ACTS":'#b4bbc4', "pMEDICI":'#56595d', "pMEDICI+":'#000000'}



ax=df[["ACTS","pMEDICI", "pMEDICI+"]].plot(kind="barh",color=colors, figsize=(8,4), width=0.2, ax=ax1)

# rimuovere scritte asse y
ax.axes.get_yaxis().set_ticks([])

# per settare limiti asse x
ax.set_xlim(0, 140)

# label changing on both x and y axes
ax.set_ylabel("Generation Tool")
ax.set_xlabel("Time (ms)")

for bars in ax.containers:
    ax.bar_label(bars)

#ax.figure.savefig(r'..\3.1a_toolsTimeComparison.png', format='png', dpi=400,  bbox_inches='tight')

# DIMENSIONE (grafico a destra)

dataSize_ACTS = ACTS_Size.shape[0]* ACTS_Size.shape[1]
ACTS_sizeMean = ( (ACTS_Size.to_numpy().sum()) / dataSize_ACTS ).round(2)

dataSize_pMEDICI =  pMediciNormal_Size.shape[0]* pMediciNormal_Size.shape[1]
pMEDICINormal_sizeMean = ( (pMediciNormal_Size.to_numpy().sum()) / dataSize_pMEDICI ).round(2)

dataSize_pMEDICIPlus =  pMediciPlus_Size.shape[0]* pMediciPlus_Size.shape[1]
pMediciPlus_sizeMean = ( (pMediciPlus_Size.to_numpy().sum()) / dataSize_pMEDICIPlus ).round(2)

indexes = ["ACTS","pMEDICI", "pMEDICI+"]
df = pd.DataFrame([ACTS_sizeMean,pMEDICINormal_sizeMean, pMediciPlus_sizeMean], index=indexes).transpose()
colors={"ACTS":'#b4bbc4', "pMEDICI":'#56595d', "pMEDICI+":'#000000'}


ax=df[["ACTS","pMEDICI", "pMEDICI+"]].plot(kind="barh",color=colors, figsize=(8,4), width=0.2, ax=ax2)

# rimuovere scritte asse y
ax.axes.get_yaxis().set_ticks([])

# per settare limiti asse x
ax.set_xlim(0, 15)

# label changing on both x and y axes
ax.set_ylabel("Generation Tool")
ax.set_xlabel("Size (number of Test Cases)")

for bars in ax.containers:
    ax.bar_label(bars)
    
#fig.figure.savefig(r'..\3.1a_toolsTimeSizeComparison.png', format='png', dpi=400,  bbox_inches='tight')

#%% 3.1b) Confronto (box diagram) delle 3 tecniche dal punto di vista del tempo e della dimensione delle TS

# Setting subplot info
fig = plt.figure()

# Divide the figure into a 2x2 grid, and give me the first section
ax1 = fig.add_subplot(221)

# Divide the figure into a 2x2 grid, and give me the second section
ax2 = fig.add_subplot(222)

# Divide the figure into a 1x2 grid, and give me the third section
ax3 = fig.add_subplot(223)

# Divide the figure into a 1x2 grid, and give me the fourth section
ax4 = fig.add_subplot(224)

plt.subplots_adjust(left=0.125, bottom=0.9, right=1.2 , top=1.8, wspace= 0.35, hspace= 0.4)

ACTS_timeMean_perModel = ACTS_Time.mean()
ACTS_sizeMean_perModel = ACTS_Size.mean()

pMEDICINormal_timeMean_perModel = pMediciNormal_Time.mean()
pMEDICINormal_sizeMean_perModel = pMediciNormal_Size.mean()

pMEDICIPlus_timeMean_perModel = pMediciPlus_Time.mean()
pMEDICIPlus_sizeMean_perModel = pMediciPlus_Size.mean()

# TEMPO
index = evolutionModelNames

df = pd.DataFrame({'pMEDICI': pMEDICINormal_timeMean_perModel,
                   'pMEDICI+': pMEDICIPlus_timeMean_perModel}, index=index)

ax = df[['pMEDICI', 'pMEDICI+']].plot(kind='box', vert=False, grid=False, color=dict(boxes='#000000', whiskers='#000000', medians='#000000', caps='#000000'), whiskerprops=dict(linestyle='--', linewidth=1),widths=0.4,  ax=ax1, showfliers=False)
# per settare limiti asse x
#ax.set_xlim(11, 20)
ax.set_xlabel("Time (ms)")

df = pd.DataFrame({'ACTS': ACTS_timeMean_perModel}, index=index)

ax = df[['ACTS']].plot(kind='box', vert=False, grid=False, color=dict(boxes='#000000', whiskers='#000000', medians='#000000', caps='#000000'), whiskerprops=dict(linestyle='--', linewidth=1),widths=0.4, ax=ax3, showfliers=False)
# per settare limiti asse x
#ax.set_xlim(0, 250)
ax.set_xlabel("Time (ms)")

# DIMENSIONE
index = evolutionModelNames

df = pd.DataFrame({'pMEDICI': pMEDICINormal_sizeMean_perModel,
                   'pMEDICI+': pMEDICIPlus_sizeMean_perModel}, index=index)

ax = df[['pMEDICI', 'pMEDICI+']].plot(kind='box', vert=False, grid=False, color=dict(boxes='#000000', whiskers='#000000', medians='#000000', caps='#000000'), whiskerprops=dict(linestyle='--', linewidth=1),widths=0.4,  ax=ax2)
# per settare limiti asse x
ax.set_xlim(0, 25)
ax.set_xlabel("Size (number of Test Cases)")

df = pd.DataFrame({'ACTS': ACTS_sizeMean_perModel}, index=index)

ax = df[['ACTS']].plot(kind='box', vert=False, grid=False, color=dict(boxes='#000000', whiskers='#000000', medians='#000000', caps='#000000'), whiskerprops=dict(linestyle='--', linewidth=1),widths=0.4, ax=ax4)
# per settare limiti asse x
ax.set_xlim(0, 25)
ax.set_xlabel("Size (number of Test Cases)")

fig.figure.savefig(r'..\3.1b_toolsTimeSizeComparisonPerModelBox.png', format='png', dpi=400,  bbox_inches='tight')


#%% 3.2a) Confronto della distanza scatter diagram
technique1_distance_ACTS = pd.read_excel(r"..\EvolutionModels_Distances.xlsx", sheet_name="EvolutionDistanceACTS", header=1,  usecols=cols )

technique1_distance_pMEDICI = pd.read_excel(r"..\pMEDICINormal_Distances.xlsx", sheet_name="Distances pMEDICI", header=1,  usecols=cols )
technique1_distance_pMEDICI = pd.DataFrame(technique1_distance_pMEDICI.mean()).transpose()

technique2_distance = pd.read_excel(r"..\pMEDICIMT_Distances.xlsx", sheet_name="Distances pMEDICI+", header=1,  usecols=cols )
technique2_distance = pd.DataFrame(technique2_distance.mean()).transpose()

plt.scatter(evolutionModelNames, technique1_distance_ACTS[evolutionModelNames], color = grey, s=40) 
plt.xticks(rotation=90)

plt.scatter(evolutionModelNames, technique1_distance_pMEDICI[evolutionModelNames], s=20, color=midGrey) 
plt.xticks(rotation=90)  

plt.scatter(evolutionModelNames, technique2_distance[evolutionModelNames], s=5, color=black) 
plt.xticks(rotation=90)  



plt.ylim(-5, 80)

# changing the label on the x axis
distanceModelLabels = ["AmbientAssistedLiving-v1v2", "AutomotiveMultimedia-v1v2", "AutomotiveMultimedia-v2v3","Boeing-v1v2","Boeing-v2v3","CarBody-v1v2","CarBody-v2v3","CarBody-v3v4","LinuxKernel-v1v2","LinuxKernel-v2v3","ParkingAssistant-v1v2","ParkingAssistant-v2v3","ParkingAssistant-v3v4", "ParkingAssistant-v4v5", "PPU-v1v2","PPU-v2v3","PPU-v3v4","PPU-v4v5","PPU-v5v6","PPU-v6v7","PPU-v7v8","PPU-v8v9","SmartHotel-v1v2","Smartwatch-v1v2","WeatherStation-v1v2"]
plt.xticks(evolutionModelNames, labels=distanceModelLabels)

plt.ylabel('Distance (%) between Evolving Test Suites')
mylabels=["Technique1 (ACTS)", "Technique1 (pMEDICI)", "Technique2 (pMEDICI+)" ]
plt.legend(labels=mylabels, bbox_to_anchor=(1.5, 1.0))

#plt.savefig(r'..\3.2a_ACTSvspMEDICIPlus_DistancesComparisonPerModel.png', format='png', dpi=400,  bbox_inches='tight')

#%% 3.3a) Confronto dimensione casi di test mantenuti, percentuale riduzione test suite
pMEDICINormal_sizeMean_perModel = pd.DataFrame(pMediciNormal_Size.mean()).transpose()
pMEDICIPlus_sizeMean_perModel = pd.DataFrame(pMediciPlus_Size.mean()).transpose()

# riduzione percentuale x:
# grande - x*grande = piccolo
# (grande - piccolo) / grande = x
# x da moltiplicare per 100

grande = pMEDICINormal_sizeMean_perModel
piccolo = pMEDICIPlus_sizeMean_perModel

riduzione = grande - piccolo
riduzione = riduzione.divide(grande)
riduzionePerc = riduzione * 100
# percentuale riduzione casi di test pMEDICI+ rispetto a pMEDICI
riduzionePerc = riduzionePerc.round(2)

numOfKeepedTestCases = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_NumOfTestsKeptWithinTheEvolution.xlsx", sheet_name="Num of test cases kept", header=1,  usecols=cols )
pMediciPlus_size = pd.DataFrame(pMediciPlus_Size.mean()).transpose()
percKeepTestCases = numOfKeepedTestCases.divide(pMediciPlus_size).mul(100)

# percentuale di casi di test mantenuti della precedente test suite di ACTS
percKeepTestCases = percKeepTestCases.round(2)

for col in percKeepTestCases.columns:
    plt.scatter(percKeepTestCases[col], riduzionePerc[col], label=col, color=black, s=7)
plt.xlabel('% of kept Test Cases on the total number\nof Test Cases of pMEDICI+')
plt.ylabel('% reduction in size of the Test Suite of pMEDICI+ \n with respect to the one of pMEDICI')
plt.axhline(y=0, color=grey, linestyle='--', linewidth=0.6)

#plt.savefig(r'..\3.3a_KeptTestSuiteSizeRelationship.png', format='png', dpi=400,  bbox_inches='tight')


#%% 3.4a) Formula per comparazione

# Funzione per calcolo punto intersezione rette

def line_intersection(line1, line2):
    xdiff = (line1[0][0] - line1[1][0], line2[0][0] - line2[1][0])
    ydiff = (line1[0][1] - line1[1][1], line2[0][1] - line2[1][1])

    def det(a, b):
        return a[0] * b[1] - a[1] * b[0]

    div = det(xdiff, ydiff)
    if div == 0:
       raise Exception('lines do not intersect')

    d = (det(*line1), det(*line2))
    x = det(d, xdiff) / div
    y = det(d, ydiff) / div
    return x, y

dACTS = pd.read_excel(r"..\EvolutionModels_Distances.xlsx", sheet_name="EvolutionDistanceACTS", header=1,  usecols=cols )
dpMEDICI = pd.read_excel(r"..\pMEDICINormal_Distances.xlsx", sheet_name="Distances pMEDICI", header=1,  usecols=cols )
dpMEDICIplus = pd.read_excel(r"..\pMEDICIMT_Distances.xlsx", sheet_name="Distances pMEDICI+", header=1,  usecols=cols )

# calcolo distanza media
dataSize_dACTS = dACTS.shape[0]* dACTS.shape[1]
dACTS_mean = ( (dACTS.to_numpy().sum()) / dataSize_dACTS  )

dataSize_dpMEDICI = dpMEDICI.shape[0]* dpMEDICI.shape[1]
dpMEDICI_mean = ( (dpMEDICI.to_numpy().sum()) / dataSize_dpMEDICI  )

dataSize_dpMEDICIplus = dpMEDICIplus.shape[0]* dpMEDICIplus.shape[1]
dpMEDICIplus_mean = ( (dpMEDICIplus.to_numpy().sum()) / dataSize_dpMEDICIplus  )

# calcolo tempo medio
dataSize_ACTS = ACTS_Time.shape[0]* ACTS_Time.shape[1]
ACTS_timeMean = ( (ACTS_Time.to_numpy().sum()) / dataSize_ACTS )

dataSize_pMEDICI = pMediciNormal_Time.shape[0]*pMediciNormal_Time.shape[1]
pMedici_timeMean = ( (pMediciNormal_Time.to_numpy().sum()) / dataSize_pMEDICI )

dataSize_pMEDICIPlus = pMediciPlus_Time.shape[0]*pMediciPlus_Time.shape[1]
pMediciPlus_timeMean = ( (pMediciPlus_Time.to_numpy().sum()) / dataSize_pMEDICIPlus )

#%% 3.4a) Comparzione tecniche

import matplotlib.pyplot as plt
import numpy as np
x = np.linspace(0,26,100)

yACTS= ACTS_timeMean + dACTS_mean*x
ypMEDICIPlus = pMediciPlus_timeMean + dpMEDICIplus_mean*x
ypMEDICI = pMedici_timeMean + dpMEDICI_mean*x

plt.plot(x, yACTS, color=grey, label=r'Technique 1: $y_{ACTS}=t_{gen\_ACTS}+d_{ACTS}*x$', linewidth=1)
plt.plot(x, ypMEDICI, color=midGrey, label='Technique 1: $y_{pMEDICI}=t_{gen\_pMEDICI}+d_{pMEDICI}*x$', linewidth=1)
plt.plot(x, ypMEDICIPlus, color=black, label='Technique 2: $y_{pMEDICI+}=t_{gen\_pMEDICI+}+d_{pMEDICI+}*x$', linewidth=1)
plt.xlim(0,26)
plt.title('Evolving Test Suites - Techniques Comparison')
plt.xlabel('x (ms)', color='#1C2833')
plt.ylabel('y (ms)', color='#1C2833')
plt.legend(bbox_to_anchor=(1.0, -0.15))

# Determinazione primo punto di intersezione
A = (x[0], yACTS[0])
B = (x[-1],yACTS[-1])
C = (x[0], ypMEDICI[0])
D = (x[-1], ypMEDICI[-1])
x1,y1 = line_intersection((A, B), (C, D))
print("** INTERSEZIONE **")
print("x1: " +str(x1))
print("y1: " +str(y1))
plt.plot(x1, y1, '.', color=black, markersize=2)
plt.annotate('$x_1 ≈ 8.81$ ms', xy=(5.6,370), color=black, size=6)

# Determinazione secondo punto di intersezione
A = (x[0], yACTS[0])
B = (x[-1],yACTS[-1])
C = (x[0], ypMEDICIPlus[0])
D = (x[-1], ypMEDICIPlus[-1])
x2,y2 = line_intersection((A, B), (C, D))
print("x2: " +str(x2))
print("y2: " +str(y2))
plt.plot(x2, y2, '.', color=black, markersize=2)
plt.annotate('$x_2≈12.95$ ms', xy=(13,435), color=black, size=6)

#plt.savefig(r'..\3.4a_FinalFormulaComparison.png', format='png', dpi=400,  bbox_inches='tight')

#%% 3.4b) Formula comparazione per ogni modello (scegliere grafici significativi)
import numpy as np

# Vengono presentati grafici come 3.4a) per ogni modello dei 25 sotto esame
dACTS = pd.read_excel(r"..\EvolutionModels_Distances.xlsx", sheet_name="EvolutionDistanceACTS", header=1,  usecols=cols )
dpMEDICI = pd.read_excel(r"..\pMEDICINormal_Distances.xlsx", sheet_name="Distances pMEDICI", header=1,  usecols=cols )
dpMEDICIplus = pd.read_excel(r"..\pMEDICIMT_Distances.xlsx", sheet_name="Distances pMEDICI+", header=1,  usecols=cols )

# calcolo distanza media per ogni modello
dACTS_mean = dACTS.mean()
dpMEDICI_mean = dpMEDICI.mean()
dpMEDICIplus_mean = dpMEDICIplus.mean()

# calcolo tempo medio per ogni modello
ACTS_timeMean = ACTS_Time.mean()
pMedici_timeMean = pMediciNormal_Time.mean()
pMediciPlus_timeMean = pMediciPlus_Time.mean()

#%%
import matplotlib.pyplot as plt
import numpy as np
x = np.linspace(0,26,100)

# stampa di tutti i grafici per comprendere quali mettere in tesi
for col in evolutionModelNames:
    yACTS= ACTS_timeMean[col] + dACTS_mean[col]*x
    ypMEDICIPlus = pMediciPlus_timeMean[col] + dpMEDICIplus_mean[col]*x
    ypMEDICI = pMedici_timeMean[col] + dpMEDICI_mean[col]*x
    plt.plot(x, yACTS, color=grey, label=r'Technique 1: $y_{ACTS}=t_{gen\_ACTS}+d_{ACTS}*x$', linewidth=1)
    plt.plot(x, ypMEDICI, color=midGrey, label='Technique 1: $y_{pMEDICI}=t_{gen\_pMEDICI}+d_{pMEDICI}*x$', linewidth=1)
    plt.plot(x, ypMEDICIPlus, color=black, label='Technique 2: $y_{pMEDICI+}=t_{gen\_pMEDICI+}+d_{pMEDICI+}*x$', linewidth=1)
    plt.xlim(0,26)
    plt.title('Techniques Comparison: '+str(col))
    plt.xlabel('x (ms)', color='#1C2833')
    plt.ylabel('y (ms)', color='#1C2833')
    plt.legend(bbox_to_anchor=(1.0, -0.15))
    plt.show()

#%% 3.4b) PPUv6 - intersezione più a destra (eseguire codice 3.4b prima)
x = np.linspace(0,260,100)

yACTS= ACTS_timeMean["PPUv6"] + dACTS_mean["PPUv6"]*x
ypMEDICIPlus = pMediciPlus_timeMean["PPUv6"] + dpMEDICIplus_mean["PPUv6"]*x
ypMEDICI = pMedici_timeMean["PPUv6"] + dpMEDICI_mean["PPUv6"]*x
plt.plot(x, yACTS, color=grey, label=r'Technique 1: $y_{ACTS}=t_{gen\_ACTS}+d_{ACTS}*x$', linewidth=1)
plt.plot(x, ypMEDICI, color=midGrey, label='Technique 1: $y_{pMEDICI}=t_{gen\_pMEDICI}+d_{pMEDICI}*x$', linewidth=1)
plt.plot(x, ypMEDICIPlus, color=black, label='Technique 2: $y_{pMEDICI+}=t_{gen\_pMEDICI+}+d_{pMEDICI+}*x$', linewidth=1)
plt.xlim(0,250)
plt.title('Techniques Comparison: PPUv6')
plt.xlabel('x (ms)', color='#1C2833')
plt.ylabel('y (ms)', color='#1C2833')
plt.legend(bbox_to_anchor=(1.0, -0.15))

# Determinazione punto di intersezione
A = (x[0], yACTS[0])
B = (x[-1],yACTS[-1])
C = (x[0], ypMEDICIPlus[0])
D = (x[-1], ypMEDICIPlus[-1])
x1,y1 = line_intersection((A, B), (C, D))
print("** INTERSEZIONE **")
print("x1: " +str(x1))
print("y1: " +str(y1))
plt.plot(x1, y1, '.', color=black, markersize=2)
plt.annotate('$x ≈ 168.36$ ms', xy=(168, 520), color=black, size=6)

#plt.savefig(r'..\3.4b_PPUv6.png', format='png', dpi=400,  bbox_inches='tight')

#%% 3.4b) LinuxKernelv3 - intersezione più a sinistra (eseguire codice 3.4b prima)
x = np.linspace(0,6,100)

yACTS= ACTS_timeMean["LinuxKernelv3"] + dACTS_mean["LinuxKernelv3"]*x
ypMEDICIPlus = pMediciPlus_timeMean["LinuxKernelv3"] + dpMEDICIplus_mean["LinuxKernelv3"]*x
ypMEDICI = pMedici_timeMean["LinuxKernelv3"] + dpMEDICI_mean["LinuxKernelv3"]*x
plt.plot(x, yACTS, color=grey, label=r'Technique 1: $y_{ACTS}=t_{gen\_ACTS}+d_{ACTS}*x$', linewidth=1)
plt.plot(x, ypMEDICI, color=midGrey, label='Technique 1: $y_{pMEDICI}=t_{gen\_pMEDICI}+d_{pMEDICI}*x$', linewidth=1)
plt.plot(x, ypMEDICIPlus, color=black, label='Technique 2: $y_{pMEDICI+}=t_{gen\_pMEDICI+}+d_{pMEDICI+}*x$', linewidth=1)
plt.xlim(0,5)
plt.title('Techniques Comparison: LinuxKernelv3')
plt.xlabel('x (ms)', color='#1C2833')
plt.ylabel('y (ms)', color='#1C2833')
plt.legend(bbox_to_anchor=(1.0, -0.15))


# Determinazione punto intersezione
A = (x[0], yACTS[0])
B = (x[-1],yACTS[-1])
C = (x[0], ypMEDICIPlus[0])
D = (x[-1], ypMEDICIPlus[-1])
x1,y1 = line_intersection((A, B), (C, D))

# intersezione
plt.plot(x1, y1, '.', color=black, markersize=2)
print("** INTERSEZIONE **")
print("x1: " +str(x1))
print("y1: " +str(y1))
plt.annotate('$x ≈ 1.94$ ms', xy=(1.9, 65), color=black, size=6)

#plt.savefig(r'..\3.4a_LinuxKernelv3.png', format='png', dpi=400,  bbox_inches='tight')


#%% LinuxKernelv3 & PPUv6 insieme

plt.subplot(1, 2, 2) # PPUv6

x = np.linspace(0,260,100)

yACTS= ACTS_timeMean["PPUv6"] + dACTS_mean["PPUv6"]*x
ypMEDICIPlus = pMediciPlus_timeMean["PPUv6"] + dpMEDICIplus_mean["PPUv6"]*x
ypMEDICI = pMedici_timeMean["PPUv6"] + dpMEDICI_mean["PPUv6"]*x
plt.plot(x, yACTS, color=grey, label=r'Technique 1: $y_{ACTS}=t_{gen\_ACTS}+d_{ACTS}*x$', linewidth=1)
plt.plot(x, ypMEDICI, color=midGrey, label='Technique 1: $y_{pMEDICI}=t_{gen\_pMEDICI}+d_{pMEDICI}*x$', linewidth=1)
plt.plot(x, ypMEDICIPlus, color=black, label='Technique 2: $y_{pMEDICI+}=t_{gen\_pMEDICI+}+d_{pMEDICI+}*x$', linewidth=1)
plt.xlim(0,250)
plt.title('Techniques Comparison: PPUv6')
plt.xlabel('x (ms)', color='#1C2833')
plt.ylabel('y (ms)', color='#1C2833')
plt.legend(bbox_to_anchor=(1.0, -0.15))

# Determinazione punto di intersezione
A = (x[0], yACTS[0])
B = (x[-1],yACTS[-1])
C = (x[0], ypMEDICIPlus[0])
D = (x[-1], ypMEDICIPlus[-1])
x1,y1 = line_intersection((A, B), (C, D))
print("** INTERSEZIONE **")
print("x1: " +str(x1))
print("y1: " +str(y1))
plt.plot(x1, y1, '.', color=black, markersize=2)
plt.annotate('$x ≈ 168.36$ ms', xy=(168, 520), color=black, size=6)

plt.subplot(1, 2, 1) # LinuxKernelv3
x = np.linspace(0,6,100)

yACTS= ACTS_timeMean["LinuxKernelv3"] + dACTS_mean["LinuxKernelv3"]*x
ypMEDICIPlus = pMediciPlus_timeMean["LinuxKernelv3"] + dpMEDICIplus_mean["LinuxKernelv3"]*x
ypMEDICI = pMedici_timeMean["LinuxKernelv3"] + dpMEDICI_mean["LinuxKernelv3"]*x
plt.plot(x, yACTS, color=grey, label=r'Technique 1: $y_{ACTS}=t_{gen\_ACTS}+d_{ACTS}*x$', linewidth=1)
plt.plot(x, ypMEDICI, color=midGrey, label='Technique 1: $y_{pMEDICI}=t_{gen\_pMEDICI}+d_{pMEDICI}*x$', linewidth=1)
plt.plot(x, ypMEDICIPlus, color=black, label='Technique 2: $y_{pMEDICI+}=t_{gen\_pMEDICI+}+d_{pMEDICI+}*x$', linewidth=1)
plt.xlim(0,5)
plt.title('Techniques Comparison: LinuxKernelv3')
plt.xlabel('x (ms)', color='#1C2833')
plt.ylabel('y (ms)', color='#1C2833')
plt.legend(bbox_to_anchor=(1.0, -0.15))


# Determinazione punto intersezione
A = (x[0], yACTS[0])
B = (x[-1],yACTS[-1])
C = (x[0], ypMEDICIPlus[0])
D = (x[-1], ypMEDICIPlus[-1])
x1,y1 = line_intersection((A, B), (C, D))

# intersezione
plt.plot(x1, y1, '.', color=black, markersize=2)
print("** INTERSEZIONE **")
print("x1: " +str(x1))
print("y1: " +str(y1))
plt.annotate('$x ≈ 1.94$ ms', xy=(1.9, 65), color=black, size=6)

plt.subplots_adjust(left=0.125, bottom=0.9, right=1.5 , top=1.6 , wspace= 0.2, hspace= 0.22)

plt.savefig(r'..\3.4b_LinuxKernelv3_PPUv6_together.png', format='png', dpi=400,  bbox_inches='tight')

#%% 3.4b) Boeingv3 (eseguire codice 3.4b prima)
x = np.linspace(0,101,100)

yACTS= ACTS_timeMean["Boeingv3"] + dACTS_mean["Boeingv3"]*x
ypMEDICIPlus = pMediciPlus_timeMean["Boeingv3"] + dpMEDICIplus_mean["Boeingv3"]*x
ypMEDICI = pMedici_timeMean["Boeingv3"] + dpMEDICI_mean["Boeingv3"]*x
plt.plot(x, yACTS, color=grey, label=r'Technique 1: $y_{ACTS}=t_{gen\_ACTS}+d_{ACTS}*x$', linewidth=1)
plt.plot(x, ypMEDICI, color=midGrey, label='Technique 1: $y_{pMEDICI}=t_{gen\_pMEDICI}+d_{pMEDICI}*x$', linewidth=1)
plt.plot(x, ypMEDICIPlus, color=black, label='Technique 2: $y_{pMEDICI+}=t_{gen\_pMEDICI+}+d_{pMEDICI+}*x$', linewidth=1)
plt.xlim(0,100)
plt.title('Techniques Comparison: Boeingv3')
plt.xlabel('x (ms)', color='#1C2833')
plt.ylabel('y (ms)', color='#1C2833')
plt.legend(bbox_to_anchor=(1.0, -0.15))

# Determinazione punto di intersezione
A = (x[0], yACTS[0])
B = (x[-1],yACTS[-1])
C = (x[0], ypMEDICIPlus[0])
D = (x[-1], ypMEDICIPlus[-1])
#x1,y1 = line_intersection((A, B), (C, D))
#print("** INTERSEZIONE **")
#print("x1: " +str(x1))
#print("y1: " +str(y1))
#plt.plot(x1, y1, '.', color=black, markersize=2)
#lt.annotate('$x ≈ $ ms', xy=(168, 520), color=black, size=6)

plt.savefig(r'..\3.4b_Boeingv3.png', format='png', dpi=400,  bbox_inches='tight')

#%% 3.4b) CarBodyv3 (eseguire codice 3.4b prima)
x = np.linspace(0,50,100)

yACTS= ACTS_timeMean["CarBodyv3"] + dACTS_mean["CarBodyv3"]*x
ypMEDICIPlus = pMediciPlus_timeMean["CarBodyv3"] + dpMEDICIplus_mean["CarBodyv3"]*x
ypMEDICI = pMedici_timeMean["CarBodyv3"] + dpMEDICI_mean["CarBodyv3"]*x
plt.plot(x, yACTS, color=grey, label=r'Technique 1: $y_{ACTS}=t_{gen\_ACTS}+d_{ACTS}*x$', linewidth=1)
plt.plot(x, ypMEDICI, color=midGrey, label='Technique 1: $y_{pMEDICI}=t_{gen\_pMEDICI}+d_{pMEDICI}*x$', linewidth=1)
plt.plot(x, ypMEDICIPlus, color=black, label='Technique 2: $y_{pMEDICI+}=t_{gen\_pMEDICI+}+d_{pMEDICI+}*x$', linewidth=1)
plt.xlim(0,50)
plt.title('Techniques Comparison: CarBodyv3')
plt.xlabel('x (ms)', color='#1C2833')
plt.ylabel('y (ms)', color='#1C2833')
plt.legend(bbox_to_anchor=(1.0, -0.15))

plt.savefig(r'..\3.4b_CarBodyv3.png', format='png', dpi=400,  bbox_inches='tight')
 
#%% 3.4b) PPUv2 (eseguire codice 3.4b prima)

x = np.linspace(1140,1191,50000)

yACTS= ACTS_timeMean["PPUv2"] + dACTS_mean["PPUv2"]*x
ypMEDICIPlus = pMediciPlus_timeMean["PPUv2"] + dpMEDICIplus_mean["PPUv2"]*x
ypMEDICI = pMedici_timeMean["PPUv2"] + dpMEDICI_mean["PPUv2"]*x
plt.plot(x, yACTS, color=grey, label=r'Technique 1: $y_{ACTS}=t_{gen\_ACTS}+d_{ACTS}*x$', linewidth=1)
plt.plot(x, ypMEDICI, color=midGrey, label='Technique 1: $y_{pMEDICI}=t_{gen\_pMEDICI}+d_{pMEDICI}*x$', linewidth=1)
plt.plot(x, ypMEDICIPlus, color=black, label='Technique 2: $y_{pMEDICI+}=t_{gen\_pMEDICI+}+d_{pMEDICI+}*x$', linewidth=1)
plt.xlim(1140,1190)
plt.title('Techniques Comparison: PPUv2')
plt.xlabel('x (ms)', color='#1C2833')
plt.ylabel('y (ms)', color='#1C2833')
plt.legend(bbox_to_anchor=(1.0, -0.15))

# Determinazione intersezione ACTS-pMEDICI+
A = (x[0], yACTS[0])
B = (x[-1],yACTS[-1])
C = (x[0], ypMEDICIPlus[0])
D = (x[-1], ypMEDICIPlus[-1])
x1,y1 = line_intersection((A, B), (C, D))
print("ACTS-pMEDICI+")
print("** INTERSEZIONE **")
print("x1: " +str(x1))
print("y1: " +str(y1))
print()
print("yACTS(x1): "+str(ACTS_timeMean["PPUv2"] + dACTS_mean["PPUv2"]*x1))
print("ypMEDICIPlus(x1): "+str( pMediciPlus_timeMean["PPUv2"] + dpMEDICIplus_mean["PPUv2"]*x1))
print("ypMEDICI(x1): "+str(pMedici_timeMean["PPUv2"] + dpMEDICI_mean["PPUv2"]*x1))
print()
print("yACTS(1000): "+str(ACTS_timeMean["PPUv2"] + dACTS_mean["PPUv2"]*1000))
print("ypMEDICIPlus(1000): "+str( pMediciPlus_timeMean["PPUv2"] + dpMEDICIplus_mean["PPUv2"]*1000))
print("ypMEDICI(1000): "+str(pMedici_timeMean["PPUv2"] + dpMEDICI_mean["PPUv2"]*1000))
print()
print("yACTS(2000): "+str(ACTS_timeMean["PPUv2"] + dACTS_mean["PPUv2"]*2000))
print("ypMEDICIPlus(2000): "+str( pMediciPlus_timeMean["PPUv2"] + dpMEDICIplus_mean["PPUv2"]*2000))
print("ypMEDICI(2000): "+str(pMedici_timeMean["PPUv2"] + dpMEDICI_mean["PPUv2"]*2000))
plt.plot(x1, y1, '.', color=black, markersize=2)
plt.annotate('$x ≈ 1167.69$ ms', xy=(1168.5, 48940), color=black, size=6)



# Determinazione intersezione ACTS-pMEDICI punto di intersezione
A = (x[0], yACTS[0])
B = (x[-1],yACTS[-1])
C = (x[0], ypMEDICI[0])
D = (x[-1], ypMEDICI[-1])
x1,y1 = line_intersection((A, B), (C, D))
print()
print("ACTS-pMEDICI")
print("** INTERSEZIONE **")
print("x2: " +str(x1))
print("y2: " +str(y1))
print()
print("yACTS(x2): "+str(ACTS_timeMean["PPUv2"] + dACTS_mean["PPUv2"]*x2))
print("ypMEDICIPlus(x2): "+str( pMediciPlus_timeMean["PPUv2"] + dpMEDICIplus_mean["PPUv2"]*x2))
print("ypMEDICI(x2): "+str(pMedici_timeMean["PPUv2"] + dpMEDICI_mean["PPUv2"]*x2))

#plt.savefig(r'..\3.4b_PPUv2.png', format='png', dpi=400,  bbox_inches='tight')
