package ch8.tests


def configObject = new ConfigSlurper().parse(new File("dsl/Config.groovy").toURL())
println configObject.script.decoder