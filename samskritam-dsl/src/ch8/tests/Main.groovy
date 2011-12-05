package ch8.tests

import java.util.List

import ch8.ItRules
import ch8.Samjna
import ch8.SivaSutra
import ch8.schemes.SimpleScriptScheme
import ch8.util.StopWatch

examples = ['suklAmbaraD.araM', 'kr.s.Na:', 'kArt.SN.yam', 'vASud.Evad.vAd.asAks.aramaN.t.rAN.t.argat.a', 'kuruks.E.t.rE.', 
            'Ad.esE.s.a', 'ad.rerAjaSut.At.majam','srEmad.ves.NvanGreN.es.Ta:','yamAt.ArAjaBAN.aSalagaM'
           ]

slokas = [ 'suklAmbaraD.aram ves.Num sasevarNam cat.urBujam | prasaN.N.avad.aN.am D.yAyE.t. SarvaveGN.Opa sAN.t.ayE. ||',
           'vyASam vaSes.Ta N.aPt.Aram sakt.E.: pO.t.ram akalmas.am | parASarAt.majam vaN.d.E. sukat.At.am t.apON.N.eD.em ||',
           'yA kuN.d.E.N.d.u t.us.Ara hAraD.avalA yA suBravaSt.rAvr.t.A | yA vENAvara d.aNdamaNdet.akarA yA svEt.a pad.mASaN.A | yA brahmAcyut.a sankarA praBr.t.eBerd.E.vI: Sad.A vaN.d.et.A | SA mAM pAt.u SaraSvat.E Bagavat.E N.e:sE.s.a jAdyApahA ||',
           'rAmAya sAsvat.a SuveSt.r.t.a s.adguNAya SarvE.svarAya balavErya mahArNavAya | N.atvA lelangayes.u arNavam utpapAta Nes.pEdya t.am gerevaram pavaN.asya SUN.u:'
         ]

chandasDefs = [ 'gmO. cet. kaN.yA',
                'BgO. get.e pankt.e:',
                't.yO. cet. t.aN.umaD.yamA'
              ]
// MetaMethod: varnas() closure - tokenize the script into individual varnas (list)
Closure varnasClosure = { item -> new SimpleScriptScheme().tokenize(item) }
varnasClosure.memoize()
//String.metaClass.varnas = { new SimpleScriptScheme().tokenize(delegate) }
String.metaClass.varnas = { varnasClosure.call(delegate) }

// MetaMethod: Direct exposition of a pratyaya or a pratyahara!
SivaSutra sivaSutra = SivaSutra.instance
sivaSutra.metaClass.getProperty = { String pratyahara ->
  def metaProperty = SivaSutra.metaClass.getMetaProperty(pratyahara)
//  def result
  if(metaProperty) {
    //if there is an existing property invoke that
    return metaProperty.getProperty(delegate)
  }
  pratyahara.taparaH() ? (pratyahara - 't.').varnas() : sivaSutra.collect(pratyahara)
}

// MetaMethod: halantyam() closure - remove the last hal iT and return the modified String
ItRules itRules = ItRules.instance
String.metaClass.halantyam = {
  def varnas = delegate.varnas() as List
  if (itRules.hasHalantyam(delegate)) {
    varnas.remove(varnas.size()-1)
  }
  varnas.join()
}

// MetaMethod: tasyaLopah() closure - remove all the it-markers from a pratyaya
String.metaClass.tasyaLopah = {
  //ItRules itRules = ItRules.instance
  itRules.tasyaLopah(delegate)
}

Samjna samjna = Samjna.instance

// MetaMethod: is a given string a vowel?
String.metaClass.svara = { delegate in samjna.svara }

// MetaMethod: is a given string a consonant?
String.metaClass.hal = { delegate in samjna.hal() }

// MetaMethod: taparastatkaalasya rule
String.metaClass.taparaH = { delegate.endsWith('t.') }

// MetaMethod: is a given string an anusvara?
String.metaClass.anusvara = { delegate in samjna.anusvara }

// MetaMethod: is a given string a visarga?
String.metaClass.visarga = { delegate in samjna.visarga }

