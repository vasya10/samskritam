package ch8.schemes;

/**
 * DSL for Devanagari Scripts !!!!!
 * 
 * @format string.hk -> indicates that the given string is in HK encoding and tokenizes its varnas
 * @author vsrinivasan
 *
 */

@Category(String)
class Script {
  def getHk() {
    new HKScriptScheme().tokenize(this)
  }
  
  def getNativeScript() {
    new NativeScriptScheme().tokenize(this)
  } 
  
  def getUnicode() {
    //new NativeScriptScheme().fromUnicode(this)
  }
}
