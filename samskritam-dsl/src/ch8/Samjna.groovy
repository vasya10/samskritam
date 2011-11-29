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

  
  def svara = "aAeEuUr.R.l.E.IOO.".varnas()
  def anusvara = ['M']
  def visarga = [':']
  
  def ku = 'kKgGn'.varnas()         //allVarnas.range('k','n')
  def cu = 'cCjJn.'.varnas()        //allVarnas.range('c','n.)
  def tu = 'tTdDN'.varnas()         //allVarnas.range('t','N')
  def thu = 't.T.d.D.N.'.varnas()   //allVarnas.range('t.','N.')
  def pu = 'pPbBm'.varnas()         //allVarnas[allVarnas.indexOf('p')..allVarnas.indexOf('m')]
  def antastha = 'yrlv'.varnas()    //allVarnas[allVarnas.indexOf('y')..allVarnas.indexOf('v')]
  def ushmana = 'ss.Sh'.varnas()    //allVarnas[allVarnas.indexOf('s')..allVarnas.indexOf('h')]
  
  List allVarnas = svara + anusvara + visarga + ku + cu + tu + thu + pu + antastha + ushmana

  //def valid = { list -> allVarnas.containsAll(list) } //validate a word.varnas()
  
    //a-ku-ha-visarjaniyaanaam kantaH
  def kanta = ['a', 'A'] + ku + visarga
  //i-cu-ya-shaanaam taalu
  def taalu = ['i', 'I'] + cu + ['y', 's']
  
  def hal = { sivaSutra.hl.unique() } //remove the duplicate h
  
  def dIrgha = 'AEUR.E.IOO.M:'.varnas()
  def hrsva = svara - dIrgha
  def vyanjana = allVarnas - svara

  def vruddhi = { sivaSutra.'At.' + sivaSutra.ic }
  def guna = { sivaSutra.'at.' + sivaSutra.'E.n' }
  
}