// MetaMethod: convert string to unicode
String.metaClass.unicode = { new SimpleScriptScheme().unicodize(delegate) }

// MetaMethod: if two consonants follow each other, then it is samyuktakshara
String.metaClass.samyuktakshara = { def v = delegate.varnas(); (v.size() > 1) ? v[0].hal() && v[1].hal() : false }

String.metaClass.singleConsonant = { def v = delegate.varnas(); v.size() == 1 && v[0].hal() }

String.metaClass.singleVowel = { def v = delegate.varnas(); v.size() == 1 && v[0].svara() }

// MetaMethod: is samyoga akshara and ends with vowel?
String.metaClass.endsWithSvara = { delegate.samyuktakshara() && delegate.varnas().last() in samjna.svara }
 
// MetaMethod: is samyoga akshara and ends with consonant?
String.metaClass.endsWithVyanjana = { !delegate.endsWithSvara() }

String.metaClass.hrsva = { delegate in samjna.hrsva } 

String.metaClass.dIrgha = { delegate in samjna.dIrgha }

String.metaClass.hrsvakshara = { (delegate.varnas() - sivaSutra.hl).every { it.hrsva() } }

String.metaClass.dIrghakshara = { (delegate.varnas() - sivaSutra.hl).any { it.dIrgha() } }

String.metaClass.token = { delegate in [' ','|'] }

//short-cut method for directly referencing a range of letters in pratyahara list
List.metaClass.range = { first, last ->
  if (first == String && last == String) {
    delegate[delegate.indexOf(first)..delegate.indexOf(last)]
  } else {
    delegate[first..last]
  }
} 

//create a [prev,current,next] list triplet from an array
List.metaClass.triplet = { i -> delegate.size() <= 0 ? [null,null,null] : i == 0 ? [null, delegate[i], (delegate.size()>1) ? delegate[i+1] : null] : (i==delegate.size()) ? [(i<1) ? null : delegate[i-1]] : [delegate[i-1], delegate[i], delegate[i+1]] }

//overrides default iterator; iterate directly over the varna-s from a string
String.metaClass.iterator = { delegate.varnas().iterator() }

//overrides default String next(); get the next varna in a string, goes along with iterator override
String.metaClass.next = { delegate.varnas().next() }

/** syllable related functions */

// MetaMethod: syllablize
Closure c = { item -> new SimpleScriptScheme().syllablize(item) }
c.memoize()
String.metaClass.syllablize = { new SimpleScriptScheme().syllablize(delegate) }
//String.metaClass.syllablize = { c.call(delegate) }

String.metaClass.syllableLength = { delegate.syllablize().size() }

//Meta-method: returns weight of a given syllable (assumes syllable does not include tokens) 
//String.metaClass.syllableWeight = { def v = delegate.varnas(); v.sum { it.hal() ? 0.5 : it.hrsva() ? 0.5 : 1.5 } }
String.metaClass.syllableWeight = { delegate.samyuktakshara() ? 2.0 : delegate.dIrghakshara() ? 1.5 : 1.0 }

//Meta-method: returns list of weight of each syllable (syllablize method will eliminate tokens)
String.metaClass.weights = { delegate.syllablize().collect { it.syllableWeight() } }

//Meta-method: returns total weight of given sentence 
String.metaClass.totalWeight = { delegate.weights().sum() }


// is given syllable a guru
String.metaClass.chandas = { 
  def w = delegate.weights()
  def s = delegate.syllablize()
  def metre = []
  //println 'weights = ' + w
  for (int i=0;i<w.size();i++) {
    def k = w[i+1]>=2.0 ? '1' : w[i]>=1.5 ? '1' : '0'
    println "$i: ${s[i]}, ${w[i]}, ${w[i+1]}, $k"
    metre << k
  }
  metre
}

String.metaClass.gana = {
  def chandas = delegate.chandas()
  int max3 = ((int) (chandas.size()/3))*3
  def g = [] 
  for (int i=0; i<max3; i+=3) {
   g << "$i: ${chandas[i]}${chandas[i+1]}${chandas[i+2]}"
  }
  for (int i=max3;i<chandas.size();i++) {
    g << "$i: ${chandas[i]}"
  }
  g
}

