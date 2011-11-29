package ch8.tests

import java.util.List

import ch8.ItRules
import ch8.SivaSutra
import ch8.schemes.SimpleScriptScheme
import ch8.Samjna

// DSL: varnas() closure - tokenize the script into individual varnas (list)
String.metaClass.varnas = {
  new SimpleScriptScheme().tokenize(delegate)
}

// DSL: halantyam() closure - remove the last hal iT and return the modified String
String.metaClass.halantyam = {
  ItRules itRules = ItRules.instance
  def varnas = delegate.varnas() as List
  if (itRules.hasHalantyam(delegate)) {
    varnas.remove(varnas.size()-1)
  }
  varnas.join()
}

// DSL: tasyaLopah() closure - remove all the it-markers from a pratyaya
String.metaClass.tasyaLopah = {
  ItRules itRules = ItRules.instance
  itRules.tasyaLopah(delegate)
}

Samjna samjna = Samjna.instance
// DSL: is a given string a vowel?
String.metaClass.svara = { delegate in samjna.svara }

// DSL: is a given string a consonant?
String.metaClass.hal = { delegate in samjna.hal() }

// DSL: taparastatkaalasya rule
String.metaClass.taparaH = { delegate.endsWith('t.') }

// DSL: is a given string an anusvara?
String.metaClass.anusvara = { delegate in samjna.anusvara }

// DSL: is a given string a visarga?
String.metaClass.visarga = { delegate in samjna.visarga }

// DSL: syllablize
String.metaClass.syllablize = { new SimpleScriptScheme().syllablize(delegate) }

// DSL: convert string to unicode
String.metaClass.unicode = { new SimpleScriptScheme().toUnicodeString(delegate) }

String.metaClass.samyogakshara = { delegate.varnas().size() > 1 }

// DSL: is samyoga akshara and ends with vowel?
String.metaClass.endsWithSvara = { delegate.samyogakshara() && delegate.varnas().last() in samjna.svara }
 
// DSL: is samyoga akshara and ends with consonant?
String.metaClass.endsWithVyanjana = { !delegate.endsWithSvara() }

// DSL: Direct exposition of a pratyaya or a pratyahara!
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
		if (pratyahara.taparaH()) {
      result = (pratyahara - 't.').varnas()
    } else {
    	result = sivaSutra.collect(pratyahara)
    }
  }
  result
}

//short-cut method for directly referencing a range of letters in pratyahara list
List.metaClass.range = { first, last ->
  if (first == String && last == String) {
    delegate[delegate.indexOf(first)..delegate.indexOf(last)]
  } else {
    delegate[first..last]
  }
}

List.metaClass.triplet = { i -> delegate.size() <= 0 ? [null,null,null] : i == 0 ? [null, delegate[i], (delegate.size()>1) ? delegate[i+1] : null] : (i==delegate.size()) ? [(i<1) ? null : delegate[i-1]] : [delegate[i-1], delegate[i], delegate[i+1]] }
String.metaClass.iterator = { delegate.varnas().iterator() }
String.metaClass.next = { delegate.varnas().next() }

void testSivaSutra() {
  
  println "\n ---- testSivaSutra ---- \n\n"
  SivaSutra sivaSutra = SivaSutra.instance

  //print the maheshvara sutrani
  println "\nSivaSutra table format:"
  sivaSutra.table.each { println it } 
  
  //print a flattened version of the maheshvara sutrani
  println "\nSivaSutra list format:"
  println sivaSutra.list 
  
  //another way to print flattened maheshvara sutrani
  println "\nSivaSutra string format:"
  sivaSutra.each { print it } 

  //print only the it markers
  println "\nsivaSutra.itVarnas: " + sivaSutra.itVarnas 

  //check it markers
  ['n.', 'l', 'k', 'm'].each { assert sivaSutra.isIt(it) }

  //expand pratyahara including the it
  assert sivaSutra.range('ak') == ['a','e','u','N!','r.','l.','k!'] 
  
  //real pratyahara-s excluding iT
  assert ['a', 'e', 'u', 'r.', 'l.']== sivaSutra.collect('ak')
  ['yr', 'ik', 'Js'].each { println sivaSutra.collect(it) }
  
  //Access pratyahara-s directly just like properties!
  assert ['a', 'e', 'u'] == sivaSutra.'aN'
  println "pratyahara ak: " + sivaSutra.ak
  println "pratyahara hl: " + sivaSutra.hl
  println "pratyahara lm: " + sivaSutra.lm
}

