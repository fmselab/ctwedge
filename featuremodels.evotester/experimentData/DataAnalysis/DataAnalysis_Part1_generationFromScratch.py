# -*- coding: utf-8 -*-
"""
Created on Wed Jun 29 15:06:50 2022

@author: Luca Parimbelli
"""

""

import pandas as pd
import matplotlib.pyplot as plt

cols2skip = [0]  
cols = [i for i in range(36) if i not in cols2skip]

ACTSFromScratch = pd.read_excel(r"..\ACTS_Generation_from_scratch.xlsx", sheet_name="TIME (ms) for generating TS", header=1,  usecols=cols )
ACTSFromScratch_Size = pd.read_excel(r"..\\ACTS_Generation_from_scratch.xlsx", sheet_name="SIZE of the generated TS", header=1,  usecols=cols )

pMediciNormal_reducedSize= pd.read_excel(r"..\\pMEDICI_Generation_from_scratch.xlsx", sheet_name="TIME (ms) for generating TS", header=1,  usecols=cols )
pMediciNormal_sizeNotReduced= pd.read_excel(r"..\\pMEDICI_Generation_from_scratch_noMergeDuplicatedTests.xlsx", sheet_name="TIME (ms) for generating TS", header=1,  usecols=cols )
pMediciNormal_reducedSize_size = pd.read_excel(r"C..\\pMEDICI_Generation_from_scratch.xlsx", sheet_name="REDUCED SIZE of the TS", header=1,  usecols=cols )
pMediciNormal_sizeNotReduced_size = pd.read_excel(r"..\\pMEDICI_Generation_from_scratch.xlsx", sheet_name="SIZE of the generated TS", header=1,  usecols=cols )

modelNumOfFeatures = pd.read_excel(r"..\EvolutionModels_Features.xlsx", sheet_name="NumOfFeaures", header=1,  usecols=cols )
modelNumOfConstraints = pd.read_excel(r"..\EvolutionModels_Features.xlsx", sheet_name="NumOfConstraints", header=1,  usecols=cols )
modelNumOfTotalConfig = pd.read_excel(r"..\EvolutionModels_Features.xlsx", sheet_name="NumOfTotalConfig", header=1,  usecols=cols )
modelNumOfValidConfig =pd.read_excel(r"..\EvolutionModels_Features.xlsx", sheet_name="NumOfValidConfig", header=1,  usecols=cols )

#%% 1.1) Impact of the test suite REDUCTION TECHNIQUE on pMEDICI

#1.1a: grafico a barre

# Impact on the size
pMediciNormal_reducedSize_mean = pMediciNormal_reducedSize_size.mean()
pMediciNormal_sizeNotreduced_mean= pMediciNormal_sizeNotReduced_size.mean()

colors1=["#b4bbc4"]*25
colors2=["#000000"]*25

colors=["#000000","#b4bbc4"]*25

# grafici su barre diverse
oldTsName = ["AmbientAssistedLivingv1", "AmbientAssistedLivingv2", "AutomotiveMultimediav1", "AutomotiveMultimediav2", "AutomotiveMultimediav3","Boeingv1","Boeingv2","Boeingv3","CarBodyv1","CarBodyv2","CarBodyv3","CarBodyv4","LinuxKernelv1","LinuxKernelv2","LinuxKernelv3","ParkingAssistantv1","ParkingAssistantv2","ParkingAssistantv3","ParkingAssistantv4", "ParkingAssistantv5", "PPUv1","PPUv2","PPUv3","PPUv4","PPUv5","PPUv6","PPUv7","PPUv8","PPUv9","SmartHotelv1","SmartHotelv2","Smartwatchv2","WeatherStationv1","WeatherStationv2"]
index = oldTsName

df = pd.DataFrame({'ReductionTechnique': pMediciNormal_reducedSize_mean,
                   'NoReductionTechnique': pMediciNormal_sizeNotreduced_mean}, index=index)
ax = df.plot.bar(color=colors, figsize=(12,6))

ax.set_ylabel("Size (number of Test Cases)")

#ax.figure.savefig(r'..\1.1a_pMEDICINormal_reductionTechniqueSIZE.png', format='png', dpi=400,  bbox_inches='tight')

#%%
# 1.1b: grafico percentuale
valore2=pMediciNormal_reducedSize_mean
valore1=pMediciNormal_sizeNotreduced_mean