/* =============================================================================================================================== *
 * =============================================================================================================================== *
 * =============================================================================================================================== * 
 */

void testVarnas() {
  println '\n---- testVarnas ---- \n\n'
  
  println 'SaMSkr.t.am'.varnas()
  println slokas[0].varnas()
  
  assert 'lyut'.varnas() == ['l', 'y', 'u', 't']
  assert 'kr.s.Na:'.varnas() == ['k', 'r.', 's.', 'N', 'a', ':']
  assert 'SaMSkr.t.am'.varnas() == ['S', 'a', 'M', 'S', 'k', 'r.', 't.', 'a', 'm']
}

void testSivaSutra() {
  println '\n---- testSivaSutra ---- \n\n'

  SivaSutra sivaSutra = SivaSutra.instance

  //print the maheshvara sutrani
  println 'SivaSutra table-format:'
  sivaSutra.table.each { println it } 
  
  //print a flattened version of the maheshvara sutrani
  println '\nSivaSutra list-format:'
  println sivaSutra.list 
  
  //another way to print flattened maheshvara sutrani
  println '\nSivaSutra string-format:'
  sivaSutra.each { print it } 

  //print only the it markers
  println '\n\nSivaSutra.itVarnas: ' + sivaSutra.itVarnas 

  ['yr', 'ek', 'Js'].each { println it + ' = ' + sivaSutra.collect(it) }

  println 'pratyahara ak: ' + sivaSutra.ak
  println 'pratyahara hl: ' + sivaSutra.hl
  println 'pratyahara lm: ' + sivaSutra.lm

  //Asserts
  //check it markers
  ['n.', 'l', 'k', 'm'].each { assert sivaSutra.isIt(it) }

  //expand pratyahara including the it
  assert sivaSutra.range('ak') == ['a','e','u','N!','r.','l.','k!'] 
  
  //real pratyahara-s excluding iT
  assert ['a', 'e', 'u', 'r.', 'l.']== sivaSutra.collect('ak')
  
  //Access pratyahara-s directly just like properties!
  assert ['a', 'e', 'u'] == sivaSutra.'aN'
}

void testItRules() {
  println '\n---- testItRules ---- \n\n'

  ItRules itRules = ItRules.instance

  println itRules.ajanunasika //prints all the ac anunasikas
  println itRules.cu
  println itRules.tu
  println itRules.allItVarnas
}


void testHalantyamRule() {
  println '\n---- testHalantyamRule ---- \n\n'
  
  //print the pratyahara-s after the halantyam rule applied
  ['kt.va', 'Gan.', 'kt.vat.', 'sap', 'lyu-t', 'saN', 'sat.r.'].each { println it + ' = ' + it.halantyam() }

  //Asserts  
  assert 'kt.va' == 'kt.va'.halantyam() //no rule applied, so same
  assert 'kt.va'  == 'kt.vat.'.halantyam() //t. removed due to halantyam() rule
  assert 'Ga' == 'Gan.'.halantyam() //n. removed due to halantyam() rule (though also applicable by lasaku rule)
}

void testTasyaLopahRule() {
  println '\n---- testTasyaLopahRule ---- \n\n'
  
  ['Gan.', 'kt.vat.', 'sap', 'lyu-t', 'saN', 'satr.'].each { println it + ' = ' + it.tasyaLopah() }

  //Asserts  
  assert 'a' == 'Gan.'.tasyaLopah()
  assert 't.va' == 'kt.vat.'.tasyaLopah()
  assert 'a' == 'sap'.tasyaLopah()
}

void testSamjnaSutras() {
  println '\n---- testSamjnaSutras ---- \n\n'

  Samjna samjna = Samjna.instance
  
  println samjna.allVarnas
  println 'vruddhi: ' + samjna.vruddhi()
  println 'guna: ' + samjna.guna()
  println 'all consonants: ' + samjna.hal()

  //Asserts  
  assert ['A', 'I', 'O.'] == samjna.vruddhi()
  assert ['a', 'E.', 'O'] == samjna.guna()
  
}

