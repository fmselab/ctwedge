# -*- coding: utf-8 -*-
"""
Created on Thu Jun 30 14:17:16 2022

@author: Luca Parimbelli
"""

import pandas as pd
import matplotlib.pyplot as plt

cols2skip = [0]  
cols = [i for i in range(36) if i not in cols2skip]

ACTSFromScratch_Size = pd.read_excel(r"..\ACTS_Generation_from_scratch.xlsx", sheet_name="SIZE of the generated TS", header=1,  usecols=cols )

pMediciPlus8 = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_8Threads.xlsx", sheet_name="TIME (ms) for generating TS", header=1,  usecols=cols )
pMediciPlus8_filling = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_8Threads.xlsx", sheet_name="TIME (ms) early filling", header=1,  usecols=cols )
pMediciPlus8_reducedSize = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_8Threads.xlsx", sheet_name="REDUCED SIZE of the TS", header=1,  usecols=cols )
pMediciPlus8_sizeNotReduced = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_8Threads.xlsx", sheet_name="SIZE of the generated TS", header=1,  usecols=cols )


pMediciPlus7 = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_7Threads.xlsx", sheet_name="TIME (ms) for generating TS", header=1,  usecols=cols )
pMediciPlus7_filling = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_7Threads.xlsx", sheet_name="TIME (ms) early filling", header=1,  usecols=cols )
pMediciPlus7_reducedSize = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_7Threads.xlsx", sheet_name="REDUCED SIZE of the TS", header=1,  usecols=cols )
pMediciPlus7_sizeNotReduced = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_7Threads.xlsx", sheet_name="SIZE of the generated TS", header=1,  usecols=cols )


pMediciPlus6 = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_6Threads.xlsx", sheet_name="TIME (ms) for generating TS", header=1,  usecols=cols )
pMediciPlus6_filling = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_6Threads.xlsx", sheet_name="TIME (ms) early filling", header=1,  usecols=cols )
pMediciPlus6_reducedSize = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_6Threads.xlsx", sheet_name="REDUCED SIZE of the TS", header=1,  usecols=cols )
pMediciPlus6_sizeNotReduced = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_6Threads.xlsx", sheet_name="SIZE of the generated TS", header=1,  usecols=cols )


pMediciPlus5 = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_5Threads.xlsx", sheet_name="TIME (ms) for generating TS", header=1,  usecols=cols )
pMediciPlus5_filling = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_5Threads.xlsx", sheet_name="TIME (ms) early filling", header=1,  usecols=cols )
pMediciPlus5_reducedSize = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_5Threads.xlsx", sheet_name="REDUCED SIZE of the TS", header=1,  usecols=cols )
pMediciPlus5_sizeNotReduced = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_5Threads.xlsx", sheet_name="SIZE of the generated TS", header=1,  usecols=cols )


pMediciPlus4 = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_4Threads.xlsx", sheet_name="TIME (ms) for generating TS", header=1,  usecols=cols )
pMediciPlus4_filling = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_4Threads.xlsx", sheet_name="TIME (ms) early filling", header=1,  usecols=cols )
pMediciPlus4_reducedSize = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_4Threads.xlsx", sheet_name="REDUCED SIZE of the TS", header=1,  usecols=cols )
pMediciPlus4_sizeNotReduced = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_4Threads.xlsx", sheet_name="SIZE of the generated TS", header=1,  usecols=cols )


pMediciPlus3 = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_3Threads.xlsx", sheet_name="TIME (ms) for generating TS", header=1,  usecols=cols )
pMediciPlus3_filling = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_3Threads.xlsx", sheet_name="TIME (ms) early filling", header=1,  usecols=cols )
pMediciPlus3_reducedSize = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_3Threads.xlsx", sheet_name="REDUCED SIZE of the TS", header=1,  usecols=cols )
pMediciPlus3_sizeNotReduced = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_3Threads.xlsx", sheet_name="SIZE of the generated TS", header=1,  usecols=cols )


