package ch8

import ch8.schemes.NativeScriptScheme
import ch8.schemes.HKScriptScheme

def Definitions = new ConfigSlurper().parse(ch8.config.Definitions) 
def t = Definitions.NativeScript.t
def Tokens = Definitions.Tokens
def DefaultScriptScheme = Definitions.NativeScript.scheme
def GanaScheme = Definitions.Gana
def Guru = GanaScheme.GURU
def Laghu = GanaScheme.LAGHU

/**
 * @metamethod tokenize the script into individual varnas; each letter is a valid varna 
 * @closure @memoize
 * @given string any word or sentence 
 * @result list of varnas
 * @example 'kr.s.Na:' -> [k,r.,s.,N,a,:]
 */
Closure closureVarnas = { item, fromScript -> fromScript.tokenize(item) }
def memoizeVarnas = closureVarnas.memoize()
String.metaClass.varnas = { fromScript = DefaultScriptScheme -> memoizeVarnas(delegate, fromScript) }

/**
 * @metamethod direct exposition of a pratyaya or a pratyahara!
 * @given string pratyahara as a property of String.ak
 * @result list of varnas in the pratyahara
 * @example String.ak -> [a,e,u]
 * @ch8 #mahesvara sutra, #taparastatkAlasya
 */
SivaSutra sivaSutra = SivaSutra.instance
sivaSutra.metaClass.getProperty = { String pratyahara ->
  def metaProperty = SivaSutra.metaClass.getMetaProperty(pratyahara)
  if(metaProperty) {
    //if there is an existing property invoke that
    return metaProperty.getProperty(delegate)
  }
  pratyahara.taparaH() ? (pratyahara - t).varnas() : sivaSutra.collect(pratyahara)
}

ItRules itRules = ItRules.instance

/**
 * @metamethod remove the last hal iT and return the modified String
 * @given string pratyahara
 * @result string pratyahara minus hal 
 * @example sap -> sa
 * @ch8 #halantyam
 */
String.metaClass.halantyam = {
  def varnas = delegate.varnas() as List
  if (itRules.hasHalantyam(delegate)) {
    varnas.remove(varnas.size()-1)
  }
  varnas.join()
}

/**
 * @metamethod remove all the it-markers from a pratyaya
 * @given string
 * @result string
 * @example sap -> sa
 * @ch8 #tasya lopaH
 */
String.metaClass.tasyaLopah = { itRules.tasyaLopah(delegate) }

Samjna samjna = Samjna.instance

/**
 * @metamethod is a given string a vowel?
 * @given string varna
 * @result boolean
 * @ch8 #varna svaraH (shikShA)
 */
String.metaClass.svara = { delegate in samjna.svara }

/**
 * @metamethod is a varna string a consonant?
 * @given string varna
 * @result boolean
 */
String.metaClass.hal = { delegate in samjna.hal() }

/**
 * @metamethod remove last t from a pratyahara
 * @given string pratyahara
 * @result boolean
 * @ch8 #taparastatkaalasya
 */
String.metaClass.taparaH = { delegate.endsWith(t) }

/**
 * @metamethod is a varna string an anusvara?
 * @given string varna
 * @result boolean
 */
String.metaClass.anusvara = { delegate in samjna.anusvara }

/**
 * @metamethod is a varna string a visarga?
 * @given string varna
 * @result boolean
 */
String.metaClass.visarga = { delegate in samjna.visarga }

/**
 * @metamethod convert string to unicode
 * @given string word, sentence or verse
 * @result string unicode equivalent
 */
String.metaClass.unicode = { new NativeScriptScheme().toUnicode(delegate) }

/**
 * @metamethod if two consonants follow each other, then it is samyuktakshara
 * @given string syllable (kt.va)
 * @ch8 #halonantaraat samyogaH
 */
String.metaClass.samyuktakshara = { def v = delegate.varnas(); (v.size() > 1) ? v[0].hal() && v[1].hal() : false }

/**
 * @metamethod is the given varna is a single consolant
 * @given string varna
 * @result boolean
 * @ch8 #SivaSutra.hl
 */
String.metaClass.singleConsonant = { def v = delegate.varnas(); v.size() == 1 && v[0].hal() }

/**
 * @metamethod is the given varna is a single vowel
 * @given string varna
 * @result boolean
 * @ch8 #SivaSutra.ac
 */
String.metaClass.singleVowel = { def v = delegate.varnas(); v.size() == 1 && v[0].svara() }

/**
 * @metamethod does syllable end with vowel?
 * @given string syllable
 * @result boolean
 * @ch8 #ajantaH
 */
String.metaClass.endsWithSvara = { /* delegate.samyuktakshara() && */ delegate.varnas().last() in samjna.svara }
 
/**
 * @metamethod does syllable end with consonant?
 * @given string syllable
 * @result boolean
 * @ch8 #halantaH
 */
String.metaClass.endsWithVyanjana = { !delegate.endsWithSvara() }

/**
 * @metamethod is given varna a hrasva?
 * @given string varna
 * @result boolean
 * @ch8 #hrasva dIrgha pluta
 */
String.metaClass.hrasva = { delegate in samjna.hrasva } 

/**
 * @metamethod is given varna a dIrgha?
 * @given string varna
 * @result boolean
 * @ch8 #hrasva dIrgha pluta
 */
String.metaClass.dIrgha = { delegate in samjna.dIrgha }

/**
 * @metamethod is given syllable hrasva? (syllable - consonants == hrasva?)
 * @given string syllable
 * @result boolean
 * @ch8 #hrasva dIrgha pluta
 */