x = (100*valore1 - valore2)/valore1

y=valore2.multiply(100).divide(valore1)
reduction=(100-y).round(1)

ax=reduction.plot(kind="bar", figsize=(16,6), color=colors2)

ax.set_ylabel("Decrease in size [%]")

# printing the values on the top of the bars
for bars in ax.containers:
    ax.bar_label(bars)
        
#ax.figure.savefig(r'..\1.1b_pMEDICINormal_reductionTechniqueSIZEDecreasePercentage.png', format='png', dpi=400,  bbox_inches='tight')

#%%
# 1.1c: box plot decrease size percentuale
pMediciNormal_reducedSize_mean = pMediciNormal_reducedSize_size.mean()
pMediciNormal_sizeNotreduced_mean= pMediciNormal_sizeNotReduced_size.mean()

# riduzione percentuale : x= (grande-piccolo)/grande
# poi moltiplico x*100 per averla in percentuale
piccolo=pMediciNormal_reducedSize_mean
grande=pMediciNormal_sizeNotreduced_mean
x = (grande-piccolo)/grande
x=x*100

reduction = pd.DataFrame(x)

ax=reduction.boxplot(vert=False, grid=False, color=dict(boxes='#000000', whiskers='#000000', medians='#000000', caps='#000000'),  whiskerprops=dict(linestyle='--', linewidth=1), figsize=(6,3),widths=0.3)

ax.set_xlabel("Decrease in size [%]")

ax.set(yticklabels=[])  # remove the tick labels
ax.set(ylabel=None)  # remove the axis label

#ax.figure.savefig(r'..\1.1c_boxplot_sizeReductionPercent.png', format='png', dpi=400,  bbox_inches='tight')

#%%
# 1.1d: box plot confronto tecniche
pMediciNormal_reducedSize_mean = pMediciNormal_reducedSize_size.mean()
pMediciNormal_sizeNotreduced_mean= pMediciNormal_sizeNotReduced_size.mean()

oldTsName = ["AmbientAssistedLivingv1", "AmbientAssistedLivingv2", "AutomotiveMultimediav1", "AutomotiveMultimediav2", "AutomotiveMultimediav3","Boeingv1","Boeingv2","Boeingv3","CarBodyv1","CarBodyv2","CarBodyv3","CarBodyv4","LinuxKernelv1","LinuxKernelv2","LinuxKernelv3","ParkingAssistantv1","ParkingAssistantv2","ParkingAssistantv3","ParkingAssistantv4", "ParkingAssistantv5", "PPUv1","PPUv2","PPUv3","PPUv4","PPUv5","PPUv6","PPUv7","PPUv8","PPUv9","SmartHotelv1","SmartHotelv2","Smartwatchv2","WeatherStationv1","WeatherStationv2"]
index = oldTsName

df = pd.DataFrame({'ReductionTechnique': pMediciNormal_reducedSize_mean,
                   'NoReductionTechnique': pMediciNormal_sizeNotreduced_mean}, index=index)

ax = df[['ReductionTechnique', 'NoReductionTechnique']].plot(kind='box', vert=False, grid=False, color=dict(boxes='#000000', whiskers='#000000', medians='#000000', caps='#000000'), whiskerprops=dict(linestyle='--', linewidth=1),widths=0.4)

ax.set_xlabel("Size (number of Test Cases)")
#ax.figure.savefig(r'C..\1.1d_boxplot_sizeReductionCompare.png', format='png', dpi=400,  bbox_inches='tight')

#%%  1.1c + 1.1d insieme

## setting subplot info
fig = plt.figure()

# Divide the figure into a 1x2 grid, and give me the first section
ax1 = fig.add_subplot(121)

# Divide the figure into a 1x2 grid, and give me the second section
ax2 = fig.add_subplot(122)

plt.subplots_adjust(left=0.125, bottom=0.9, right=1.8 , top=1.4, wspace= 0.5, hspace= 0.35)

# 1.1c: box plot decrease size percentuale
pMediciNormal_reducedSize_mean = pMediciNormal_reducedSize_size.mean()
pMediciNormal_sizeNotreduced_mean= pMediciNormal_sizeNotReduced_size.mean()

