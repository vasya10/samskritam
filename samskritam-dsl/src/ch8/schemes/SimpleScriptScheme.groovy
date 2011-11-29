package ch8.schemes

import ch8.UnicodeMap


/**
 * A simple script tokenizer
 * uses only three non-alphabetic chars
 * 
 * @author vsrinivasan
 */
class SimpleScriptScheme implements ScriptScheme {

  // hyphen denotes anunasika
  static List NotationMarkers = ['.', ':', '-', 'M']

  /** 
   * split/tokenize a given word into a list of varnas
   * the word could be a pada, shabda, pratyaya or pratyahara
   * needs to handle anunasika properly
   * 
   * @calledby String.metaClass.varnas()
   * @param word
   * @return list of varnas
   */
  @Override
  public List tokenize(String word) {
    def varnas = []
    
    //alternative implementation without using iterator, because String.metaClass.iterator calls this one.
    for (int i = 0; i < word.length(); i++) {
      def c = word[i]
      c = ((i < word.length()-1) ? ((word[i+1] in NotationMarkers) ? (c + word[i+1]) : c) : c)
      if (!(c in NotationMarkers)) varnas << c
    }
//    word.eachWithIndex { c, i ->
//      c = ((i < word.length()-1) ? ((word[i+1] in NotationMarkers) ? (c + word[i+1]) : c) : c)
//      if (!(c in NotationMarkers)) varnas << c
//    }
    varnas
  }

  //suklAmBaraD.aram ves.Num sasivarNam cat.urBujam | praSaN.N.a vadaN.am D.yayEt. Sarva viG.nopa sAnt.ayE |
  //su-klA-mBa-ra-D.a-ram ve s.Num
  @Override
  public List syllablize(String word) {
    def syllables = [], syllable = ''
    def prev, current, next

    def v = word.varnas()
    for (int i=0; i<v.size(); i++) {
      if (i > 0) prev = v[i-1]
      current = v[i]
      if (i < v.size()-1) next = v[i+1]

      if (prev?.svara()) {
        if (syllable) syllables << syllable
        syllable = '' + current
      } else {
        syllable += current
      }
    }
    syllables << syllable
  }

  @Override
  public List syllablize2(String word) {
    def syllables = [], syllable = ''
    def current, next

    def v = word.varnas()
    for (int i=0; i<v.size(); i++) {
      current = v[i]
      if (i < v.size()-1) next = v[i+1]

      if (current.hal() && next?.svara()) {
        if (syllable) syllables << syllable
        syllable = '' + current
      } else {
        syllable += current
      }
    }
    syllables << syllable
  }
  
  @Override
  public List toUnicodeList(String word) {
    def v = word.varnas()
    def unicodeList = []
    def halfCode, fullCode
    
    for (int i=0; i<=v.size()-1; i++) {
      def (prev, current, next) = v.triplet(i)
      println "$prev, $current, $next"
      halfCode = UnicodeMap.unicodeMap[current][0]
      fullCode = UnicodeMap.unicodeMap[current][1]
      
      if (current.hal()) {
        if (!next || next?.hal()) {
          unicodeList << fullCode
          unicodeList << halfCode
        } else {
          unicodeList << fullCode
        }
      } else if (current.svara() && prev?.hal()) {
        if (halfCode) {
          unicodeList << halfCode
        }
      } else {
        unicodeList << fullCode
      }
    }
    
    unicodeList
  } 
  
  @Override
  public String toUnicode(String word) {
    toUnicodeList(word).collect { '&#x' + it + ';' }.join()
  }

//  @Override
//  public List toUnicode(String word) {
//    def v = word.varnas()
//    def unicodeList = []
//    def prev, current, next, halfCode, fullCode
//    for (int i=0; i<v.size(); i++) {
//      if (i > 0) prev = v[i-1]
//      current = v[i]
//      if (i < v.size()) next = v[i+1]
//      halfCode = UnicodeMap.unicodeMap[current][0]
//      fullCode = UnicodeMap.unicodeMap[current][1]
//      if (current.hal()) {
//        println current + " = " + fullCode
//        if (!next || next?.hal()) {
//          unicodeList << fullCode
//          unicodeList << halfCode
//        } else {
//          unicodeList << fullCode
//        }
//      } else if (current.svara() && prev.hal()) {
//        if (halfCode) {
//          println current + " = " + halfCode
//          unicodeList << halfCode
//        }
//      } else {
//        println current + " = " + fullCode
//        unicodeList << fullCode
//      }
//    }
//    unicodeList
//  } 
}