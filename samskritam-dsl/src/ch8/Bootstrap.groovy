package ch8


import java.util.List

import ch8.schemes.SimpleScriptScheme

/*
 * DSL: varnas() closure - tokenize the script into individual varnas (list)
 */
String.metaClass.varnas = {
  new SimpleScriptScheme().tokenize(delegate)
}

/*
 * DSL: halantyam() closure - remove the last hal iT and return the modified String
 */
String.metaClass.halantyam = {
  ItRules itRules = ItRules.instance
  def varnas = delegate.varnas() as List
  if (itRules.hasHalantyam(delegate)) {
    varnas.remove(varnas.size()-1)
  }
  varnas.join()
}

/*
 * DSL: tasyaLopah() closure - remove all the it-markers from a pratyaya
 */
String.metaClass.tasyaLopah = {
  ItRules itRules = ItRules.instance
  itRules.tasyaLopah(delegate)
}

Samjna samjna = Samjna.instance
/*
 * DSL: is a given string a vowel?
 */
String.metaClass.svara = { delegate in samjna.svara }

/*
 * DSL: is a given string a consonant?
 */
String.metaClass.hal = { delegate in samjna.hal() }

/*
 * DSL: is a given string an anusvara?
 */
String.metaClass.anusvara = { delegate in samjna.anusvara }

/*
 * DSL: is a given string a visarga?
 */
String.metaClass.visarga = { delegate in samjna.visarga }

/*
* DSL: syllablize
*/
String.metaClass.syllablize = {
  new SimpleScriptScheme().syllablize(delegate)
}


/*
 * DSL: Direct exposition of a pratyaya or a pratyahara!
 */
SivaSutra sivaSutra = SivaSutra.instance
sivaSutra.metaClass.getProperty = { String pratyahara ->
  def metaProperty = SivaSutra.metaClass.getMetaProperty(pratyahara)
  def result
  if(metaProperty) {
    //if there is an existing property invoke that
    result = metaProperty.getProperty(delegate)
  } else {
    //inspect the property and convert it to varnas
    //taparastatkaalasya rule; need to formulate in a better way
    if (pratyahara.endsWith('t.')) {
      result = (pratyahara - 't.').varnas()
    } else {
      result = sivaSutra.collect(pratyahara)
    }
  }
  result
}
