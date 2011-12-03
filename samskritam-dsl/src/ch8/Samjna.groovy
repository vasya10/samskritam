package ch8

/**
 * Definitions of some samjna-s
 * @author vsrinivasan
 *
 */
@Singleton
class Samjna {

  boolean lazy = false
  
  SivaSutra sivaSutra = SivaSutra.instance

  
  def svara = 'aAeEuUr.R.l.E.IOO.'.varnas()
  def anusvara = ['M']
  def visarga = [':']
  def candrabindu = ['M.']
  def avagraha = ['a.']
  def avasanam = [' ', '|', '||'] //#() viramo avasaanam
  
  def ku = 'kKgGn'.varnas()         //allVarnas.range('k','n')
  def cu = 'cCjJn.'.varnas()        //allVarnas.range('c','n.)
  def tu = 'tTdDN'.varnas()         //allVarnas.range('t','N')
  def thu = 't.T.d.D.N.'.varnas()   //allVarnas.range('t.','N.')
  def pu = 'pPbBm'.varnas()         //allVarnas.range('p','m')
  def antastha = 'yrlv'.varnas()    //allVarnas.range('y','v')
  def ushmana = 'ss.Sh'.varnas()    //allVarnas.range('s','h')
  List allVarnas = svara + anusvara + visarga + ku + cu + tu + thu + pu + antastha + ushmana + avasanam

  //def valid = { list -> allVarnas.containsAll(list) } //validate a word.varnas()
  
  //a-ku-ha-visarjaniyaanaam kantaH
  def kanta = ['a', 'A'] + ku + visarga
  //i-cu-ya-shaanaam taalu
  def taalu = ['i', 'I'] + cu + ['y', 's']
  //r.-tu-ra-shaanaam murdha
  def murdha = ['r.', 'R.'] + tu + ['r', 's.']
  //l.-tu-la-saanaam danta
  def danta = ['l.'] + thu + ['S']
  
  def hal = { sivaSutra.hl.unique() } //remove the duplicate h
  
  def dIrgha = 'AEUR.E.IOO.M:'.varnas()
  def hrsva = svara - dIrgha
  def vyanjana = allVarnas - svara

  //#(1.1.1) vruddhiraadiac
  def vruddhi = { sivaSutra.'At.' + sivaSutra.Ic }
  
  //#(1.1.2) adengunaH
  def guna = { sivaSutra.'at.' + sivaSutra.'E.n' }
  
  //#(1.1.10) idudedoH pragruhyam
  def pragruhyam = { sivaSutra.'Et.' + sivaSutra.'Ut.' + sivaSutra.'E.t.' }
  
  //def anga
  //def upadhaa
  
  
  
}
