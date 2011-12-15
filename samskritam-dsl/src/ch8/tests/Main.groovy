package ch8.tests

import ch8.ItRules
import ch8.Samjna
import ch8.Sandhi
import ch8.SivaSutra
import ch8.schemes.NativeScriptScheme
import ch8.util.StopWatch

evaluate(new File("ch8/Bootstrap.groovy"))
evaluate(new File("ch8/MetaFunctions.groovy"))

akshara = loadConfig(ch8.config.Akshara)
vRtti = loadConfig(ch8.config.Vrutti)

sivaSutra = SivaSutra.instance
itRules = ItRules.instance
samjna = Samjna.instance
sandhi = Sandhi.instance

examples = ['suklAmbaraD.araM', 'kr.s.Na:', 'kArt.SN.yam', 'vASud.Evad.vAd.asAks.aramaN.t.rAN.t.argat.a', 'kuruks.E.t.rE.',
  'Ad.esE.s.a', 'ad.rerAjaSut.At.majam','srEmad.ves.NvanGreN.es.Ta:','yamAt.ArAjaBAN.aSalagaM', 'kascet. kAN.t.a verahaguNa SvAD.ekArAt.pramat.t.a:'
 ]

slokas = [ 'suklAmbaraD.aram ves.NuM sasevarNaM cat.urBujam | prasaN.N.avad.aN.am D.yAyE.t. SarvaveGN.Opa sAN.t.ayE. ||',
 'vyASam vaSes.Ta N.aPt.Aram sakt.E.: pO.t.ram akalmas.am | parASarAt.majam vaN.d.E. sukat.At.am t.apON.N.eD.em ||',
 'yA kuN.d.E.N.d.u t.us.Ara hAraD.avalA yA suBravaSt.rAvr.t.A | yA vENAvara d.aNdamaNdet.akarA yA svE.t.a pad.mASaN.A | yA brahmAcyut.a sankarA praBr.t.eBerd.E.vI: Sad.A vaN.d.et.A | SA mAM pAt.u SaraSvat.E Bagavat.E N.essE.s.a jAdyApahA ||',
 'rAmAya sAsvat.a SuveSt.r.t.a s.adguNAya SarvE.svarAya balavErya mahArNavAya | N.atvA lelangayes.u arNavam utpapAta Nes.pEdya t.am gerevaram pavaN.asya SUN.u:'
]

slokasHk = [ 'zuklAmbaradharam viSNum zazivarNam caturbhujam | prasannavadanam dhyAyet sarvavighnopa zAntaye ||']

chandasDefs = [ vRtti.kanya, vRtti.pankti, vRtti.tanumadhya ]


void test_akshara() {
  println akshara.svara
  println akshara.ku
  println akshara.allVarnas
  println akshara.nAsikA
  println akshara.sthAna
  println akshara.Prayatna.vivRta  
}

void test_varnas() {
  println 'varnamala: ' + akshara.varnamala
  println 'svara: ' + akshara.svara
  println 'SaMSkr.t.am'.varnas()
  println 'nAsika: ' + akshara.nAsikA
  println slokas[0].varnas()
  
  assert akshara.ku == ['k','K','g','G','n']
  assert 'lyut'.varnas() == ['l', 'y', 'u', 't']
  assert 'kr.s.Na:'.varnas() == ['k', 'r.', 's.', 'N', 'a', ':']
  assert 'SaMSkr.t.am'.varnas() == ['S', 'a', 'M', 'S', 'k', 'r.', 't.', 'a', 'm']
}

