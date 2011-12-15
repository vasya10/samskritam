package ch8

import groovy.lang.Closure

@Singleton
class Sandhi {
  
  def akshara = loadConfig(ch8.config.Akshara) 
  SivaSutra sivaSutra = SivaSutra.instance
  Samjna samjna = Samjna.instance
  
  Closure purvaRupaSandhi = {sthAna, adesha, condition, purva, para ->
    def original_purva = purva
    if (condition(para)) {
      purva = sthAna.substitute(adesha, purva)
      println "sthana: $sthAna, adesha: $adesha, original: $original_purva, new: $purva"
    }
    [purva, para]
  }

  //Find the aadeza-varNa within the sthANa of the given varNa
  //#(sthAne antaratamaH |)
  //TODO Currently not being used, need to understand this better; esp kaNTha + OSTha should return varna from kaNThOSTham
  Closure sthAne_antaratamaH = { varNa, aadeza ->
    for (def s : akshara.sthAna) {
      if (varNa in s) return s.intersect(aadeza)
    }
  }
  
  /**
   * if two similar svarA-s, lengthen the svara
   */
  Closure akaH_savarNe_dIrghaH = { purva, para -> 
    def result = []
    if (samjna.savarNam(purva, para)) {
      result = [akshara.hrasva_dIrgha_map[purva]?:purva,'']
    }
    result
  }

  Closure aad_guNaH = { x, y ->
    def guna = ['a','e','u'].substitute(samjna.guNaH, x.toLowerCase())
    [guna?:x, null]
  }
  
  def aci = { it.svara() } 
  def jhazi = { it in sivaSutra.Js }
  def zcuna = { it in ['s'] + akshara.cu}
  def STunA = { it in ['s.'] + akshara.thu }

  //#A(6.1.78)
  def iko_yaN_aci = purvaRupaSandhi.curry(sivaSutra.ek, sivaSutra.yN, aci)
  //#A()
  def jhalAm_jaz_jhazi = purvaRupaSandhi.curry(sivaSutra.Jl, sivaSutra.js, jhazi)
  //#A(6.1.79)
  def eco_ayavaayaavaH = purvaRupaSandhi.curry(sivaSutra.'E.c', ['ay', 'av', 'Ay', 'Av'], aci)
  //#A()
  def stoH_zcuna_zcuH = purvaRupaSandhi.curry(['S']+ akshara.thu, ['s']+ akshara.cu , zcuna)
  //#A()
  def stoH_STunA_STuH = purvaRupaSandhi.curry(['S'] + akshara.thu, ['s.'] + akshara.thu, STunA)

  //generic method - application of any sandhi rule
  def apply = { word, vidhiSutra ->
    if (!word) return '' 
    if (!vidhiSutra) return 'invalid rule'
    def words = word.tokenize()
    if (words.size()<2) return word
    def (purva, para) = [words[0].varnas().last(), words[1].varnas().first()]
    (purva, para) = vidhiSutra.call(purva, para)
    //check specifically for null, because '' means delete and empty string is false in Groovy Truth
    if (purva != null) words[0] = words[0].replaceLast(purva)
    if (para != null) words[1] = words[1].replaceFirst(para)
    //TODO Kludgy, but works - if a sandhi happened, join the words, if not keep them separate
    (purva != null && para != null) ? words[0] + words[1] : words[0] + ' ' + words[1]  
  }
  
  /**
   * @given split a word and auto apply sandhi
   */
  def auto = { text ->
    def words = text.tokenize()
    def (purva, para) = [words[0].varnas().last(), words[1].varnas().first()]
    
    def sandhiRule
    if (samjna.savarNam(purva, para)) {
      sandhiRule = akaH_savarNe_dIrghaH
      text = apply(text, sandhiRule)
    } else if (aci(para)){
      if (purva in sivaSutra.ek) { sandhiRule = iko_yaN_aci; text = apply(text, sandhiRule) } 
      if (purva.svara()) { sandhiRule = aad_guNaH; text = apply(text, sandhiRule) } 
      if (purva in sivaSutra.'E.c') { sandhiRule =  eco_ayavaayaavaH; text = apply(text, sandhiRule) } 
    }
    
    text
  }
  
}
