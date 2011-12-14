package ch8

@Singleton
class ItRules {

  def akshara = new ConfigSlurper().parse(ch8.config.Akshara)

  def ajanunasika = akshara.svara.collect { it + "-" }

  //cutU
  def cu = akshara.cu
  def tu = akshara.tu

  //s.asca, denoting as "sha" for convenience
  def sha = ['s.']

  //lashaku (ataddhite)
  def lashaku = 'ls'.varnas() + akshara.ku

  //adirnitudavaH
  def adirnitudavaH = ['n.e','tu', 'du']
  
  //#(1.3.3) halantyam - check if the last char is hal
  SivaSutra sivaSutra = SivaSutra.instance
  boolean hasHalantyam(String pratyaya) { pratyaya.varnas().last() in sivaSutra.hl }

  //allItMarkers except hal, which is applicable only to last letter
  def allItVarnas = ajanunasika + cu + tu + lashaku

  boolean anunasika(String varna) { varna.endsWith('-') }

  boolean itVarna(String varna) { varna in allItVarnas }

  String tasya_lopaH(String pratyaya) { (pratyaya.halantyam().varnas() - allItVarnas).join() }
  
  boolean isPit(String pratyaya) { 'p' in pratyaya.varnas() }
  boolean isKit(String pratyaya) { 'k' in pratyaya.varnas() }
  boolean isNit(String pratyaya) { 'N' in pratyaya.varnas() }

}