pMediciPlus2 = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_2Threads.xlsx", sheet_name="TIME (ms) for generating TS", header=1,  usecols=cols )
pMediciPlus2_filling = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_2Threads.xlsx", sheet_name="TIME (ms) early filling", header=1,  usecols=cols )
pMediciPlus2_reducedSize = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_2Threads.xlsx", sheet_name="REDUCED SIZE of the TS", header=1,  usecols=cols )
pMediciPlus2_sizeNotReduced = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_2Threads.xlsx", sheet_name="SIZE of the generated TS", header=1,  usecols=cols )

pMediciPlus1 = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_1Threads.xlsx", sheet_name="TIME (ms) for generating TS", header=1,  usecols=cols )
pMediciPlus1_filling = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_1Threads.xlsx", sheet_name="TIME (ms) early filling", header=1,  usecols=cols )
pMediciPlus1_reducedSize = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_1Threads.xlsx", sheet_name="REDUCED SIZE of the TS", header=1,  usecols=cols )
pMediciPlus1_sizeNotReduced = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_1Threads.xlsx", sheet_name="SIZE of the generated TS", header=1,  usecols=cols )

#%% 2.1 pMEDICI+: performances analysis and default values setting

# 2.1a) Impatto del numero di “TestEarlyFiller” threads sul tempo totale di esecuzione

numOfValues = pMediciPlus8.shape[0]* pMediciPlus8.shape[1]
allMean8= (pMediciPlus8.to_numpy().sum()) / numOfValues
allMean7= (pMediciPlus7.to_numpy().sum()) / numOfValues
allMean6= (pMediciPlus6.to_numpy().sum()) / numOfValues
allMean5= (pMediciPlus5.to_numpy().sum()) / numOfValues
allMean4= (pMediciPlus4.to_numpy().sum()) / numOfValues
allMean3= (pMediciPlus3.to_numpy().sum()) / numOfValues
allMean2= (pMediciPlus2.to_numpy().sum()) / numOfValues
allMean1= (pMediciPlus1.to_numpy().sum()) / numOfValues


allMean8_fill= (pMediciPlus8_filling .to_numpy().sum()) / numOfValues
allMean7_fill= (pMediciPlus7_filling .to_numpy().sum()) / numOfValues
allMean6_fill= (pMediciPlus6_filling .to_numpy().sum()) / numOfValues
allMean5_fill= (pMediciPlus5_filling .to_numpy().sum()) / numOfValues
allMean4_fill= (pMediciPlus4_filling .to_numpy().sum()) / numOfValues
allMean3_fill= (pMediciPlus3_filling .to_numpy().sum()) / numOfValues
allMean2_fill= (pMediciPlus2_filling .to_numpy().sum()) / numOfValues
allMean1_fill= (pMediciPlus1_filling .to_numpy().sum()) / numOfValues

threadNum = [1,2,3,4,5,6,7,8]
MeanTotalTime=[allMean1, allMean2, allMean3, allMean4,allMean5,allMean6,allMean7,allMean8]
threadsMeanTime_dict = {'Number of threads': threadNum, 'MeanTotalTime': MeanTotalTime}

MeanTotalTime_filling=[allMean1_fill, allMean2_fill, allMean3_fill, allMean4_fill,allMean5_fill,allMean6_fill,allMean7_fill,allMean8_fill]
threadsMeanTime_dict_filling = {'Number of threads': threadNum, 'MeanTotalTime': MeanTotalTime_filling}

# create DataFrame from dict
threadsMeanTime = pd.DataFrame(threadsMeanTime_dict)
threadsMeanTime_filling = pd.DataFrame(threadsMeanTime_dict_filling)

# set index using column
threadsMeanTime = threadsMeanTime.set_index('Number of threads')
threadsMeanTime=threadsMeanTime.round(2)

threadsMeanTime_filling = threadsMeanTime_filling.set_index('Number of threads')
threadsMeanTime_filling =threadsMeanTime_filling.round(2)

colors1 = ["#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4"]
colors2 = ["#000000","#000000","#000000","#000000","#000000","#000000","#000000","#000000"]

ax = threadsMeanTime["MeanTotalTime"].plot.bar(figsize=(10,6), color=colors1)
ay = threadsMeanTime_filling["MeanTotalTime"].plot.bar(figsize=(10,6), color=colors2)

