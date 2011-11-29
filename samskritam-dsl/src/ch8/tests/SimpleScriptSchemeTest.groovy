package ch8.tests

import java.util.List

import org.codehaus.groovy.control.CompilerConfiguration

import ch8.schemes.SimpleScriptScheme

List decode(String word = 'kr.s.n.a') {
	List NotationMarkers = ['.', ':', '-']
	def varnas = []

	varnas = word.eachWithIndex { c, i ->
		c = ((i < word.length()-1) ? ((word[i+1] in NotationMarkers) ? (c + word[i+1]) : c) : c)
	}.collect { !(it in NotationMarkers) }

	varnas
}

