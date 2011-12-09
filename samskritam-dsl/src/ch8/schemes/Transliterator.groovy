package ch8.schemes

class Transliterator {

  static def Definitions = new ConfigSlurper().parse(ch8.config.Definitions)
  /**
   * @closure maps any script to any other script, works only for 1:1 mapping
   */
  static def scriptMapper = { varna, fromScript, toScript -> toScript.varnamala[fromScript.varnamala.indexOf(varna)] }

  /**
   * @closure transliterate from one script to another script
   */
  static def transliterate = { text, fromScript, toScript -> text.varnas(fromScript.scheme).collect { scriptMapper.call(it, fromScript, toScript) } }

  static def nativeToHk = transliterate.rcurry(Definitions.NativeScript, Definitions.HKScript)
  static def hkToNative = transliterate.rcurry(Definitions.HKScript, Definitions.NativeScript)
}