# label changing on both x and y axes
ax.set_xlabel("Number of Threads")
ax.set_ylabel("Time (ms)")

ay.set_xlabel("Number of Threads")
ay.set_ylabel("Time (ms)")

   
# printing the values on the top of the bars
for bars in ax.containers:
    ax.bar_label(bars)
    
for bars in ay.containers:
    ay.bar_label(bars)

mylabels=["TotalTime","EarlyFillingTime"]
ax.legend(labels=mylabels, bbox_to_anchor=(1.0, 1.0))

#ax.figure.savefig(r'..\2.1a_pMEDICIPlus_ThreadsMeanTimeComparision.png', format='png', dpi=400,  bbox_inches='tight')

#%%
# 2.1b) Tempo per l’operazione iniziale di riempimento della tcList (test suite iniziale del nuovo modello a partire dalla vecchia) per i modelli con test suite più piccola (4 modelli) e più grande (4 modelli)

# media dei tempi di test early filling per numero di thread PER OGNI MODELLO
pMediciPlus8_filling_mean = pMediciPlus8_filling.mean()
pMediciPlus7_filling_mean = pMediciPlus7_filling.mean()
pMediciPlus6_filling_mean = pMediciPlus6_filling.mean()
pMediciPlus5_filling_mean = pMediciPlus5_filling.mean()
pMediciPlus4_filling_mean = pMediciPlus4_filling.mean()
pMediciPlus3_filling_mean = pMediciPlus3_filling.mean()
pMediciPlus2_filling_mean = pMediciPlus2_filling.mean()
pMediciPlus1_filling_mean = pMediciPlus1_filling.mean()

pMediciPlus8_filling_mean=pd.DataFrame(pMediciPlus8_filling_mean).transpose()
pMediciPlus7_filling_mean=pd.DataFrame(pMediciPlus7_filling_mean).transpose()
pMediciPlus6_filling_mean=pd.DataFrame(pMediciPlus6_filling_mean).transpose()
pMediciPlus5_filling_mean=pd.DataFrame(pMediciPlus5_filling_mean).transpose()
pMediciPlus4_filling_mean=pd.DataFrame(pMediciPlus4_filling_mean).transpose()
pMediciPlus3_filling_mean=pd.DataFrame(pMediciPlus3_filling_mean).transpose()
pMediciPlus2_filling_mean=pd.DataFrame(pMediciPlus2_filling_mean).transpose()
pMediciPlus1_filling_mean=pd.DataFrame(pMediciPlus1_filling_mean).transpose()

pMediciPlus8_filling_mean.index=[8]
pMediciPlus7_filling_mean.index=[7]
pMediciPlus6_filling_mean.index=[6]
pMediciPlus5_filling_mean.index=[5]
pMediciPlus4_filling_mean.index=[4]
pMediciPlus3_filling_mean.index=[3]
pMediciPlus2_filling_mean.index=[2]
pMediciPlus1_filling_mean.index=[1]

mergedMeans=pd.concat([pMediciPlus1_filling_mean, pMediciPlus2_filling_mean, pMediciPlus3_filling_mean, pMediciPlus4_filling_mean, pMediciPlus5_filling_mean, pMediciPlus6_filling_mean, pMediciPlus7_filling_mean, pMediciPlus8_filling_mean])


# dimensione media dei vecchi casi di test utilizzati da pMEDICI+
ACTSFromScratch_Size["Boeingv1"]
oldTsName = ["AmbientAssistedLivingv1", "AutomotiveMultimediav1", "AutomotiveMultimediav2","Boeingv1","Boeingv2","CarBodyv1","CarBodyv2","CarBodyv3","LinuxKernelv1","LinuxKernelv2","ParkingAssistantv1","ParkingAssistantv2","ParkingAssistantv3", "ParkingAssistantv4", "PPUv1","PPUv2","PPUv3","PPUv4","PPUv5","PPUv6","PPUv7","PPUv8","SmartHotelv1","Smartwatchv1","WeatherStationv1"]
ACTSKeptInIncremental=ACTSFromScratch_Size[oldTsName]
ACTSKeptInIncremental.values