# riduzione percentuale : x= (grande-piccolo)/grande
# poi moltiplico x*100 per averla in percentuale
piccolo=pMediciNormal_reducedSize_mean
grande=pMediciNormal_sizeNotreduced_mean
x = (grande-piccolo)/grande
x=x*100
reduction = pd.DataFrame(x)

reduction.boxplot(vert=False, grid=False, color=dict(boxes='#000000', whiskers='#000000', medians='#000000', caps='#000000'),  whiskerprops=dict(linestyle='--', linewidth=1), figsize=(6,3),widths=0.3, ax=ax1)

ax1.set_xlabel("Decrease in size [%]")

ax1.set(yticklabels=['ReductionTechnique'])  # remove the tick labels

# 1.1d: box plot confronto tecniche
oldTsName = ["AmbientAssistedLivingv1", "AmbientAssistedLivingv2", "AutomotiveMultimediav1", "AutomotiveMultimediav2", "AutomotiveMultimediav3","Boeingv1","Boeingv2","Boeingv3","CarBodyv1","CarBodyv2","CarBodyv3","CarBodyv4","LinuxKernelv1","LinuxKernelv2","LinuxKernelv3","ParkingAssistantv1","ParkingAssistantv2","ParkingAssistantv3","ParkingAssistantv4", "ParkingAssistantv5", "PPUv1","PPUv2","PPUv3","PPUv4","PPUv5","PPUv6","PPUv7","PPUv8","PPUv9","SmartHotelv1","SmartHotelv2","Smartwatchv2","WeatherStationv1","WeatherStationv2"]
index = oldTsName
df = pd.DataFrame({'ReductionTechnique': pMediciNormal_reducedSize_mean,
                   'NoReductionTechnique': pMediciNormal_sizeNotreduced_mean}, index=index)

df[['ReductionTechnique', 'NoReductionTechnique']].plot(kind='box', vert=False, grid=False, color=dict(boxes='#000000', whiskers='#000000', medians='#000000', caps='#000000'), whiskerprops=dict(linestyle='--', linewidth=1),widths=0.4, ax=ax2)

ax2.set_xlabel("Size (number of Test Cases)")

plt.savefig(r'..\1.1cd_boxplot_sizeReductionTogether.png', format='png', dpi=400,  bbox_inches='tight')


#%%
# 1.1e) Impact on the time
pMediciNormal_reducedSize_mean = pMediciNormal_reducedSize.mean()
pMediciNormal_sizeNotreduced_mean= pMediciNormal_sizeNotReduced.mean()

colors1=["#b4bbc4"]*25
colors2=["#000000"]*25

colors=["#000000","#b4bbc4"]*25

# grafici su barre diverse
oldTsName = ["AmbientAssistedLivingv1", "AmbientAssistedLivingv2", "AutomotiveMultimediav1", "AutomotiveMultimediav2", "AutomotiveMultimediav3","Boeingv1","Boeingv2","Boeingv3","CarBodyv1","CarBodyv2","CarBodyv3","CarBodyv4","LinuxKernelv1","LinuxKernelv2","LinuxKernelv3","ParkingAssistantv1","ParkingAssistantv2","ParkingAssistantv3","ParkingAssistantv4", "ParkingAssistantv5", "PPUv1","PPUv2","PPUv3","PPUv4","PPUv5","PPUv6","PPUv7","PPUv8","PPUv9","SmartHotelv1","SmartHotelv2","Smartwatchv2","WeatherStationv1","WeatherStationv2"]
index = oldTsName

df = pd.DataFrame({'ReductionTechnique': pMediciNormal_reducedSize_mean,
                   'NoReductionTechnique': pMediciNormal_sizeNotreduced_mean}, index=index)
ax = df.plot.bar(color=colors, figsize=(12,6))

ax.set_ylabel("Size (number of Test Cases)")

ax.legend(bbox_to_anchor=(0.97, 1.0))

ax.figure.savefig(r'..\1.1e_pMEDICINormal_reductionTechniqueTIME.png', format='png', dpi=400,  bbox_inches='tight')

#%% 1.2) ACTS vs pMEDICI
grey='#b4bbc4'
black='000000'

ACTSFromScratch_time = ACTSFromScratch
ACTSFromScratch_size = ACTSFromScratch_Size
pMedici_time = pMediciNormal_reducedSize
pMedici_size=pMediciNormal_reducedSize_size