void testItRules() {
  println "\n ---- testItRules ---- \n\n"

  ItRules itRules = ItRules.instance

  println itRules.ajanunasika //prints all the ac anunasikas
  
  assert "lyut".varnas() == ['l', 'y', 'u', 't']
}


void testHalantyamRule() {
  println "\n ---- testHalantyamRule ---- \n\n"
  
  //print the pratyahara-s after the halantyam rule applied
  ["kt.va", "Gan.", "kt.vat.", "sap", "lyu-t", "saN", "sat.r."].each { println it + " = " + it.halantyam() }

  assert 'kt.va' == 'kt.va'.halantyam() //no rule applied, so same
  assert 'kt.va'  == 'kt.vat.'.halantyam() //t. removed due to halantyam() rule
  assert 'Ga' == 'Gan.'.halantyam() //n. removed due to halantyam() rule (though also applicable by lasaku rule)
}

void testTasyaLopahRule() {
  println "\n ---- testTasyaLopahRule ---- \n\n"
  
  ["Gan.", "kt.vat.", "sap", "lyu-t", "saN", "satr."].each { println it + " = " + it.tasyaLopah() }

  assert 'a' == 'Gan.'.tasyaLopah()
  assert 't.va' == 'kt.vat.'.tasyaLopah()
}

void testSamjnaSutras() {
  println "\n ---- testSamjnaSutras ---- \n\n"

  Samjna samjna = Samjna.instance
  
  println "vruddhi: " + samjna.vruddhi()
  assert ['A', 'i', 'O.'] == samjna.vruddhi()
  
  println "guna: " + samjna.guna()
  assert ['a', 'E.', 'o'] == samjna.guna()
  
  println "all consonants: " + samjna.hal()
}

List testSyllablize() {
  println "\n ---- testSyllablize ---- \n\n"
  def examples = ['suklAmBaraD.aram', 'kr.s.Na:', 'kArtsnyam', 'vASud.EvadvAd.asAks.aramaN.t.rAN.t.argat.a', 'kuruks.Et.rE', 'AdesEs.a', 'ad.rerAjaSut.At.majam']
  examples.each { println it + ' = ' + new SimpleScriptScheme().syllablize(it) }
 
  assert ['su', 'klA', 'mBa', 'ra', 'D.a', 'ra', 'm'] == new SimpleScriptScheme().syllablize('suklAmBaraD.aram')
}

List testUnicode() {
  println "\n ---- testUnicode ---- \n\n"
  
  def scheme = new SimpleScriptScheme()
  
  def examples = ['suklAmBaraD.aram', 'kr.s.Na:', 'kArtsnyam', 'vASudEvadvAdasAks.aramaNt.rAnt.argat.a', 'kuruks.Et.rE', 'AdesEs.a', 'ad.rerAjaSut.At.majam']
  examples.each { println scheme.toUnicode(it) }
}

List testIfEndsWithSvara() {
  println "\n ---- testIfEndsWithSvara ---- \n\n"
  ['a', 'E', 't.at.', 'kam', 'Barat.', 'nadE', 'svara'].each { println it + " = " + it.endsWithSvara() }
}

void testStringIterator() {
  println "\n ---- testStringIterator ---- \n\n"
  def examples = ['suklAmBaraD.araM', 'kr.s.Na:', 'kArt.Snyam', 'vASud.EvadvAdasAks.aramaNt.rAnt.argat.a', 'kuruks.Et.rE', 'Ad.esEs.a', 'ad.rerAjaSut.At.majam']
  examples.each { example -> println example + ' = ' + example.each {}.collect { it } }
}

void testListTriplet() {
  println "\n ---- testListTriplet ---- \n\n"
  def prev, current, next
  List list = 1..10 as List
  
  println "\n test full list \n\n"
  (0..list.size()-1).each {
    println list.triplet(it)
  }
  
  println "\n test partial list \n\n"
  (0..(list.size()-1)/2).each {
    println list.triplet(it)
  }

  println "\n test one value \n\n"
  list = 1..1 as List
  (0..list.size()-1).each {
    println list.triplet(it)
  }
  
  println "\n test no values \n\n"
  list = []
  (0..0).each {
    println list.triplet(it)
  }
}

void testAll() {
  testSivaSutra()
  testItRules()
  testHalantyamRule()
  testTasyaLopahRule()
  testSamjnaSutras()
  testSyllablize()
  testUnicode()
  testIfEndsWithSvara()
  testStringIterator()
  testListTriplet()
}

//testSivaSutra()
//testItRules()
//testHalantyamRule()
//testTasyaLopahRule()
//testSamjnaSutras()
//testSyllablize()
testUnicode()
//testIfEndsWithSvara()
//testStringIterator()
//testListTriplet()