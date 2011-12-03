package ch8.tests


def configObject = new ConfigSlurper().parse(new File("ch8/Config.groovy").toURL())
println configObject.script.decoder