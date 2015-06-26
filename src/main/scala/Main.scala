//  There really should be a license here

import fr.umlv.unitex.jni.UnitexJni

/**
 * Created by aborsu on 26/06/15.
 * 
 * This program is a scala adaptation of the UnitexJniDemo distributed with Unitex' source code
 * The original file can be found on the Unitex svn server https://svnigm.univ-mlv.fr/svn/unitex
 * and contained the following copyright :
   *** Unitex
   *
   * Copyright (C) 2001-2014 Université Paris-Est Marne-la-Vallée <unitex@univ-mlv.fr>
   *
   * This library is free software; you can redistribute it and/or
   * modify it under the terms of the GNU Lesser General Public
   * License as published by the Free Software Foundation; either
   * version 2.1 of the License, or (at your option) any later version.
   *
   * This library is distributed in the hope that it will be useful,
   * but WITHOUT ANY WARRANTY; without even the implied warranty of
   * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   * Lesser General Public License for more details.
   *
   * You should have received a copy of the GNU Lesser General Public
   * License along with this library; if not, write to the Free Software
   * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA.
   *
   *
   ***
   * File created and contributed by Gilles Vollant (Ergonotics SAS) 
   * as part of an UNITEX optimization and reliability effort
   *
   * additional information: http://www.ergonotics.com/unitex-contribution/
   * contact : unitex-contribution@ergonotics.com
   *
   *
*/
object UnitexJniDemo {

  private val pathSeparator = if(UnitexJni.isUnderWindows()) "\\" else  "/"

  def getVirtualFilePfx():Option[String] = {

    if (UnitexJni.unitexAbstractPathExists("*")) {
      Some("*")
    } else if (UnitexJni.unitexAbstractPathExists("$:")) {
      Some("$:")
    } else {
      None
    }
  }

