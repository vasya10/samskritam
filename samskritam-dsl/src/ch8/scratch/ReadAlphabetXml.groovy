package ch8.scratch

import groovy.xml.*

def beans = new XmlSlurper().parse(new File("c:/smartspace/alphabet.xml"))
def beanMap = [:]
def beanList = []
//def k = beans.bean.collect { bean -> bean.property.find { it.@name == 'letter' }.@value.collect{ "'" + it.text() + "'" }  }; //.property.'@value'.collect { it.text() }
//def k = beans.bean.collect { bean -> bean.property.findAll { it.@name == 'letter' }.@value.collect { it.text() } }
def k = beans.bean.each { bean -> 
 beanMap.put("'" + bean.property.find { it.@name == 'letter' }.@value.text() + "'", ["'" + bean.property.find { it.@name == 'halfUnicode' }.@value.text() + "'", "'" + bean.property.find { it.@name == 'unicode' }.@value.text() + "'"]) 
}
println beanMap

beans.bean.each { bean -> 
  beanList.add(["'" + bean.property.find { it.@name == 'halfUnicode' }.@value.text() + "'", "'" + bean.property.find { it.@name == 'unicode' }.@value.text() + "'"]) 
}
println beanList

