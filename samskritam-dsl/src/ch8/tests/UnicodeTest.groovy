package ch8.tests

import java.nio.charset.Charset

import ch8.config.Definitions
import ch8.schemes.NativeScriptScheme
import ch8.util.UnicodeUtil

evaluate(new File("ch8/Bootstrap.groovy"))

void testGuessEncoding() {
  File file = new File("c:/smartspace/test.txt")
  CharsetToolkit toolkit = new CharsetToolkit(file)

  // guess the encoding
  Charset guessedCharset = toolkit.getCharset()

  // create a reader with the correct charset
  BufferedReader reader = toolkit.getReader()

  // read the file content
  String line
  while ((line = reader.readLine())!= null) {
    println(line)
  }
}

void testUnicodeRead() {
  def reader = new File("c:/smartspace/a.txt").newReader("UTF-8")
  def result = ''
  reader.eachLine{ String line -> result += UnicodeUtil.stringToHex(line) }
  println result
}
  
void testUnicodeWrite() {
  def result = ['\u0915','\u093F','\u092E','\u0930','\u094D','\u0925','\u0902']
  UnicodeUtil.writeToFile("c:/smartspace/b.txt", result)
}

void testReadWriteUnicodeFile() {
  File file = new File("c:/smartspace/test-in.txt")
  def reader =file.newReader("UTF-8")
  def outFile= new File("c:/smartspace/test-out.txt")
  outFile.withWriter("UTF-8") { writer->
    reader.eachLine{line->
      writer.write(line)
    }
  }
}

void testPrintUnicodeMap() {
  ConfigSlurper Definitions = new ConfigSlurper().parse(ch8.config.Definitions)
  
  def unicodeMap = Definitions.UnicodeScript.varnamala
  unicodeMap.each {
    println it.key + "," + it.value
  }
}

void testUnicodeRange() {
  def result = ['\u0915','\u093F','\u092E','\u0930','\u094D','\u0925','\u0902']
  result.each {
    boolean svara = it >= '\u0900' && it <= '\u0914'
    boolean halfStopSvara = it >= '\u093E' && it <= '\u094C'
    
    boolean vyanjana = it >= '\u0915' && it <= '\u0939'
    boolean halfStopConsnant = it == '\u094D'
    
    println "svara: $svara, halfStopSvara: $halfStopSvara, vyanjana: $vyanjana, halfStopConsonant: $halfStopConsnant"
  }
}

void testFindUnicodeKey() {
  def Definitions = new ConfigSlurper().parse(ch8.config.Definitions)
  def text = ['\u0915','\u093F','\u092E','\u0930','\u094D','\u0925','\u0902']
  def unicodeMap = Definitions.UnicodeScript.varnamala
  text.each { print Definitions.UnicodeScript.findKey(it) }
  println ""
}

void testUnicode2Native() {
  def Definitions = new ConfigSlurper().parse(ch8.config.Definitions)
  def text = ['\u0915','\u093F','\u092E','\u0930','\u094D','\u0925','\u0902']
  println new NativeScriptScheme().fromUnicode(text)
}

void testNative2Unicode2Native() {
  def Definitions = new ConfigSlurper().parse(ch8.config.Definitions)
  def unicodeMap = Definitions.UnicodeScript.varnamala
  
  def original = 'suklAmbaraD.aram ves.Num sasevarNam cat.urBujam | prasaN.N.avad.aN.am D.yAyE.t. SarvaveGN.Opa sAN.t.ayE. ||'
  def fileName = "c:/smartspace/verse.txt"
  UnicodeUtil.writeToFile(fileName, original.unicode())
  
  def unicodeText = new File(fileName).getText("UTF-8")
  def convertedBack = new NativeScriptScheme().fromUnicode(unicodeText)
  println convertedBack
  
  assert convertedBack == original
}


//testReadWriteUnicodeFile()
//testUnicodeRead()
//testUnicodeWrite()
//testFindUnicodeKey()
testUnicode2Native()
//testNative2Unicode2Native()
