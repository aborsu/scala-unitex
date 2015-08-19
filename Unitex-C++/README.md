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