  def processUnitexWork(othersResDir: String,workingDicoFileName:String ,
      workingGraphFileName:String ,corpusPath:String ,corpusText:String ): String = {
    val pSep = pathSeparator
    UnitexJni.writeUnitexFile(UnitexJni.combineUnitexFileComponent(corpusPath,"corpus.txt"),corpusText)
  
    // we create offsets file offset1.txt and offset2.txt to get position against the original corpus in the xml file

    val cmdNorm = "Normalize " + UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"corpus.txt") + " -r "+UnitexJni.combineUnitexFileComponentWithQuote(othersResDir,"Norm.txt") + " --output_offsets="+ UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"offset1.txt") ;
    val cmdTok = "Tokenize " + UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"corpus.txt") + " -a "+ UnitexJni.combineUnitexFileComponentWithQuote(othersResDir,"Alphabet.txt") + " --input_offsets="+ UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"offset1.txt") + " --output_offsets="+ UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"offset2.txt") ;
    val cmdDico = "Dico -t "+ UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"corpus.snt")+ " -a " + UnitexJni.combineUnitexFileComponentWithQuote(othersResDir,"Alphabet.txt")+" "+UnitexJni.combineUnitexFileComponentWithQuote(workingDicoFileName) ;
    val cmdLocate = "Locate -t "+UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"corpus.snt")+ " " + UnitexJni.combineUnitexFileComponentWithQuote(workingGraphFileName)+ " -a " + UnitexJni.combineUnitexFileComponentWithQuote(othersResDir,"Alphabet.txt")+ " -L -R --all -b -Y";
    val cmdConcord = "Concord "+ UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"corpus_snt","concord.ind")+ " -m " + UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"corpus.txt") ;

    val cmdConcordXml = "Concord "+ UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"corpus_snt","concord.ind")+ 
      " --uima="+ UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"offset2.txt") +" --xml";

    UnitexJni.execUnitexTool("UnitexTool " + cmdNorm)
    UnitexJni.execUnitexTool("UnitexTool " + cmdTok)
    UnitexJni.execUnitexTool("UnitexTool " + cmdDico)
    UnitexJni.execUnitexTool("UnitexTool " + cmdLocate)
    UnitexJni.execUnitexTool("UnitexTool " + cmdConcord)
    UnitexJni.execUnitexTool("UnitexTool " + cmdConcordXml)

    // these 6 lines can be replaced by only one execution (with very small speed improvement)
    /*
    UnitexJni.execUnitexTool("UnitexTool { " + cmdNorm + " } { " + cmdTok + " } { " + cmdDico + " } { "  + cmdLocate + " } { " + cmdConcord + " } { " + cmdConcord2+ " }");

    */
    val merged =  UnitexJni.getUnitexFileString(UnitexJni.combineUnitexFileComponent(corpusPath,"corpus.txt"))
    val xml = UnitexJni.getUnitexFileString(UnitexJni.combineUnitexFileComponent(corpusPath,"corpus_snt","concord.xml"))
    xml
  }


  def main(args: Array[String]): Unit = {

    println("is ms-windows:"+UnitexJni.isUnderWindows()+" : "+System.getProperty("os.name")+ " "+java.io.File.separator)
    println("Usage : UnitexJniDemo [ressource_dir] [base_work_dir] [nb_loop] [param]")
    println("  param=0 : no vfs and no persistance")
    println("  param=1 : vfs and no persistance")
    println("  param=2 : no vfs and persistance")
    println("  param=3 : vfs and persistance (fastest)")
    println("")

    var baseWorkDir = if (args.length>=2) args(1) else "." + pathSeparator + "demojnires"
    val ressourceDir = if (args.length>=1) args(0) else "." + pathSeparator + "demojnires"
    var nbLoop=8
    var cfgParam = 3


    if (args.length >= 3)
        nbLoop = Integer.parseInt(args(2))

    if (nbLoop < 1)
        nbLoop = 1

    if (args.length>=4)
        cfgParam = Integer.parseInt(args(3))

    println("resource path : '"+ressourceDir+"' and work path is '"+baseWorkDir+"' and "+nbLoop+" executions");

    var graphResDir = UnitexJni.combineUnitexFileComponent(ressourceDir, "Graphs")
    var dictionnaryResDir = UnitexJni.combineUnitexFileComponent(ressourceDir, "Dela")
    // val othersResDir = UnitexJni.combineUnitexFileComponent(ressourceDir, "others");

    // UnitexJni.setStdOutTrashMode(true)

    val fusevfs = (cfgParam == 1) || (cfgParam == 3)
    val fusepersist = (cfgParam == 2) || (cfgParam == 3)

    val prefixVFS = if (fusevfs) getVirtualFilePfx() else None

    print("use vfs: " + fusevfs)
    if (fusevfs) {
      println(s"on: ${prefixVFS.get}")
    } else {
      println()
    }
    println("use persist: "+fusepersist)

    if (fusevfs) {
      println("Not implemented yet ...")

      assert(prefixVFS.isDefined)

      baseWorkDir = prefixVFS.get + baseWorkDir

      UnitexJni.copyUnitexFile(UnitexJni.combineUnitexFileComponent(baseWorkDir,"Alphabet.txt"),
              UnitexJni.combineUnitexFileComponent(prefixVFS+baseWorkDir,"Alphabet.txt"))

      UnitexJni.copyUnitexFile(UnitexJni.combineUnitexFileComponent(baseWorkDir,"Norm.txt"),
              UnitexJni.combineUnitexFileComponent(prefixVFS+baseWorkDir,"Norm.txt"))

      baseWorkDir = prefixVFS.get + baseWorkDir

      if (!fusepersist)
      {
          UnitexJni.copyUnitexFile(UnitexJni.combineUnitexFileComponent(dictionnaryResDir,"dela-en-public.bin"),
                                          UnitexJni.combineUnitexFileComponent(prefixVFS+dictionnaryResDir,"dela-en-public.bin"));

          UnitexJni.copyUnitexFile(UnitexJni.combineUnitexFileComponent(dictionnaryResDir,"dela-en-public.inf"),
                  UnitexJni.combineUnitexFileComponent(prefixVFS+dictionnaryResDir,"dela-en-public.inf"))

          dictionnaryResDir = prefixVFS + dictionnaryResDir

          UnitexJni.copyUnitexFile(UnitexJni.combineUnitexFileComponent(graphResDir,"AAA-hours-demo.fst2"),
                                          UnitexJni.combineUnitexFileComponent(prefixVFS+graphResDir,"AAA-hours-demo.fst2"))
          graphResDir = prefixVFS + graphResDir
      }
    }

    val dicoFileName = UnitexJni.combineUnitexFileComponent(dictionnaryResDir,"dela-en-public.bin")
    val graphFileName = UnitexJni.combineUnitexFileComponent(graphResDir,"AAA-hours-demo.fst2")

    val workingDicoFileName = if(fusepersist) {
      UnitexJni.loadPersistentDictionary(dicoFileName)
      } else {
        dicoFileName
      }

    val workingGraphFileName = if(fusepersist) {
      UnitexJni.loadPersistentFst2(graphFileName)
      } else {
        graphFileName
      }


    val CorpusWorkPath = UnitexJni.combineUnitexFileComponent(baseWorkDir, "workUnitexThread" + Thread.currentThread().getId())

    println("will work on "+CorpusWorkPath)
    UnitexJni.createUnitexFolder(CorpusWorkPath)
    UnitexJni.createUnitexFolder(UnitexJni.combineUnitexFileComponent(CorpusWorkPath,"corpus_snt"))
    var res=""

    val startT = System.currentTimeMillis();

    0.until(nbLoop).foreach{ i => 
      res = processUnitexWork(ressourceDir,workingDicoFileName,workingGraphFileName,CorpusWorkPath,
                   "I want watch  at "+ ((i%10)+1)+":02 am see at 6:00 pm before leave at 15.47")
    }
    val endT = System.currentTimeMillis();

    // debug : you can remove this line to inspect files
    UnitexJni.removeUnitexFolder(CorpusWorkPath)


    if (fusepersist)
    {
        UnitexJni.freePersistentDictionary(workingDicoFileName)
        UnitexJni.freePersistentFst2(workingGraphFileName)
    }

    println("")
    println("result:")
    println(res)
    println("time : "+(endT-startT)+" ms (average "+ ((endT-startT)/nbLoop)+" ms per iteration)")
    println("result:")
    println(res)
    println("time : "+(endT-startT)+" ms (average "+ ((endT-startT)/nbLoop)+" ms per iteration)")

  }
  
}