void test_sivasutra() {
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


void test_hrasva_akshara() {
  slokas[2].syllablize().each {
    println it + ' = ' + (it.hrasvakshara() ? 0 : 1)
  }  
}

void test_it_rules() {
  println itRules.ajanunasika //prints all the ac anunasikas
  println itRules.cu
  println itRules.tu
  println itRules.allItVarnas
}


void test_halantyam_rule() {
  //print the pratyahara-s after the halantyam rule applied
  ['kt.va', 'Gan.', 'kt.vat.', 'sap', 'lyu-t', 'saN', 'sat.r.'].each { println it + ' = ' + it.halantyam() }

  //Asserts  
  assert 'kt.va' == 'kt.va'.halantyam() //no rule applied, so same
  assert 'kt.va'  == 'kt.vat.'.halantyam() //t. removed due to halantyam() rule
  assert 'Ga' == 'Gan.'.halantyam() //n. removed due to halantyam() rule (though also applicable by lasaku rule)
}

void test_tasya_lopaH_rule() {
  ['Gan.', 'kt.vat.', 'sap', 'lyu-t', 'saN', 'sa-t.r.N.'].each { println it + ' = ' + it.varnas() + ' = ' + it.tasyaLopah() }

  //Asserts  
  assert 'a' == 'Gan.'.tasyaLopah()
  assert 't.va' == 'kt.vat.'.tasyaLopah()
  assert 'a' == 'sap'.tasyaLopah()
}

void test_samjna_sutras() {
  println 'vruddhi: ' + samjna.vRddhi
  println 'guna: ' + samjna.guNa
  println 'all consonants: ' + samjna.hal
  println 'upadhA of yak: ' + samjna.upadhA('yak')
  
  //Asserts  
  assert ['A', 'I', 'O.'] == samjna.vRddhi
  assert ['a', 'E.', 'O'] == samjna.guNa
  assert ['E', 'U', 'E.'] == samjna.pragRhyam
  assert 'u' == samjna.anga('t.aN.up')
  assert 'k' == samjna.upadhA('yak')
}

void test_ends_with_svara() {
  def example = ['a', 'E', 't.at.', 'kam', 'Barat.', 'nadE', 'svara']
  
  example.each { println it + ' = ' + it.endsWithSvara() }
  [true, true, false, false, false, true, true] == example.each { it.endsWithSvara() } 
}

void test_string_direct_iterator() {
  examples.each { example -> example.each { print it + ',' } }
}

void test_samyukta_akshara() {
  slokas.each { it.tokenize().each { it.syllablize().each { println it + ' = ' + it.samyuktakshara() } } }
}

void test_syllablize_word() {
  [slokas[0]].each { println it + ' = ' + new NativeScriptScheme().syllablize(it) }
 
  //Asserts  
  assert ['su', 'klA', 'mba', 'ra', 'D.a', 'ram'] == new NativeScriptScheme().syllablize('suklAmbaraD.aram')
}

void performance_test_syllablize_sloka() {
  slokas.each { println it.syllablize() }
}

void test_sloka_size() {
  slokas.each { println it + ' = ' + it.syllablize() + ' = ' + it.syllableLength() }
  assert slokas.collect { it.syllableLength() } == [32, 32, 76, 56]
}

void test_native_to_unicode() {
  slokas.each { 
    println it + '<br/>' + it.unicode() + '<br/><br/>'
  }
  
  //Asserts  
  assert 'suklAmbaraD.araM'.unicode() == '\u0936\u0941\u0915\u094D\u0932\u093E\u092E\u094D\u092C\u0930\u0927\u0930\u0902'
}

void test_sloka_chandas() {
  slokas.each { verse->
    def syllables = verse.syllablize()
    def metre = verse.metre()
    
    println '\n\nsyllables = ' + syllables + ' = ' + syllables.size()
    println 'metre = ' + metre + ' = ' + metre.size()
    
    def map = [:]
    0.step(syllables.size(),1) { map.put(syllables[it], metre[it]) }
    println 'metre of each syllable = ' + map

    println 'gana = ' + verse.metreAsString().metreAsGroup()
    println 'gana = ' + verse.gana()
    
    //isn't this cool or what?
    assert  'gmO. cet. kaN.yA'.gana() == ['ma', 'ga']
  }
}

void test_chandas_defs() {
  chandasDefs.each { c ->
    List syllables = c.definition.syllablize()
    List metre = c.definition.metre() //list of 1s, 0s
    List gana = metre.join().metreAsGroup().gana()
    List originalGana = c.definition.gana() //.syllablize()
    
    println "\n\n verifying ${c.name}"
    println "syllables = $syllables = (${syllables.size()})"
    println "metre = $metre = (${metre.size()})"
    
    def map = [:]
    0.step(syllables.size(),1) { map.put(syllables[it], metre[it]) }
    println "metre of each syllable = $map"

    println "chandas determined gana = $gana"
    println "chandas original gana   = $originalGana"
    
    //isn't this absoluately cool or what?
    assert  originalGana == gana
  }
}

void test_verse_quarters() {
  slokas.each { sloka ->
    println sloka.syllablize()
    println sloka.gana()
    def (p1,p2,p3,p4) = [sloka.pada1(),sloka.pada2(),sloka.pada3(),sloka.pada4()]
    println "$p1\n$p2\n$p3\n$p4"
  }
}

void test_varnas_hk() {
  //DSL !!!
  use(ch8.schemes.Dsl) {
    println 'saMskRtam'.hk
    println slokasHk[0].hk
    assert 'lyuT'.hk == ['l', 'y', 'u', 'T']
    assert 'saMskRtam'.hk == ['s', 'a', 'M', 's', 'k', 'R', 't', 'a', 'm']
    assert 'madhvAcAryaH'.hk == ['m', 'a', 'dh', 'v', 'A', 'c', 'A', 'r', 'y', 'a', 'H']
    assert 'rAmAnujaH'.hk == ['r', 'A', 'm', 'A', 'n', 'u', 'j', 'a', 'H']
  }
}

void test_find_anga() {
  def text = ['Ad.esE.s.a', 'BU', 'sat.r.c']
  def anga = { a -> a.varnas().reverse().find { it.svara() } }
  text.each { println anga(it) }
}

void test_sthAne_antaratamaH() {
  sivaSutra.ek.each {
    println it + ' -> ' + sandhi.sthAne_antaratamaH(it, sivaSutra.yN)
  }
  sivaSutra.Jl.each {
    println it + ' -> ' + sandhi.sthAne_antaratamaH(it, sivaSutra.js)
  }
}

void test_tulya_aasya_prayatnam() {
  ['a', 'k', 'p', 'y', 'e', 'v'].each { println it + ' = ' + samjna.aasya(it) }
  [['a', 'A'], ['e','y'], ['A', 'k']].each { println it + " = " + samjna.savarNam(it) }
}

void test_akaH_savarNe_dIrghaH() {
  println sandhi.apply('rAma ayaNam', sandhi.akaH_savarNe_dIrghaH)
  println sandhi.apply('SEtA AgacCat.e', sandhi.akaH_savarNe_dIrghaH)
  println sandhi.apply('et.e ed.AnEm', sandhi.akaH_savarNe_dIrghaH)
  println sandhi.apply('vaD.u upacAra:', sandhi.akaH_savarNe_dIrghaH)
  
  assert sandhi.apply('rAma ayaNam', sandhi.akaH_savarNe_dIrghaH) == 'rAmAyaNam'
  assert sandhi.apply('vAk aBed.a:', sandhi.akaH_savarNe_dIrghaH) == 'vAk aBed.a:'
  
}

void test_aad_guNaH() {
  println sandhi.apply('BU a', sandhi.aad_guNaH)
  
  assert sandhi.apply('BU a', sandhi.aad_guNaH) == 'BO a'
}

void test_auto_sandhi() {
  ['rAma ayaNam', 'SEtA AgacCat.e', 'vAk aBed.a:', 'BU a', 'et.e ape'].each {
    println it + ' = ' + sandhi.auto(it)
  }
}

void test_pratyahara_dsl() {
  use(ch8.schemes.Dsl) {
    println 'sap'.pratyaya
    //assert 'cr'.pratyahara == sivaSutra.cr 
    
    println 'BU'.dhatu + 'sap'.pratyaya + 't.en'.pratyaya
  }
}

void test_dhatu_padam() {
  use (ch8.schemes.Dsl) {
    def a1 = 'BU'.dhatu + 'sap'.pratyaya + 't.en'.pratyaya
    def a2 = sandhi.apply(a1, sandhi.aad_guNaH)
    def padam = sandhi.apply(a2, sandhi.eco_ayavaayaavaH)
    println 'BU + sap + te.n' + ' = ' + padam
  }
}

/** ***********************************************************************************
 *  ************  Test Calls ************** ************** ************** **************
 *  ***********************************************************************************
 */

void runAllTests() {
  java.lang.reflect.Method[] m = this.getClass().getMethods().collect { it.name.startsWith('test_') ? it : ''} - '' 
  m.each { this.invokeMethod(it.name, [] as Object[]) }
}

@Override
public Object invokeMethod(String name, Object args) {
  System.out.println ("\n----- $name ------ \n")
  return super.invokeMethod(name, args)
}

StopWatch.start()

//runAllTests()

//test_akshara()
//test_varnas()
//test_sivasutra()
//test_hrasva_akshara()
//test_it_rules()
//test_halantyam_rule()
//test_tasya_lopaH_rule()
//test_samjna_sutras()
//test_ends_with_svara()
//test_string_direct_iterator()
//test_samyuktAkshara()
//test_syllablize_word()
//performance_test_syllablize_sloka()
//test_sloka_size()
//test_native_to_unicode()
//test_sloka_chandas()
//test_chandas_defs()
//test_verse_quarters()
//test_varnas_hk()
//test_find_anga()
//test_sthAne_antaratamaH()
//test_tulya_aasya_prayatnam()
//test_akaH_savarNe_dIrghaH()
//test_purvarupa_sandhi()
//test_pratyahara_dsl()
//test_dhatu_padam()
test_auto_sandhi()

println StopWatch.stop()