void testIfEndsWithSvara() {
  println '\n---- testIfEndsWithSvara ---- \n\n'
  
  ['a', 'E', 't.at.', 'kam', 'Barat.', 'nadE', 'svara'].each { println it + ' = ' + it.endsWithSvara() }
}

void testStringIterator() {
  println '\n---- testStringIterator ---- \n\n'
  
  examples.each { example -> println example + ' = ' + example.each {}.collect { it } }
}

void testSamyuktakshara() {
  slokas.each { it.tokenize().each { it.syllablize().each { println it + ' = ' + it.samyuktakshara() } } }
}

void testSyllablizeWord() {
  println '\n---- testSyllablizeWord ---- \n\n'
  examples.each { println it + ' = ' + new SimpleScriptScheme().syllablize(it) }
 
  //Asserts  
  assert ['su', 'klA', 'mba', 'ra', 'D.a', 'ram'] == new SimpleScriptScheme().syllablize('suklAmbaraD.aram')
}

void testSyllablizeSloka() {
  println '\n---- testSyllablizeSloka ---- \n\n'
  
  slokas.each { println it.syllablize() } 
}

void testSlokaSize() {
  println '\n---- testSlokaSize ---- \n\n'
  
  slokas.each { println it + ' = ' + it.syllableLength() }
}

void testUnicode() {
  println '\n---- testUnicode ---- \n\n'
  
  slokas.each { 
    println it + '<br/>'
    println it.unicode() + '<br/>'
  }
  //Asserts  
  assert 'suklAmbaraD.araM'.unicode() == '&#x936;&#x941;&#x915;&#x94D;&#x932;&#x93E;&#x92E;&#x94D;&#x92C;&#x930;&#x927;&#x930;&#x902;'
}

void testWeight() {
  println '\n---- testWeight ---- \n\n'

  slokas.each { println it + ' = ' + it.syllableLength() }
  slokas.each { println it.weights() } 
}

void testChandas() {
  println '\n---- testChandas ---- \n\n'
  
  slokas.each { sloka ->
    def syllables = sloka.syllablize()
    def chandas = sloka.chandas()
    def weights = sloka.weights()
    
    println 'syllables = ' + syllables + ' = ' + syllables.size()
    println 'weights = ' + weights + ' = ' + weights.size()
    println 'chandas = ' + chandas + ' = ' + chandas.size()
    def map = [:]
    for (int i=0;i<syllables.size(); i++) {
      map.put(syllables[i], '(' + weights[i] + '-' + chandas[i] + ')')
    }
    println map
    println sloka.gana()
  }
}

void testChandasDefinitions() {
  println '\n---- testChandasDefinitions ---- \n\n'
  
  chandasDefs.each { c ->
    def syllables = c.syllablize()
    def chandas = c.chandas()
    def weights = c.weights()

    println 'syllables = ' + syllables + ' = ' + syllables.size()
    println 'weights = ' + weights + ' = ' + weights.size()
    println 'chandas = ' + chandas + ' = ' + chandas.size()
    def map = [:]
    for (int i=0;i<syllables.size(); i++) {
      map.put(syllables[i], '(' + weights[i] + '-' + chandas[i] + ')')
    }
    println map
    println 'gana = ' + c.gana()
  }
}

void testSloka() {
  def s = slokas[3]
  
}

void runAllTests() {
  java.lang.reflect.Method[] m = this.getClass().getMethods().collect { it.name.startsWith('test') ? it : ''} - '' 
  m.each { this.invokeMethod(it.name, [] as Object[]) }
}


StopWatch.start()

//testVarnas()
//testSivaSutra()
//testItRules()
//testHalantyamRule()
//testTasyaLopahRule()
//testSamjnaSutras()
//testIfEndsWithSvara()
//testStringIterator()
//testSamyuktakshara()
//testSyllablizeWord()
//testSyllablizeSloka()
//testSlokaSize()
//testUnicode()
//testWeight()
//testChandas()
//testSloka()
testChandasDefinitions()


//runAllTests()

println StopWatch.stop()
