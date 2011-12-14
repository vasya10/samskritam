package ch8.tests

//import ch8.Bootstrap
//import ch8.MetaFunctions
//import ch8.config.Akshara

//evaluate(new File("ch8/Bootstrap.groovy"))
//evaluate(new File("ch8/MetaFunctions.groovy"))
//evaluate(new File("ch8/Bootstrap.groovy"))
//evaluate(new File("ch8/MetaFunctions.groovy"))


void testValues() {
  //def Script = new ConfigSlurper().parse(ch8.config.Script)
  def akshara = new ConfigSlurper().parse(ch8.config.Akshara)
  
  println akshara.svara
  println akshara.ku
  println akshara.allVarnas
  println akshara.nAsikA
  println akshara.sthAna
  println akshara.Prayatna.vivRta  
}

testValues()