# calcolo tempo medio per modello e totale
dataSize_pMedici = pMedici_time.shape[0]* pMedici_time.shape[1]
pMedici_time_mean_perModel = pMedici_time.mean().round(2)
pMedici_time_totalMean= ( (pMedici_time.to_numpy().sum()) / dataSize_pMedici ).round(2)

dataSize_ACTS = ACTSFromScratch_time.shape[0]*ACTSFromScratch_time.shape[1]
ACTSFromScratch_time_mean_perModel = ACTSFromScratch_time.mean().round(2)
ACTSFromScratch_time_totalMean = ( (ACTSFromScratch_time.to_numpy().sum()) / dataSize_ACTS ).round(2)

# calcolo dimensione media test suite per modello e totale
dataSize_pMedici = pMedici_time.shape[0]* pMedici_time.shape[1]
pMedici_size_mean_perModel = pMedici_size.mean().round(2)
pMedici_size_totalMean= ( (pMedici_size.to_numpy().sum()) / dataSize_pMedici ).round(2)

dataSize_ACTS = ACTSFromScratch_size.shape[0]*ACTSFromScratch_size.shape[1]
ACTSFromScratch_size_mean_perModel = ACTSFromScratch_size.mean()
ACTSFromScratch_size_totalMean = ( (ACTSFromScratch_size.to_numpy().sum()) / dataSize_ACTS ).round(2)

# 1.2a) Tempo medio di tutte le generazioni

indexes = ["ACTS","pMEDICI"]
time_totalMean = pd.DataFrame([ACTSFromScratch_time_totalMean,pMedici_time_totalMean], index=indexes).transpose()
colors={"ACTS":black, "pMEDICI":grey}
ax=time_totalMean[['ACTS', 'pMEDICI']].plot(kind="barh",color=colors, figsize=(8,4), width=0.2)

# rimuovere scritte asse y
ax.axes.get_yaxis().set_ticks([])

# per settare limiti asse x
ax.set_xlim(0, 125)

# label changing on both x and y axes
ax.set_ylabel("Generation Tool")
ax.set_xlabel("Time (ms)")

for bars in ax.containers:
    ax.bar_label(bars)
    
#ax.figure.savefig(r'..\1.2a_pMEDICIvsACTS_totalTime.png', format='png', dpi=400,  bbox_inches='tight')

# 1.2b) Dimensione media di tutte le generazioni

indexes = ["ACTS","pMEDICI"]
size_totalMean = pd.DataFrame([ACTSFromScratch_size_totalMean,pMedici_size_totalMean], index=indexes).transpose()
colors={"ACTS":black, "pMEDICI":grey}
ax=size_totalMean[['ACTS', 'pMEDICI']].plot(kind="barh",color=colors, figsize=(8,4), width=0.2)

# rimuovere scritte asse y
ax.axes.get_yaxis().set_ticks([])

# per settare limiti asse x
ax.set_xlim(0, 13)

# label changing on both x and y axes
ax.set_ylabel("Generation Tool")
ax.set_xlabel("Size (number of Test Cases)")

for bars in ax.containers:
    ax.bar_label(bars)
    
#ax.figure.savefig(r'..\1.2b_pMEDICIvsACTS_totalSize.png', format='png', dpi=400,  bbox_inches='tight')

#%% Unisco 1.2a e 1.2b

## setting subplot info
fig = plt.figure()

# Divide the figure into a 1x2 grid, and give me the first section
ax1 = fig.add_subplot(121)

# Divide the figure into a 1x2 grid, and give me the second section
ax2 = fig.add_subplot(122)

plt.subplots_adjust(left=0.125, bottom=0.9, right=1.0 , top=1.4, wspace= 0.2, hspace= 0.35)

indexes = ["ACTS","pMEDICI"]
time_totalMean = pd.DataFrame([ACTSFromScratch_time_totalMean,pMedici_time_totalMean], index=indexes).transpose()
colors={"ACTS":black, "pMEDICI":grey}
time_totalMean[['ACTS', 'pMEDICI']].plot(kind="barh",color=colors, figsize=(8,4), width=0.2, ax=ax1)

# rimuovere scritte asse y
ax1.axes.get_yaxis().set_ticks([])

# per settare limiti asse x
ax1.set_xlim(0, 135)

