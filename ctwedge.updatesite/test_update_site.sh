#!/bin/bash

# save the current location
updatesite=$(pwd)
echo "$updatesite" 

# make a remp dir
#mkdir temp
cd temp
# first download eclipse
# use he mirror service, skip if it alredy exits 
wget --no-clobber http://www.mirrorservice.org/sites/download.eclipse.org/eclipseMirror/technology/epp/downloads/release/2021-03/R/eclipse-java-2021-03-R-win32-x86_64.zip

# test with a fresh installation
#
# remove old eclipse
rm -rf eclipse_fresh
#unzip the new one
unzip eclipse-java-2021-03-R-win32-x86_64.zip
mv eclipse eclipse_fresh

# install the plugins
cd eclipse_fresh
#
#./eclipsec.exe -noSplash -application org.eclipse.equinox.p2.director  -repository file://$updatesite  -installIU Asmeta
# ./eclipsec.exe -noSplash -application org.eclipse.equinox.p2.director  -repository file:/home/garganti/progettiDaSVN/asmeta/asmeta_update_site  -installIU Asmeta
# 
# this is to test the local update site
./eclipsec.exe -noSplash -application org.eclipse.equinox.p2.director  -repository https://download.eclipse.org/releases/latest,file:/D:\\AgHome\\progettidaSVNGIT\\asmeta\\asmeta_update_site -installIU Asmeta
# this is to test the officila update site
#./eclipsec.exe -noSplash -application org.eclipse.equinox.p2.director  -repository https://raw.githubusercontent.com/asmeta/asmeta_update_site/master,https://download.eclipse.org/releases/latest -installIU Asmeta

cd ..

# test as update - NON RIESCO p2. director non permette l'update
# remove old eclipse
#rm -rf eclipse_update
#unzip the new one
#unzip eclipse-java-2020-12-R-win32-x86_64.zip 
#mv eclipse eclipse_update
#cd eclipse_update
# install from old repo
#./eclipsec.exe -noSplash -application org.eclipse.equinox.p2.director  -repository https://raw.githubusercontent.com/asmeta/asmeta_update_site/master,https://download.eclipse.org/releases/latest -installIU Asmeta
# now update with the new one
# NON FUNZUIONA DA FARE A MANO
#./eclipsec.exe -noSplash -application org.eclipse.equinox.p2.director  -repository https://download.eclipse.org/releases/latest,file:/D:\\AgDocuments\\progettiDaSVN\\asmeta\\asmeta_update_site -installIU Asmeta


# delete temp
#rm -rf temp