# Models with old ts min size: ['Boeingv2', 'Boeingv3', 'ParkingAssistantv2']

# Boeingv2 -> size ts Boeingv1=1
print("size Boeingv1 oldTs: ", ACTSKeptInIncremental["Boeingv1"][0])
oldTsSize1=ACTSFromScratch_Size["Boeingv1"]
ax = mergedMeans["Boeingv2"].plot.bar(figsize=(10,6), color=colors2)
ax.set_xlabel("Number of Threads")
ax.set_ylabel("Time (ms)")

# printing the values on the top of the bars
for bars in ax.containers:
    ax.bar_label(bars)
    
for bars in ay.containers:
    ay.bar_label(bars)

mylabels=["Boeingv2"]
ax.legend(labels=mylabels)
ax.annotate("Old TS size: 1", xy=(-0.4,1.25))
# ax.figure.savefig(r'..\ThreadFillingTime_min_Boeingv2.png', format='png', dpi=400,  bbox_inches='tight')

#%%

# Boeingv3 -> size ts Boeingv2=2
print("size Boeingv2 oldTs: ", ACTSKeptInIncremental["Boeingv2"][0])
ax = mergedMeans["Boeingv3"].plot.bar(figsize=(10,6), color=colors2)
ax.set_xlabel("Number of Threads")
ax.set_ylabel("Time (ms)")

# printing the values on the top of the bars
for bars in ax.containers:
    ax.bar_label(bars)
    
for bars in ay.containers:
    ay.bar_label(bars)

mylabels=["Boeingv3"]
ax.legend(labels=mylabels)
ax.annotate("Old TS size: 2", xy=(-0.4,1.28))
# ax.figure.savefig(r'..\ThreadFillingTime_min_Boeingv3.png', format='png', dpi=400,  bbox_inches='tight')

#%%
# ParkingAssistantv2 -> old size ts (ParkingAssistantv1)=1
print("size ParkingAssistantv1 oldTs: ", ACTSKeptInIncremental["ParkingAssistantv1"][0])
ax = mergedMeans["ParkingAssistantv2"].plot.bar(figsize=(10,6), color=colors2)
ax.set_xlabel("Number of Threads")
ax.set_ylabel("Time (ms)")

# printing the values on the top of the bars
for bars in ax.containers:
    ax.bar_label(bars)
    
for bars in ay.containers:
    ay.bar_label(bars)

mylabels=["ParkingAssistantv2"]
ax.legend(labels=mylabels)
ax.annotate("Old TS size: 1", xy=(-0.4,1.22))
# ax.figure.savefig(r'..\ThreadFillingTime_min_ParkingAssistantv2.png', format='png', dpi=400,  bbox_inches='tight')

#%%
# ParkingAssistantv3 -> size ts ParkingAssistantv2=2
print("size ParkingAssistantv2 oldTs: ", ACTSKeptInIncremental["ParkingAssistantv2"][0])
ax = mergedMeans["ParkingAssistantv3"].plot.bar(figsize=(10,6), color=colors2)
ax.set_xlabel("Number of Threads")
ax.set_ylabel("Time (ms)")

# printing the values on the top of the bars
for bars in ax.containers:
    ax.bar_label(bars)
    
for bars in ay.containers:
    ay.bar_label(bars)

mylabels=["ParkingAssistantv3"]
ax.legend(labels=mylabels)
ax.annotate("Old TS size: 2", xy=(-0.4,1.1))
# ax.figure.savefig(r'..\ThreadFillingTime_min_ParkingAssistantv3.png', format='png', dpi=400,  bbox_inches='tight')

#%%
# Models with max size: ["WeatherStationv2"]
# WeatherStationv2 -> size ts WeatherStationv1=11
print("size WeatherStationv1 oldTs: ", ACTSKeptInIncremental["WeatherStationv1"][0])
ax = mergedMeans["WeatherStationv2"].plot.bar(figsize=(10,6), color=colors2)
ax.set_xlabel("Number of Threads")
ax.set_ylabel("Time (ms)")

# printing the values on the top of the bars
for bars in ax.containers:
    ax.bar_label(bars)
    
