package ch8

@Singleton
class ItRules {

  //#(1.3.2) upadeshe ajanunaasika iT, anunAsika-s are denoted by a "-" at the end,
  //  may be M would be a better option?
  Samjna samjna = Samjna.instance
  def ajanunasika = samjna.svara.collect { it + "-" }

  //cutU
  def cu = samjna.cu
  def tu = samjna.tu

  //s.asca, denoting as "sha" for convenience
  def sha = ['s.']

  //lashaku (ataddhite)
  def lashaku = 'ls'.varnas() + samjna.ku

  //some more to be defined
  def adirnitudavaH = ["n.i",'tu', 'du']
  
  //#(1.3.3) halantyam - check if the last char is hal
  SivaSutra sivaSutra = SivaSutra.instance
  boolean hasHalantyam(String pratyaya) { pratyaya.varnas().last() in sivaSutra.hl }

  //allItMarkers except hal, which is applicable only to last letter
  def allItVarnas = ajanunasika + cu + tu + lashaku

  boolean anunasika(String varna) { varna.endsWith('-') }

  boolean itVarna(String varna) { varna in allItVarnas }

  String tasyaLopah(String pratyaya) { (pratyaya.halantyam().varnas() - allItVarnas).join() }
}
