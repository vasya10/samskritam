package ch8.tests

import ch8.ItRules
import ch8.Samjna
import ch8.SivaSutra
import ch8.schemes.NativeScriptScheme
import ch8.util.StopWatch
import ch8.util.UnicodeUtil

evaluate(new File("ch8/Bootstrap.groovy"))

def Vrutti = new ConfigSlurper().parse(ch8.config.Vrutti)

examples = ['suklAmbaraD.araM', 'kr.s.Na:', 'kArt.SN.yam', 'vASud.Evad.vAd.asAks.aramaN.t.rAN.t.argat.a', 'kuruks.E.t.rE.', 
            'Ad.esE.s.a', 'ad.rerAjaSut.At.majam','srEmad.ves.NvanGreN.es.Ta:','yamAt.ArAjaBAN.aSalagaM'
           ]

slokas = [ 'suklAmbaraD.aram ves.NuM sasevarNaM cat.urBujam | prasaN.N.avad.aN.am D.yAyE.t. SarvaveGN.Opa sAN.t.ayE. ||',
           'vyASam vaSes.Ta N.aPt.Aram sakt.E.: pO.t.ram akalmas.am | parASarAt.majam vaN.d.E. sukat.At.am t.apON.N.eD.em ||',
           'yA kuN.d.E.N.d.u t.us.Ara hAraD.avalA yA suBravaSt.rAvr.t.A | yA vENAvara d.aNdamaNdet.akarA yA svE.t.a pad.mASaN.A | yA brahmAcyut.a sankarA praBr.t.eBerd.E.vI: Sad.A vaN.d.et.A | SA mAM pAt.u SaraSvat.E Bagavat.E N.essE.s.a jAdyApahA ||',
           'rAmAya sAsvat.a SuveSt.r.t.a s.adguNAya SarvE.svarAya balavErya mahArNavAya | N.atvA lelangayes.u arNavam utpapAta Nes.pEdya t.am gerevaram pavaN.asya SUN.u:'
         ]

slokasHk = [ 'zuklAmbaradharam viSNum zazivarNam caturbhujam | prasannavadanam dhyAyet sarvavighnopa zAntaye ||']

chandasDefs = [ Vrutti.kanya, Vrutti.pankti, Vrutti.tanumadhya ]

void test_varnas() {
  println '\n---- test_varnas ---- \n\n'
  
  Samjna samjna = Samjna.instance
  println 'varnamala: ' + samjna.varnamala
  println 'svara: ' + samjna.svara
  println 'SaMSkr.t.am'.varnas()
  println slokas[0].varnas()
  
  assert samjna.ku == ['k','K','g','G','n']
  assert 'lyut'.varnas() == ['l', 'y', 'u', 't']
  assert 'kr.s.Na:'.varnas() == ['k', 'r.', 's.', 'N', 'a', ':']
  assert 'SaMSkr.t.am'.varnas() == ['S', 'a', 'M', 'S', 'k', 'r.', 't.', 'a', 'm']
}