for bars in ay.containers:
    ay.bar_label(bars)

mylabels=["WeatherStationv2"]
ax.legend(labels=mylabels)
ax.annotate("Old TS size: 11", xy=(-0.4,1.375))
# ax.figure.savefig(r'..\ThreadFillingTime_max_WeatherStationv2.png', format='png', dpi=400,  bbox_inches='tight')

#%%
# Models with max size: ["AmbientAssistedLivingv2"]
# AmbientAssistedLivingv2 -> size ts AmbientAssistedLivingv1=11
print("size AmbientAssistedLivingv1 oldTs: ", ACTSKeptInIncremental["AmbientAssistedLivingv1"][0])
ax = mergedMeans["AmbientAssistedLivingv2"].plot.bar(figsize=(10,6), color=colors2)
ax.set_xlabel("Number of Threads")
ax.set_ylabel("Time (ms)")

# printing the values on the top of the bars
for bars in ax.containers:
    ax.bar_label(bars)
    
for bars in ay.containers:
    ay.bar_label(bars)

mylabels=["AmbientAssistedLivingv2"]
ax.legend(labels=mylabels)
ax.annotate("Old TS size: 11", xy=(-0.4,1.23))
# ax.figure.savefig(r'..\ThreadFillingTime_max_AmbientAssistedLivingv2.png', format='png', dpi=400,  bbox_inches='tight')

#%%
# Models with max size: ["AmbientAssistedLivingv2"]
# AmbientAssistedLivingv2 -> size ts AmbientAssistedLivingv1=11
print("size AmbientAssistedLivingv1 oldTs: ", ACTSKeptInIncremental["AmbientAssistedLivingv1"][0])
ax = mergedMeans["AmbientAssistedLivingv2"].plot.bar(figsize=(10,6), color=colors2)
ax.set_xlabel("Number of Threads")
ax.set_ylabel("Time (ms)")

# printing the values on the top of the bars
for bars in ax.containers:
    ax.bar_label(bars)
    
for bars in ay.containers:
    ay.bar_label(bars)

mylabels=["AmbientAssistedLivingv2"]
ax.legend(labels=mylabels)
ax.annotate("Old TS size: 11", xy=(-0.4,1.23))
# ax.figure.savefig(r'..\ThreadFillingTime_max_AmbientAssistedLivingv2.png', format='png', dpi=400,  bbox_inches='tight')

#%%
# Models with max size: ["PPUv6"]
# PPUv5 -> size ts PPUv4=10
print("size PPUv5 oldTs: ", ACTSKeptInIncremental["PPUv5"][0])
ax = mergedMeans["PPUv6"].plot.bar(figsize=(10,6), color=colors2)
ax.set_xlabel("Number of Threads")
ax.set_ylabel("Time (ms)")

# printing the values on the top of the bars
for bars in ax.containers:
    ax.bar_label(bars)
    
for bars in ay.containers:
    ay.bar_label(bars)

mylabels=["PPUv6"]
ax.legend(labels=mylabels)
ax.annotate("Old TS size: 10", xy=(-0.4,1.1))
# ax.figure.savefig(r'..\ThreadFillingTime_max_PPUv6.png', format='png', dpi=400,  bbox_inches='tight')

#%%
# Models with max size: ["PPUv7"]
# PPUv6 -> size ts PPUv5=10
print("size PPUv5 oldTs: ", ACTSKeptInIncremental["PPUv6"][0])
ax = mergedMeans["PPUv7"].plot.bar(figsize=(10,6), color=colors2)
ax.set_xlabel("Number of Threads")
ax.set_ylabel("Time (ms)")

# printing the values on the top of the bars
for bars in ax.containers:
    ax.bar_label(bars)
    
for bars in ay.containers:
    ay.bar_label(bars)

mylabels=["PPUv7"]
ax.legend(labels=mylabels)
ax.annotate("Old TS size: 10", xy=(-0.4,1.15))
# ax.figure.savefig(r'..\ThreadFillingTime_max_PPUv7.png', format='png', dpi=400,  bbox_inches='tight')

#%% 2.1b) Unione di più grafici insieme (max e min test suite)
## setting subplot info

