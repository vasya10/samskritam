package ch8.schemes;

import ch8.Dhatu
import ch8.Pratyaya
import ch8.SivaSutra

/**
 * DSL for Devanagari Scripts !!!!!
 * 
 * @format string.hk -> indicates that the given string is in HK encoding and tokenizes its varnas
 * @author vsrinivasan
 *
 */

@Category(String)
class Dsl {
  def getHk() {
    new HKScriptScheme().tokenize(this)
  }
  
  def getNativeScript() {
    new NativeScriptScheme().tokenize(this)
  }
  
  def getPratyahara() {
    return SivaSutra.instance.collect(this)
  } 
  
  def getPratyaya() {
    Pratyaya p = new Pratyaya(this)
    return p
  }
  
  def getUnicode() {
    //new NativeScriptScheme().fromUnicode(this)
  }
  
  def getDhatu() {
    return new Dhatu(this) //).load(this)
  }
}
