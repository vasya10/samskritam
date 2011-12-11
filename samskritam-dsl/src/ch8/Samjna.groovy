package ch8

/**
 * Definitions of samjna-s, partially uses the Definitions configuration file
 * 75% close to being independent of script
 * @author vsrinivasan
 *
 */
@Singleton
class Samjna {

  boolean lazy = false
  
  SivaSutra sivaSutra = SivaSutra.instance

  def Definitions = new ConfigSlurper().parse(ch8.config.Definitions)
  def script = Definitions.NativeScript
  def varnamala = script.varnamala
  
  def svara = varnamala.range('a', ':') 
  def anusvara = [script.anusvara]
  def visarga = [script.visarga]
  def candrabindu = [script.candrabindu]
  def avagraha = [script.avagraha]
  def taparaH = script.t
  def anunasika = script.anunasika
  def avasanam = varnamala.range(' ', '||') //[' ', '|', '||'] //#() viramo avasaanam
  
  def ku = varnamala.range('k', 'n')
  def cu = varnamala.range('c','n.')
  def tu = varnamala.range('t','N')
  def thu = varnamala.range('t.','N.')
  def pu = varnamala.range('p','m')
  def antastha = varnamala.range('y','v')
  def ushmana = varnamala.range('s','h')
  List allVarnas = varnamala.range('a','OM')

  //a-ku-ha-visarjaniyaanaam kantaH
  //TODO include all 18 types of a, using the spread operator
  def kanta = ['a', 'A'] + ku + ['h'] + visarga
  //i-cu-ya-shaanaam taalu
  def taalu = ['e', 'E'] + cu + ['y', 's']
  //r.-tu-ra-shaanaam murdha
  def murdha = ['r.', 'R.'] + tu + ['r', 's.']
  //l.-tu-la-saanaam danta
  def danta = ['l.'] + thu + ['l', 'S']
  //upu-upadhmaniyaanaam oshtaH
  def upadhmana = ''
  def oshta = ['u', 'U'] + pu + ['v'] //+ upadhmaniya
  //nasika
  def nasika = [cu, pu, ku, tu, thu].collect { it.last() }
  //dantoshtam
  def dantoshtam = ['v']
  //jihvamula 
  def jihvamula = ''
  
  def hal = sivaSutra.hl.unique() //remove the duplicate h
  
  def dIrgha = 'AEUR.E.IOO.M:'.varnas()
  def hrasva = svara - dIrgha
  def vyanjana = allVarnas - svara

  //#(1.1.1) vruddhiraadiac
  def vruddhi = sivaSutra.'At.' + sivaSutra.Ic
  
  //#(1.1.2) adengunaH
  def guna = sivaSutra.'at.' + sivaSutra.'E.n'
  
  //#(1.1.10) iduded pragruhyam
  def pragruhyam = sivaSutra.'Et.' + sivaSutra.'Ut.' + sivaSutra.'E.t.'

  //last vowel
  def anga = { x -> x.varnas().reverse().find { it.svara() } }
  
  //#() alontyaat pUrvaH upadhA
  def upadhaa = { x -> def varnas = x.varnas(); varnas[varnas.size()-1] }
  
  //sandhi rules
  Closure vidhiSutra = {sthana, adesha, condition, words ->
    def (purva, para) = words.tokenize()
    if (condition(para)) {
      List purvaVarnas = purva.varnas()
      println "purvaVarnas: $purvaVarnas, sthana: $sthana, adesha: $adesha" 
      def k = sthana.substitute(adesha, purvaVarnas.last())
      println "$purva is substituted by $k ... to get "
      List result = purvaVarnas.clone()
      result.replaceLast(k)
      result.join() + para
    } else {
      words
    }
  }
  
  //Given any value in sthana list, return a value from adesa list, which is found in the varna-category lists
  Closure sthaneAntaratamaH = { x, adesha ->
    if (![x].disjoint(kanta)) return kanta.intersect(adesha)
    if (![x].disjoint(taalu)) return taalu.intersect(adesha)
    if (![x].disjoint(murdha)) return murdha.intersect(adesha)
    if (![x].disjoint(danta)) return danta.intersect(adesha)
    if (![x].disjoint(oshta)) return oshta.intersect(adesha)
    if (![x].disjoint(dantoshtam)) return dantoshtam.intersect(adesha)
  }
  
  //when applying sandhi between two words, the next word must be supplied here
  def aci = { word -> word.varnas()[0].svara() }
  def Jsi = { word -> word.varnas()[0] in sivaSutra.Js }
  
  def eko_yaN_aci = vidhiSutra.curry(sivaSutra.ek, sivaSutra.yN, aci)
  def JalAm_jas_Jase = vidhiSutra.curry(sivaSutra.Jl, sivaSutra.js, Jsi)
  
}