# media dei tempi di test early filling per numero di thread PER OGNI MODELLO
pMediciPlus8_filling_mean = pMediciPlus8_filling.mean()
pMediciPlus7_filling_mean = pMediciPlus7_filling.mean()
pMediciPlus6_filling_mean = pMediciPlus6_filling.mean()
pMediciPlus5_filling_mean = pMediciPlus5_filling.mean()
pMediciPlus4_filling_mean = pMediciPlus4_filling.mean()
pMediciPlus3_filling_mean = pMediciPlus3_filling.mean()
pMediciPlus2_filling_mean = pMediciPlus2_filling.mean()
pMediciPlus1_filling_mean = pMediciPlus1_filling.mean()

pMediciPlus8_filling_mean=pd.DataFrame(pMediciPlus8_filling_mean).transpose()
pMediciPlus7_filling_mean=pd.DataFrame(pMediciPlus7_filling_mean).transpose()
pMediciPlus6_filling_mean=pd.DataFrame(pMediciPlus6_filling_mean).transpose()
pMediciPlus5_filling_mean=pd.DataFrame(pMediciPlus5_filling_mean).transpose()
pMediciPlus4_filling_mean=pd.DataFrame(pMediciPlus4_filling_mean).transpose()
pMediciPlus3_filling_mean=pd.DataFrame(pMediciPlus3_filling_mean).transpose()
pMediciPlus2_filling_mean=pd.DataFrame(pMediciPlus2_filling_mean).transpose()
pMediciPlus1_filling_mean=pd.DataFrame(pMediciPlus1_filling_mean).transpose()

pMediciPlus8_filling_mean.index=[8]
pMediciPlus7_filling_mean.index=[7]
pMediciPlus6_filling_mean.index=[6]
pMediciPlus5_filling_mean.index=[5]
pMediciPlus4_filling_mean.index=[4]
pMediciPlus3_filling_mean.index=[3]
pMediciPlus2_filling_mean.index=[2]
pMediciPlus1_filling_mean.index=[1]

mergedMeans=pd.concat([pMediciPlus1_filling_mean, pMediciPlus2_filling_mean, pMediciPlus3_filling_mean, pMediciPlus4_filling_mean, pMediciPlus5_filling_mean, pMediciPlus6_filling_mean, pMediciPlus7_filling_mean, pMediciPlus8_filling_mean])

colors1 = ["#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4"]
colors2 = ["#000000","#000000","#000000","#000000","#000000","#000000","#000000","#000000"]



fig = plt.figure()

ax1 = fig.add_subplot(221)
ax2 = fig.add_subplot(223)
ax3 = fig.add_subplot(222)
ax4 = fig.add_subplot(224)

plt.subplots_adjust(left=0.125, bottom=0.9, right=1.2 , top=1.9 , wspace= 0.15, hspace= 0.20)

# Boeingv2 -> old ts size (Boeingv1)=1
mergedMeans["Boeingv2"].plot.bar(figsize=(10,6), color=colors2, ax=ax1)
ax1.set_xlabel("Number of Threads")
ax1.set_ylabel("Time (ms)")
ax1.set_ylim(0, 1.65)


# printing the values on the top of the bars
for bars in ax1.containers:
    ax1.bar_label(bars)

mylabels=["Boeingv2"]
ax1.legend(labels=mylabels, bbox_to_anchor=(0.27, 1.0))
ax1.annotate("Old TS size: 1", xy=(-0.25,1.3))
ax1.title.set_text('Smallest old Test Suites')

# ParkingAssistantv2 -> old size ts (ParkingAssistantv1)=1
mergedMeans["ParkingAssistantv2"].plot.bar(figsize=(10,6), color=colors2, ax=ax2)
ax2.set_xlabel("Number of Threads")
ax2.set_ylabel("Time (ms)")
ax2.set_ylim(0, 1.65)

# printing the values on the top of the bars
for bars in ax2.containers:
    ax2.bar_label(bars)
    
mylabels=["ParkingAssistantv2"]
ax2.legend(labels=mylabels, bbox_to_anchor=(0.405, 1.0))
ax2.annotate("Old TS size: 1", xy=(-0.25,1.3))

