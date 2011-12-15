package ch8

class Dhatu {
  def name
  int gana //1..10
  def ganaName //bhvAdi, divAdi
  def prefix //n.e, tu, du etc. : need to remember the prefix in order to apply kta and other pratyaya-s
  def vikarana //mostly based on gana itself

  Dhatu(String _name) { name = _name }
  
  def load(String _name) {
    def dhatupAtha = new ConfigSlurper().parse(ch8.config.DhatuPatha)
    def property = dhatupAtha.getProperty(name)
    Dhatu dhatu = new Dhatu(name)
    dhatu
  }  
  
  @Override
  String plus(Pratyaya p) {
    name + ' ' + p.toString()
  }
  
  @Override
  String toString() { name }
}
