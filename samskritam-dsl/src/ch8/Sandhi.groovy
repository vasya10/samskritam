package ch8

import groovy.lang.Closure

@Singleton
class Sandhi {
  
  def akshara = loadConfig(ch8.config.Akshara) 
  SivaSutra sivaSutra = SivaSutra.instance
  Samjna samjna = Samjna.instance
  
  //sandhi rules
  Closure purvaRupaSandhi = {sthAna, adesha, condition, words ->
    def (purva, para) = words.tokenize()
    if (condition(para)) {
      List purvaVarnas = purva.varnas()
      def k = sthAna.substitute(adesha, purvaVarnas.last())
      List result = purvaVarnas.clone()
      result.replaceLast(k)
      println "purva: $purvaVarnas, sthana: $sthAna, adesha: $adesha, substituted by $k, purva: $result"
      result.join() + para
    } else {
      words
    }
  }

  //Given any value in sthana list, return a value from adesa list, which is found in the varna-category lists
  //#()
  Closure sthAne_antaratamaH = { x, aadeza ->
    for (def s : akshara.sthAna) {
      if (x in s) return s.intersect(aadeza)
    }
  }

  //when applying sandhi between two words, the next word must be supplied here
  def aci = { word -> word.varnas()[0].svara() }
  def jhazi = { word -> word.varnas()[0] in sivaSutra.Js }
  def zcuna = { word -> word.varnas()[0] in ['s'] + akshara.cu}
  def STunA = { word -> word.varnas()[0] in ['s.'] + akshara.thu }

  //#(6.1.78)
  def iko_yaN_aci = purvaRupaSandhi.curry(sivaSutra.ek, sivaSutra.yN, aci)
  def jhalAm_jaz_jhazi = purvaRupaSandhi.curry(sivaSutra.Jl, sivaSutra.js, jhazi)
  //#(6.1.79)
  def eco_ayavaayaavaH = purvaRupaSandhi.curry(sivaSutra.'E.c', ['ay', 'av', 'Ay', 'Av'], aci)
  def stoH_zcuna_zcuH = purvaRupaSandhi.curry(['S']+ akshara.thu, ['s']+ akshara.cu , zcuna)
  def stoH_STunA_STuH = purvaRupaSandhi.curry(['S'] + akshara.thu, ['s.'] + akshara.thu, STunA)

  def aat_guNaH = {}
  
  def aasya = { varna1, varna2 ->  
    for (def p : akshara.sthAna)  {
      //aasya
      if (varna1 in p && varna2 in p) return true
    }
    false
  }
  
  def prayatna = { varna1, varana2 ->
    
  }

  def savarNa = { v1, v2 -> aasya(v1) == aasya(v2) && prayatna(v1) == prayatna(v2) }
    
  def akaH_savarNe_dIrghaH = { words -> 
    if (savarNa(v1, v2)) {
      //modify
    }  
  }
}