# WeatherStationv2 -> size old ts (WeatherStationv1)=11
mergedMeans["WeatherStationv2"].plot.bar(figsize=(10,6), color=colors2, ax=ax3)
ax3.set_xlabel("Number of Threads")
ax3.set_ylabel("Time (ms)")
ax3.set_ylim(0, 1.65)

# printing the values on the top of the bars
for bars in ax3.containers:
    ax3.bar_label(bars)

mylabels=["WeatherStationv2"]
ax3.legend(labels=mylabels, bbox_to_anchor=(0.39, 1.0))
ax3.annotate("Old TS size: 11", xy=(-0.25,1.3))
ax3.title.set_text('Largest old Test Suites')
# ax.figure.savefig(r'..\ThreadFillingTime_max_WeatherStationv2.png', format='png', dpi=400,  bbox_inches='tight')

# AmbientAssistedLivingv2 -> old size ts (AmbientAssistedLivingv2)=11
mergedMeans["AmbientAssistedLivingv2"].plot.bar(figsize=(10,6), color=colors2, ax=ax4)
ax4.set_xlabel("Number of Threads")
ax4.set_ylabel("Time (ms)")
ax4.set_ylim(0, 1.65)

# printing the values on the top of the bars
for bars in ax4.containers:
    ax4.bar_label(bars)

mylabels=["AmbientAssistedLivingv2"]
ax4.legend(labels=mylabels, bbox_to_anchor=(0.49, 1.0))
ax4.annotate("Old TS size: 11",  xy=(-0.25,1.3))

plt.savefig(r'..\2.1b_TimeThreadExtremeCases.png', format='png', dpi=400,  bbox_inches='tight')



#%%
# 2.1c) Impatto del numero di TestEarlyFiller threads sulla dimensione finale delle test suite

# Calcolo media TOTALE della size della TS per ogni tipologia di esecuzione (numero di thread)
numOfValues = pMediciPlus8_reducedSize.shape[0]* pMediciPlus8_reducedSize.shape[1]

allMean8_reducedSize= (pMediciPlus8_reducedSize.to_numpy().sum()) / numOfValues
allMean7_reducedSize= (pMediciPlus7_reducedSize.to_numpy().sum()) / numOfValues
allMean6_reducedSize= (pMediciPlus6_reducedSize.to_numpy().sum()) / numOfValues
allMean5_reducedSize= (pMediciPlus5_reducedSize.to_numpy().sum()) / numOfValues
allMean4_reducedSize= (pMediciPlus4_reducedSize.to_numpy().sum()) / numOfValues
allMean3_reducedSize= (pMediciPlus3_reducedSize.to_numpy().sum()) / numOfValues
allMean2_reducedSize= (pMediciPlus2_reducedSize.to_numpy().sum()) / numOfValues
allMean1_reducedSize= (pMediciPlus1_reducedSize.to_numpy().sum()) / numOfValues


allMean8_sizeNotReduced = (pMediciPlus8_sizeNotReduced.to_numpy().sum()) / numOfValues
allMean7_sizeNotReduced = (pMediciPlus7_sizeNotReduced.to_numpy().sum()) / numOfValues
allMean6_sizeNotReduced = (pMediciPlus6_sizeNotReduced.to_numpy().sum()) / numOfValues
allMean5_sizeNotReduced = (pMediciPlus5_sizeNotReduced.to_numpy().sum()) / numOfValues
allMean4_sizeNotReduced = (pMediciPlus4_sizeNotReduced.to_numpy().sum()) / numOfValues
allMean3_sizeNotReduced = (pMediciPlus3_sizeNotReduced.to_numpy().sum()) / numOfValues
allMean2_sizeNotReduced = (pMediciPlus2_sizeNotReduced.to_numpy().sum()) / numOfValues
allMean1_sizeNotReduced = (pMediciPlus1_sizeNotReduced.to_numpy().sum()) / numOfValues

threadNum = [1,2,3,4,5,6,7,8]