# label changing on both x and y axes
ax1.set_ylabel("Generation Tool")
ax1.set_xlabel("Time (ms)")

for bars in ax1.containers:
    ax1.bar_label(bars)
    
# 1.2b) Dimensione media di tutte le generazioni

indexes = ["ACTS","pMEDICI"]
size_totalMean = pd.DataFrame([ACTSFromScratch_size_totalMean,pMedici_size_totalMean], index=indexes).transpose()
colors={"ACTS":black, "pMEDICI":grey}
size_totalMean[['ACTS', 'pMEDICI']].plot(kind="barh",color=colors, figsize=(8,4), width=0.2, ax=ax2)

# rimuovere scritte asse y
ax2.axes.get_yaxis().set_ticks([])

# per settare limiti asse x
ax2.set_xlim(0, 13)

# label changing on both x and y axes
ax2.set_ylabel("Generation Tool")
ax2.set_xlabel("Size (number of Test Cases)")

for bars in ax2.containers:
    ax2.bar_label(bars)

plt.savefig(r'..\1.2ab_pMEDICIvsACTS_togetherTimeSize.png', format='png', dpi=400,  bbox_inches='tight')


#%% 1.2c) Validity ratio ACTS/pMEDICI - time/size relationship
ACTSFromScratch_time = ACTSFromScratch
ACTSFromScratch_size = ACTSFromScratch_Size
pMedici_time = pMediciNormal_reducedSize
pMedici_size=pMediciNormal_reducedSize_size

grey = "#b4bbc4"
black = "#000000"

ACTSFromScratch_time_mean_perModel = pd.DataFrame(ACTSFromScratch_time.mean()).transpose()
ACTSFromScratch_size_mean_perModel = pd.DataFrame(ACTSFromScratch_size.mean()).transpose()
pMedici_time_mean_perModel = pd.DataFrame(pMedici_time.mean()).transpose()
pMedici_size_mean_perModel = pd.DataFrame(pMedici_size.mean()).transpose()

validityRatio = modelNumOfValidConfig.divide(modelNumOfTotalConfig).multiply(100)

ax= plt.subplot(2, 2, 1) # row 2, col 2, index 1
for col in validityRatio.columns:
    plt.scatter(validityRatio[col], ACTSFromScratch_time_mean_perModel[col], label=col, color=black, s=4)
plt.xlabel('Validity Ratio (%)')
plt.ylabel('Time (ms)')
plt.title('ACTS')
#plt.legend(loc='best', fontsize=5)

plt.subplot(2, 2, 3) 
for col in validityRatio.columns:
    plt.scatter(validityRatio[col], pMedici_time_mean_perModel[col], label=col, color=black, s=4)
plt.xlabel('Validity Ratio (%)')
plt.ylabel('Time (ms)')
plt.title('pMEDICI')

plt.subplot(2, 2, 2) 
for col in validityRatio.columns:
    plt.scatter(validityRatio[col], ACTSFromScratch_size_mean_perModel[col], label=col, color=black, s=4)
plt.xlabel('Validity Ratio (%)')
plt.ylabel('Size (number of Test Cases)')
plt.title('ACTS')

plt.subplot(2, 2, 4) 
for col in validityRatio.columns:
    plt.scatter(validityRatio[col], pMedici_size_mean_perModel[col], label=col, color=black, s=4)
plt.xlabel('Validity Ratio (%)')
plt.ylabel('Size (number of Test Cases)')
plt.title('pMEDICI')
plt.subplots_adjust(left=0.125, bottom=0.9, right=1.8 , top=2.5 , wspace= 0.2, hspace= 0.3)
plt.show()

ax.figure.savefig(r'..\1.2c_pMEDICIvsACTS_validityRatioTimeSize.png', format='png', dpi=400,  bbox_inches='tight')

#%% 1.2c) Primi due, solo time: Validity ratio ACTS/pMEDICI - time/size relationship
ACTSFromScratch_time = ACTSFromScratch
ACTSFromScratch_size = ACTSFromScratch_Size
pMedici_time = pMediciNormal_reducedSize
pMedici_size=pMediciNormal_reducedSize_size

grey = "#b4bbc4"
black = "#000000"