void test_sivasutra() {
  println '\n---- test_sivasutra ---- \n\n'

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


void test_hrasvAkshara() {
  println '\n---- test_hrasvAkshara ---- \n\n'
  
  slokas[2].syllablize().each {
    println it + ' = ' + (it.hrasvakshara() ? 0 : 1)
  }  
}

void test_it_rules() {
  println '\n---- test_it_rules ---- \n\n'

  ItRules itRules = ItRules.instance

  println itRules.ajanunasika //prints all the ac anunasikas
  println itRules.cu
  println itRules.tu
  println itRules.allItVarnas
}


void test_halantyam_rule() {
  println '\n---- test_halantyam_rule ---- \n\n'
  
  //print the pratyahara-s after the halantyam rule applied
  ['kt.va', 'Gan.', 'kt.vat.', 'sap', 'lyu-t', 'saN', 'sat.r.'].each { println it + ' = ' + it.halantyam() }

  //Asserts  
  assert 'kt.va' == 'kt.va'.halantyam() //no rule applied, so same
  assert 'kt.va'  == 'kt.vat.'.halantyam() //t. removed due to halantyam() rule
  assert 'Ga' == 'Gan.'.halantyam() //n. removed due to halantyam() rule (though also applicable by lasaku rule)
}

void test_tasya_lopaH_rule() {
  println '\n---- test_tasya_lopaH_rule ---- \n\n'
  
  ['Gan.', 'kt.vat.', 'sap', 'lyu-t', 'saN', 'satr.'].each { println it + ' = ' + it.tasyaLopah() }

  //Asserts  
  assert 'a' == 'Gan.'.tasyaLopah()
  assert 't.va' == 'kt.vat.'.tasyaLopah()
  assert 'a' == 'sap'.tasyaLopah()
}

void test_samjna_sutras() {
  println '\n---- testSamjnaSutras ---- \n\n'

  Samjna samjna = Samjna.instance
  
  println samjna.allVarnas
  println 'vruddhi: ' + samjna.vruddhi
  println 'guna: ' + samjna.guna
  println 'all consonants: ' + samjna.hal

  //Asserts  
  assert ['A', 'I', 'O.'] == samjna.vruddhi
  assert ['a', 'E.', 'O'] == samjna.guna
  
}

void test_ends_with_svara() {
  println '\n---- test_ends_with_svara ---- \n\n'
  def example = ['a', 'E', 't.at.', 'kam', 'Barat.', 'nadE', 'svara']
  
  example.each { println it + ' = ' + it.endsWithSvara() }
  [true, true, false, false, false, true, true] == example.each { it.endsWithSvara() } 
}

void test_string_direct_iterator() {
  println '\n---- test_string_iterator ---- \n\n'
  
  examples.each { example -> example.each { print it + ',' } }
}

void test_samyuktAkshara() {
  println '\n---- test_samyktAkshara ---- \n\n'
  
  slokas.each { it.tokenize().each { it.syllablize().each { println it + ' = ' + it.samyuktakshara() } } }
}

void test_syllablize_word() {
  println '\n---- test_syllablize_word ---- \n\n'
  examples.each { println it + ' = ' + new NativeScriptScheme().syllablize(it) }
 
  //Asserts  
  assert ['su', 'klA', 'mba', 'ra', 'D.a', 'ram'] == new NativeScriptScheme().syllablize('suklAmbaraD.aram')
}

void performance_test_syllablize_sloka() {
  println '\n---- performance_test_syllablize_sloka ---- \n\n'
  
  slokas.each { println it.syllablize() }
}

void test_sloka_size() {
  println '\n---- test_sloka_size ---- \n\n'
  
  slokas.each { println it + ' = ' + it.syllableLength() }
  assert slokas.collect { it.syllableLength() } == [32, 32, 76, 56]
}

void test_native_to_unicode() {
  println '\n---- test_native_to_unicode ---- \n\n'
  
  slokas.each { 
    println it + '<br/>' + it.unicode() + '<br/><br/>'
  }
  //Asserts  
  assert 'suklAmbaraD.araM'.unicode() == '\u0936\u0941\u0915\u094D\u0932\u093E\u092E\u094D\u092C\u0930\u0927\u0930\u0902'
}

void test_sloka_chandas() {
  println '\n---- test_sloka_chandas ---- \n\n'
  
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
  println '\n---- test_chandas_defs ---- \n\n'
  
  chandasDefs.each { c ->
    def syllables = c.definition.syllablize()
    def metre = c.definition.metre() //list of 1s, 0s
    def gana = metre.join().metreAsGroup().gana()
    def originalGana = c.definition.gana().syllablize()
    
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
  println '\n---- test_verse_quarters ---- \n\n'
  
  slokas.each { sloka ->
    println sloka.syllablize()
    println sloka.gana()
    def (p1,p2,p3,p4) = [sloka.pada1(),sloka.pada2(),sloka.pada3(),sloka.pada4()]
    println "$p1\n$p2\n$p3\n$p4"
  }
}

void test_varnas_hk() {
  println '\n---- test_varnas_HK ---- \n\n'
  
  //DSL !!!
  use(ch8.schemes.Script) {
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

void test_vyanjana_sandhi() {
  Samjna samjna = Samjna.instance
  SivaSutra sivaSutra = SivaSutra.instance
  
  println samjna.eko_yaN_aci('et.e ape')
  println samjna.JalAm_jas_Jase('ap D.e')
  println samjna.eco_ayavaayaavaH('cE. anam')
  println samjna.eco_ayavaayaavaH('cI aka')
  println samjna.eco_ayavaayaavaH('lO. aka')
  println samjna.stoH_shcuna_schuH('vr.ks.aS sE.t.E.')
  println samjna.stoH_shcuna_schuH('vr.ks.aS ceN.Ot.e')
  println samjna.stoH_shcuna_schuH('agnecet. sE.t.E.')
  println samjna.stoH_shcuna_schuH('agnecet. ceN.Ot.e')
  
  assert samjna.eco_ayavaayaavaH('lO anam') == 'lavanam'
  assert samjna.eco_ayavaayaavaH('cI aka') == 'cAyaka'
  assert samjna.eco_ayavaayaavaH('lO. aka') == 'lAvaka'
  
  //assert samjna.JalAm_jas_Jase('ap D.e') == 'abD.e'
}

void test_pratyahara_dsl() {
  use(ch8.schemes.Script) {
    println 'cr'.pratyahara
    println SivaSutra.instance.cr
    assert 'cr'.pratyahara == SivaSutra.instance.cr 
  }
}

void test_sthaneantaratamah() {
  SivaSutra sivaSutra = SivaSutra.instance
  Samjna samjna = Samjna.instance
  
  sivaSutra.ek.each {
    println it + ' -> ' + samjna.sthaneAntaratamaH(it, sivaSutra.yN)
  }
  sivaSutra.Jl.each {
    println it + ' -> ' + samjna.sthaneAntaratamaH(it, sivaSutra.js)
  }

}

void runAllTests() {
  java.lang.reflect.Method[] m = this.getClass().getMethods().collect { it.name.startsWith('test') ? it : ''} - '' 
  m.each { this.invokeMethod(it.name, [] as Object[]) }
}

//StopWatch.start()

//test_varnas()
//test_sivasutra()
//test_hrasvAkshara()
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
test_vyanjana_sandhi()
//test_pratyahara_dsl()
//test_sthaneantaratamah()

//runAllTests()

//println StopWatch.stop()