MeanReducedSize=[allMean1_reducedSize, allMean2_reducedSize, allMean3_reducedSize, allMean4_reducedSize,allMean5_reducedSize,allMean6_reducedSize,allMean7_reducedSize,allMean8_reducedSize]
threadsMeanReducedSize_dict = {'Number of threads': threadNum, 'MeanReducedSize': MeanReducedSize}

MeanSizeNotReduced=[allMean1_sizeNotReduced, allMean2_sizeNotReduced , allMean3_sizeNotReduced , allMean4_sizeNotReduced ,allMean5_sizeNotReduced ,allMean6_sizeNotReduced ,allMean7_sizeNotReduced ,allMean8_sizeNotReduced ]
threadsMeanSizeNotReduced_dict = {'Number of threads': threadNum, 'MeanSizeNotReduced': MeanSizeNotReduced}


# create DataFrame from dict
threadsMeanReducedSize = pd.DataFrame(threadsMeanReducedSize_dict)
threadsMeanSizeNotReduced = pd.DataFrame(threadsMeanSizeNotReduced_dict)

# set index using column
threadsMeanReducedSize = threadsMeanReducedSize.set_index('Number of threads')
threadsMeanReducedSize=threadsMeanReducedSize.round(2)

threadsMeanSizeNotReduced = threadsMeanSizeNotReduced.set_index('Number of threads')
threadsMeanSizeNotReduced =threadsMeanSizeNotReduced.round(2)

colors1 = ["#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4","#b4bbc4"]
colors2 = ["#000000","#000000","#000000","#000000","#000000","#000000","#000000","#000000"]


#ay = threadsMeanSizeNotReduced["MeanSizeNotReduced"].plot.bar(figsize=(10,6), color=colors1)
ax = threadsMeanReducedSize["MeanReducedSize"].plot.bar(figsize=(10,6), color=colors2)

#trial=pd.concat([threadsMeanReducedSize, threadsMeanSizeNotReduced], axis=1)
#atrial = trial.plot.bar(figsize=(10,6), stacked=True)


# label changing on both x and y axes
ax.set_xlabel("Number of Threads")
ax.set_ylabel("Size (number of Test Cases)")
ax.set_ylim(0, 13)

#ay.set_xlabel("Number of Threads")
#ay.set_ylabel("Size (number of Test Cases)")

# printing the values on the top of the bars
for bars in ax.containers:
    ax.bar_label(bars)
    
#for bars in ay.containers:
#   ay.bar_label(bars)

#mylabels=["MeanTotalSize", "MeanReducedSize"]
#ax.legend(labels=mylabels, bbox_to_anchor=(1.0, 1.0))

ax.figure.savefig(r'..\2.1c_pMEDICIPlus_ThreadsImpactOnSizes.png', format='png', dpi=400,  bbox_inches='tight')

#%%
# 2.2a) Relazione distanza tra vecchie TS generate con ACTS e numero di test mantenuti con la tecnica 2

grey='#b4bbc4'
black='000000'

pMediciPlus_totalTime = pMediciPlus2
pMediciPlus_fillingTime = pMediciPlus2_filling
pMediciPlus_size = pd.DataFrame(pMediciPlus2_reducedSize.mean()).transpose()
numOfKeepedTestCases = pd.read_excel(r"..\pMEDICIMT_Incremental_Generation_NumOfTestsKeptWithinTheEvolution.xlsx", sheet_name="Num of test cases kept", header=1,  usecols=cols )
evolutionDistanceTS = pd.read_excel(r"..\EvolutionModels_Distances.xlsx", sheet_name="EvolutionDistanceACTS", header=1,  usecols=cols )

percKeepTestCases = numOfKeepedTestCases.divide(pMediciPlus_size).mul(100)

for col in evolutionDistanceTS.columns:
    plt.scatter(evolutionDistanceTS[col], percKeepTestCases[col], label=col, color=black, s=5)
plt.xlabel('Distance (%) between Evolving TSs generated with Technique 1')
plt.ylabel('Kept Test Cases (%) with Technique 2')
plt.title('pMEDICI+')
plt.savefig(r'..\2.2a_KeptDistanceRelationship.png', format='png', dpi=400,  bbox_inches='tight')

