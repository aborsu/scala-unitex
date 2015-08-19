##To compile unitex for linux:

in the unitex folder type:<br />
```cd Src/C++/build```
 
Compile the unitex JNI library using the following command, don't forget to replace $JAVA_HOME with the path to your java home.<br />
```make 64BITS=yes TRE_DIRECT_COMPILE=yes JNILIBRARY=yes ADDITIONAL_CFLAG+=-I$JAVA_HOME/include/ ADDITIONAL_CFLAG+=-I$JAVA_HOME/include/linux/```
 
now move to the bin folder:<br />
```cd ../bin```

This folder should contain three files:
 * libUnitexJni.so
 * Test_lib
 * UnitexJni.jar

Move them to the lib folder in the scala-unitex project.

##To compile unitex for windows(64bit):

unitex\Unitex-C++\bin contient le UnitexJni.jar

unitex\Unitex-C++\build contient UnitexToolAndLib_vs2013.sln, à ouvrir avec Visual Studio (2013)
	-> Compile en Release x64
	-> Erreurs :
		-> Path ../tre et ../yaml
		-> copy tre-0.8.0 & yaml-0.1.6 dans le dossier parent
	
-		-> Path JNI
-			-> Ouvrir les propriété de UnitexJNI, sous C/C++ > General > Additional Include Directories, ajouter le path vers le JNI de java (genre C:/program files/java ...)
	
-> Résultat est dans "unitex\Unitex-C++\build\x64\UnitexJni\Release", le .dll suffit
