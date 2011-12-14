package ch8

class Dhatu {
  int gana //1..10
  def ganaName //bhvAdi, divAdi
  def prefix //n.e, tu, du etc. : need to remember the prefix in order to apply kta and other pratyaya-s
  def vikarana //mostly based on gana itself

  def load(String d) {
    def dhatupAtha = new ConfigSlurper().parse(ch8.config.DhatuPatha)
    dhatupAtha.getProperty(d)
  }  
}