String.metaClass.hrasvakshara = { (delegate.varnas() - sivaSutra.hl).every { it.hrasva() } }

/**
 * @metamethod is given syllable dIrgha? (syllable - consonants == dIrgha?)
 * @given string syllable
 * @result boolean
 * @ch8 #hrasva dIrgha pluta
 */
String.metaClass.dIrghakshara = { (delegate.varnas() - sivaSutra.hl).any { it.dIrgha() } }

String.metaClass.token = { delegate in Tokens }

/**
 * @metamethod directly reference a range of letters in pratyahara list
 * @given string1 varna, string2 varna
 * @result list varnas between the two strings
 * @example range('a','N')) -> [a,e,u,N]
 */
List.metaClass.range = { first, last ->
  if (first instanceof String && last instanceof String) {
    delegate[delegate.indexOf(first)..delegate.indexOf(last)]
  } else {
    delegate[first..last]
  }
} 

/**
 * @metamethod get a list of prev,current,next set from a list
 * @given integer index
 * @result [prev,current,next], where current = list[index]
 */
List.metaClass.triplet = { i -> delegate.size() <= 0 ? [null,null,null] : i == 0 ? [null, delegate[i], (delegate.size()>1) ? delegate[i+1] : null] : (i==delegate.size()) ? [(i<1) ? null : delegate[i-1]] : [delegate[i-1], delegate[i], delegate[i+1]] }

/**
 * @metamethod overrides String's default iterator and allows direct iteration over varna-s
 * @given string
 * @result Iterator
 */
String.metaClass.iterator = { delegate.varnas().iterator() }

/**
 * @metamethod overrides String's default next() and allows direct iteration over varna-s
 * @given none
 * @result Iterator
 * @see iterator metamethod
 */
String.metaClass.next = { delegate.varnas().next() }

/**
 * @metamethod syllablize a given string, memoizes for faster access
 * @closure @memoize
 * @given string word, sentence or verse
 * @result list 
 * @see ScriptScheme
 * @example 'N.ArAyaNa:' -> [N.A, rA, ya, Na:]
 */
Closure closureSyllablize = { item -> new NativeScriptScheme().syllablize(item) }
def memoizeSyllablize = closureSyllablize.memoize()
String.metaClass.syllablize = { memoizeSyllablize(delegate) }

/**
 * @metamethod length of syllable array
 * @given string word, sentence or verse
 * @result integer number of syllables
 */
String.metaClass.syllableLength = { delegate.syllablize().size() }

/**
 * @metamethod find mAtrA for each syllable, metre := list of Guru-s and Laghu-s (1s and 0s) 
 * @given string word, sentence or verse
 * @result list metre
 * @example 'kr.s.Na:' -> [kr.,s.Na:] -> [1,1]
 * @see syllablize()
 * @ch8 mAtrA balam (shikShA)
 */
String.metaClass.metre = { 
  def s = delegate.syllablize()
  def metre = []
  for (int i=0;i<s.size();i++) {
    metre << (s[i+1]?.samyuktakshara() ? Guru : s[i].dIrghakshara() ? Guru : Laghu)
  }
  metre
}

/**
 * @metamethod join the 1s and 0s into a single string
 * @given string word, sentence or verse
 * @result string 100110001
 * @example 'kr.s.Na:' -> [kr.,s.Na:] -> [1,1] -> 11
 * @see metre()
 */
String.metaClass.metreAsString = { delegate.metre().join() }

/**
 * @metamethod group a given metre-string to list of octals (gana := list of octals)
 * @given string
 * @result list 
 * @example 11100110 -> [111,001,1,0]
 */
String.metaClass.metreAsGroup = {
  def size = delegate.size()
  def result = []
  //TODO how about using inject()?
  0.step(size-2, 3) { result << delegate.substring(it, it+3) }
  (((int) size/3)*3).step(size,1) { result << delegate.substring(it,it+1) }

  result
}

/**
 * @metamethod convert the grouped gana (list of octals) into named list 
 * @given list of octals
 * @result list of named gana
 * @example [111,000,1,0] -> [ma, na, ga, la]
 */
List.metaClass.gana = {
  delegate.collect { GanaScheme.findName(it) }
}

/**
 * @metamethod convert a given verse into a list ganas
 * @shortcut directly reference from a string
 * @given string verse
 * @result list named ganas
 * @example #verse  -> [ma, ga] 
 */
String.metaClass.gana = {
  delegate.metreAsString().metreAsGroup().gana()
}

/**
 * @closure splits a metre (list of 0s and 1s) and returns the metre (list of 0s and 1s) of the desired pAda
 * @given list metre, pAda#
 * @result list metre[#]
 */
Closure closurePada = { metre, q -> 
   int spq = metre.size()/4
   metre[(q*spq)..((q+1)*spq-1)]
}
   
/**
 * @closure curried closures - get different pAda-s for a verse
 * @curry pAda segment
 */
Closure closurePada1 = closurePada.rcurry(0)
Closure closurePada2 = closurePada.rcurry(1)
Closure closurePada3 = closurePada.rcurry(2)
Closure closurePada4 = closurePada.rcurry(3)

/**
 * @metamethod directly invoke pAda for a string
 * @given string sloka, verse
 * @result 
 */
String.metaClass.pada1 = { closurePada1(delegate.metre()) }
String.metaClass.pada2 = { closurePada2(delegate.metre()) }
String.metaClass.pada3 = { closurePada3(delegate.metre()) }
String.metaClass.pada4 = { closurePada4(delegate.metre()) }

