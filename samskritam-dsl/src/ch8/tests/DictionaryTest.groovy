package ch8.tests

import ch8.Dictionary;

def d = new ConfigSlurper().parse(new File("ch8/Dictionary.groovy").toURL())
println d.aH
println d.AdiseshaH
println d.suklAmbaradharam
