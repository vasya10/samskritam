package ch8

/**
 * Samjna Prakaranam - Definitions
 * 
 * variables use HK notation as much as possible
 * 
 * @author vsrinivasan
 */
@Singleton
class Samjna {

  boolean lazy = false
  
  def akshara = loadConfig(ch8.config.Akshara)
  SivaSutra sivaSutra = SivaSutra.instance

  //halantyam #SK(1.1) 
  def hal = sivaSutra.hl.unique()
  
  //#A(1.1.1) vruddhiraadiac
  def vRddhi = sivaSutra.'At.' + sivaSutra.Ic
  
  //#A(1.1.2) adengunaH
  def guNaH = sivaSutra.'at.' + sivaSutra.'E.n'
  
  def aasya = { varna -> akshara.sthAna.find { varna in it } }
  
  def prayatna = { varna -> akshara.prayatna.find { varna in it } } 
  
  //TODO implement prayatna equality
  //#A(tulya aasya prayatnam savarNam |)
  //#A(na ac halau |)
  //#SK(ako aki savarNam)
  def savarNam = { varna1, varna2 -> aasya(varna1) == aasya(varna2) && (varna1.svara() && varna2.svara()) }
  
  //#A(1.1.10) iduded pragruhyam
  def pragRhyam = sivaSutra.'Et.' + sivaSutra.'Ut.' + sivaSutra.'E.t.'

  //#A() angasya - last vowel
  def anga = { x -> x.varnas().reverse().find { it.svara() } }
  
  //#A() alontyaat pUrvaH upadhA
  def upadhA = { x -> def varnas = x.varnas(); varnas[varnas.size()-1] }
  
}