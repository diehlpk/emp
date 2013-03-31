#!/bin/bash

# Vorbereitung
cp WebContent/include/Tail.html Tail.bak
date=`date "+%d.%m.%Y"`
filerev=`cat Tail.bak | grep footerlist | cut -d">" -f4 | cut -d"<" -f1 | cut -d"(" -f2 `
rev=`svn info | grep Revision | cut -d" " -f2`
filedate=`cat Tail.bak | grep "footer" | cut -d"<" -f6 | cut -d";" -f3`
sed -e "s/"$filedate"/"$date"/g" -e "s/"$filerev"/"r"$rev"")/g" Tail.bak > WebContent/include/Tail.html