ACTSFromScratch_time_mean_perModel = pd.DataFrame(ACTSFromScratch_time.mean()).transpose()
ACTSFromScratch_size_mean_perModel = pd.DataFrame(ACTSFromScratch_size.mean()).transpose()
pMedici_time_mean_perModel = pd.DataFrame(pMedici_time.mean()).transpose()
pMedici_size_mean_perModel = pd.DataFrame(pMedici_size.mean()).transpose()

validityRatio = modelNumOfValidConfig.divide(modelNumOfTotalConfig).multiply(100)

ax= plt.subplot(2, 1, 1) # row 2, col 1, index 1
for col in validityRatio.columns:
    plt.scatter(validityRatio[col], ACTSFromScratch_time_mean_perModel[col], label=col, color=black, s=6)
plt.xlabel('Validity Ratio (%)')
plt.ylabel('Time (ms)')
plt.title('ACTS')
#plt.legend(loc='best', fontsize=5)

plt.subplot(2, 1, 2) 
for col in validityRatio.columns:
    plt.scatter(validityRatio[col], pMedici_time_mean_perModel[col], label=col, color=black, s=6)
plt.xlabel('Validity Ratio (%)')
plt.ylabel('Time (ms)')
plt.title('pMEDICI')

plt.subplots_adjust(left=1, bottom=0.9, right=1.8 , top=2.5 , wspace= 0.2, hspace= 0.3)
plt.show()



#%%
# 1.2extra3) Tempo medio di ogni modello in relazione al numero di feature
# asse x = numero di feature, asse y = tempo di generazione
# fare un multiplot (di tipo scatter) sopra ACTS, sotto pMEDICI
modelNumOfFeatures.head()
modelNumOfConstraints.head()
ACTSFromScratch_time_mean_perModel = pd.DataFrame(ACTSFromScratch.mean()).transpose()
ACTSFromScratch_size_mean_perModel = pd.DataFrame(ACTSFromScratch_size_mean_perModel).transpose()
pMedici_time_mean_perModel = pd.DataFrame(pMedici_time_mean_perModel).transpose()
pMedici_size_mean_perModel = pd.DataFrame(pMedici_size_mean_perModel).transpose()

# per tracciare linea di regressione
#trial=modelNumOfFeatures.transpose()
#trialFeatures = trial.rename(columns={0: 'modelNumOfFeatures'})
#trial=ACTSFromScratch_time_mean_perModel.transpose()
#trialTimeACTS = trial.rename(columns={0: 'ACTSTime'})

#trialDF=pd.concat([trialFeatures, trialTimeACTS], axis=1)
#import seaborn as sns
#sns.lmplot(x='modelNumOfFeatures',y='ACTSTime',data=trialDF,fit_reg=True)         
#

# 1.2extra1) Tempo medio + Dimensione media di ogni modello in relazione al Numero di Features
ax= plt.subplot(2, 2, 1) 
for col in modelNumOfFeatures.columns:
    plt.scatter(modelNumOfFeatures[col], ACTSFromScratch_time_mean_perModel[col], label=col, color=black)
plt.xlabel('Number of Features')
plt.ylabel('Time (ms)')
plt.title('ACTS')
#plt.legend(loc='best', fontsize=5)

plt.subplot(2, 2, 3) 
for col in modelNumOfFeatures.columns:
    plt.scatter(modelNumOfFeatures[col], pMedici_time_mean_perModel[col], label=col, color=black)
plt.xlabel('Number of Features')
plt.ylabel('Time (ms)')
plt.title('pMEDICI')

plt.subplot(2, 2, 2) 
for col in modelNumOfFeatures.columns:
    plt.scatter(modelNumOfFeatures[col], ACTSFromScratch_size_mean_perModel[col], label=col, color=black)
plt.xlabel('Number of Features')
plt.ylabel('Size (number of Test Cases)')
plt.title('ACTS')

plt.subplot(2, 2, 4) 
for col in modelNumOfFeatures.columns:
    plt.scatter(modelNumOfFeatures[col], pMedici_size_mean_perModel[col], label=col, color=black)
plt.xlabel('Number of Features')
plt.ylabel('Size (number of Test Cases)')
plt.title('pMEDICI')
plt.subplots_adjust(left=0.125, bottom=0.9, right=1.8 , top=2.5 , wspace= 0.2, hspace= 0.3)
plt.show()

