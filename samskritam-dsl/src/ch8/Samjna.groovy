package ch8

/**
 * Definitions of samjna-s, partially uses the Script configuration file
 * 75% close to being independent of script
 * @author vsrinivasan
 *
 */
@Singleton
class Samjna {

  boolean lazy = false
  
  SivaSutra sivaSutra = SivaSutra.instance

//  def Script = new ConfigSlurper().parse(ch8.config.Script)
//  def nativeScript = Script.NativeScript
//  def varnamala = nativeScript.varnamala
//  
//  def svara = varnamala.range('a', ':') 
//  def anusvara = [nativeScript.anusvara]
//  def visarga = [nativeScript.visarga]
//  def candrabindu = [nativeScript.candrabindu]
//  def avagraha = [nativeScript.avagraha]
//  def taparaH = nativeScript.t
//  def anunAsikA = nativeScript.anunasika
//  def avasAnam = varnamala.range(' ', '||')
//  
//  def ku = varnamala.range('k','n')
//  def cu = varnamala.range('c','n.')
//  def tu = varnamala.range('t','N')
//  def thu = varnamala.range('t.','N.')
//  def pu = varnamala.range('p','m')
//  def antastha = varnamala.range('y','v')
//  def USmAna = varnamala.range('s','h')
//  List allVarnas = varnamala.range('a','OM')
//
//  //a-ku-ha-visarjaniyaanaam kantaH
//  //TODO include all 18 types of a, using the spread operator
//  def kaNTha = ['a', 'A'] + ku + ['h'] + visarga
//  //i-cu-ya-Saanaam taalu
//  def tAlu = ['e', 'E'] + cu + ['y', 's']
//  //r.-tu-ra-Saanaam murdha
//  def mUrdha = ['r.', 'R.'] + tu + ['r', 's.']
//  //l.-tu-la-saanaam danta
//  def danta = ['l.'] + thu + ['l', 'S']
//  
//  //TODO upu-upadhmaniyaanaam oStaH
//  def upadhmAna = ''
//  def oSTha = ['u', 'U'] + pu + ['v'] //+ upadhmaniya
//  //nasika
//  def nAsikA = [cu, pu, ku, tu, thu]*.last()
//  
//  def kaNTha_tAlu = ['E.','I']
//  def kaNTha_OSTham = ['O','O.']
//  
//  //dantoStam
//  def dantoSTham = ['v']
//  //TODO jihvAmulIyasya jihvAmulam 
//  def jihvAmula = ''
//  
//  def sthAna = [kaNTha, tAlu, mUrdha, danta, oSTha, nAsikA, dantoSTham, jihvAmula]
//  
//  def hal = sivaSutra.hl.unique() //remove the duplicate h
//  
//  def dIrgha = 'AEUR.E.IOO.M:'.varnas()
//  def hrasva = svara - dIrgha
//  def vyanjana = allVarnas - svara

  def hal = sivaSutra.hl.unique()
  
  //#(1.1.1) vruddhiraadiac
  def vRddhi = sivaSutra.'At.' + sivaSutra.Ic
  
  //#(1.1.2) adengunaH
  def guNa = sivaSutra.'at.' + sivaSutra.'E.n'
  
  //#(1.1.10) iduded pragruhyam
  def pragRhyam = sivaSutra.'Et.' + sivaSutra.'Ut.' + sivaSutra.'E.t.'

  //last vowel
  def anga = { x -> x.varnas().reverse().find { it.svara() } }
  
  //#() alontyaat pUrvaH upadhA
  def upadhA = { x -> def varnas = x.varnas(); varnas[varnas.size()-1] }
  
}