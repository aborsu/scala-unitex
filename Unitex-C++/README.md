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
UnitexJni.jar:
Located in unitex\Unitex-C++\bin

UnitexJni.dll:
Go to unitex\Unitex-C++\build
Open UnitexToolAndLib_vs2013.sln (Visual Studio)

Build the Solution Config -> Release Platforms -> x64

In case of : ```Error	22	error C1083: Cannot open source file: '..\tre-0.8.0\lib\regcomp.c': No such file or directory	(...)```
	* Copy tre-0.8.0/ and yaml-0.1.6/ from build/ to the parent folder
	
The output will be in build\x64\UnitexJni\Release, only the .dll is needed
	