ax.figure.savefig(r'..\1.2extra1_pMEDICIvsACTS_featureTime_relationship.png', format='png', dpi=400,  bbox_inches='tight')


# 1.2extra2) Tempo medio + Dimensione media di ogni modello in relazione al Numero di Constraints
# ax= plt.subplot(2, 1, 1) 
ax= plt.subplot(2, 2, 1) # row 2, col 2 index 1
for col in modelNumOfConstraints.columns:
    plt.scatter(modelNumOfConstraints[col], ACTSFromScratch_time_mean_perModel[col], label=col, color=black)
plt.xlabel('Number of Constraints')
plt.ylabel('Time (ms)')
plt.title('ACTS')
#plt.legend(loc='best', fontsize=5)

# plt.subplot(2, 1, 2) # row 2, col 2 index 2
plt.subplot(2, 2, 3) 
for col in modelNumOfConstraints.columns:
    plt.scatter(modelNumOfConstraints[col], pMedici_time_mean_perModel[col], label=col, color=black)
plt.xlabel('Number of Constraints')
plt.ylabel('Time (ms)')
plt.title('pMEDICI')

plt.subplot(2, 2, 2) 
for col in modelNumOfConstraints.columns:
    plt.scatter(modelNumOfConstraints[col], ACTSFromScratch_size_mean_perModel[col], label=col, color=black)
plt.xlabel('Number of Constraints')
plt.ylabel('Size (number of Test Cases)')
plt.title('ACTS')

plt.subplot(2, 2, 4) 
for col in modelNumOfConstraints.columns:
    plt.scatter(modelNumOfConstraints[col], pMedici_size_mean_perModel[col], label=col, color=black)
plt.xlabel('Number of Constraints')
plt.ylabel('Size (number of Test Cases)')
plt.title('pMEDICI')
plt.subplots_adjust(left=0.125, bottom=0.9, right=1.8 , top=2.5 , wspace= 0.2, hspace= 0.3)
plt.show()

ax.figure.savefig(r'..\1.2extra2_pMEDICIvsACTS_constraintsTime_relationship.png', format='png', dpi=400,  bbox_inches='tight')


# 1.2c) Tempo medio + Dimensione media di ogni modello in relazione alla complessità del modello (Numero di Features + Numero di Constraints)
modelComplexity = pd.concat([modelNumOfFeatures, modelNumOfConstraints])
modelComplexity=modelComplexity.sum()
modelComplexity=pd.DataFrame(modelComplexity).transpose()

ax= plt.subplot(2, 2, 1) # row 2, col 2 index 1
for col in modelComplexity.columns:
    plt.scatter(modelComplexity[col], ACTSFromScratch_time_mean_perModel[col], label=col, color=black)
plt.xlabel('Model Complexity\n(#features+#constraints)')
plt.ylabel('Time (ms)')
plt.title('ACTS')
#plt.legend(loc='best', fontsize=5)

# plt.subplot(2, 1, 2) # row 2, col 2 index 2
plt.subplot(2, 2, 3) 
for col in modelComplexity.columns:
    plt.scatter(modelComplexity[col], pMedici_time_mean_perModel[col], label=col, color=black)
plt.xlabel('Model Complexity\n(#features+#constraints)')
plt.ylabel('Time (ms)')
plt.title('pMEDICI')

plt.subplot(2, 2, 2) 
for col in modelComplexity.columns:
    plt.scatter(modelComplexity[col], ACTSFromScratch_size_mean_perModel[col], label=col, color=black)
plt.xlabel('Model Complexity\n(#features+#constraints)')
plt.ylabel('Size (number of Test Cases)')
plt.title('ACTS')

plt.subplot(2, 2, 4) 
for col in modelComplexity.columns:
    plt.scatter(modelComplexity[col], pMedici_size_mean_perModel[col], label=col, color=black)
plt.xlabel('Model Complexity\n(#features+#constraints)')
plt.ylabel('Size (number of Test Cases)')
plt.title('pMEDICI')
plt.subplots_adjust(left=0.125, bottom=0.9, right=1.8 , top=2.5 , wspace= 0.2, hspace= 0.35)
plt.show()

ax.figure.savefig(r'..\1.2c_pMEDICIvsACTS_modelComplexity_relationship.png', format='png', dpi=400,  bbox_inches='tight')