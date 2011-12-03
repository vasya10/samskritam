package ch8.schemes

import ch8.UnicodeMap


/**
 * A simple script tokenizer
 * uses only three non-alphabetic chars
 * 
 * @author vsrinivasan
 */
class SimpleScriptScheme implements ScriptScheme {

  //List of all varnas - this is for validation
  //TODO Ideally this should come from config and there must be an indirection of varnas to the actual script notation
  //TODO Use ConfigSlurper and read script from a configuration
  static def VARNA_MALA = ['a','A','e','E','u','U','r.','R.','l.','E.','I','O','O.','M',':','k','K','g','G','n','c','C','j','J','n.','t','T','d','D','N','t.','T.','d.','D.','N.','p','P','b','B','m','y','r','l','v','s','s.','S','h',' ','|','||'   ]
  static def TOKENS = [' ', '|']
  static def ANUNASIKA = '-'
  
  //hyphen denotes anunasika
  static List NOTATION_MARKERS = ['.', '-']

  /** 
   * tokenize a given sentence into words
   * 
   * @see tokenizeWord
   */
  @Override
  public List tokenize(String sentence) {
    def varnas = []
    def tokens = new StringTokenizer(sentence, ' |', true)
    tokens.each { varnas.addAll( (it in TOKENS) ? it : tokenizeWord(it)) }
    varnas
  }

  /** 
   * tokenizes a given word into a list of varnas
   * the word could be a pada, shabda, pratyaya or pratyahara
   * TODO needs to handle anunasika
   * 
   * @calledby String.metaClass.varnas()
   * @calledby String.metaClass.iterator
   * @param word
   * @return list of varnas
   * @throws Exception if an unknown varna is encountered
   */
  protected List tokenizeWord(String word) {
    def varnas = []
    for (int i=0; i<word.length(); i++) {
      def (current, next) = [word[i], (i<word.length()-1) ? word[i+1] : '']
      if (current in NOTATION_MARKERS) continue
      current = (next in NOTATION_MARKERS) ? current+next : current
      if (!((current - ANUNASIKA) in VARNA_MALA)) {
        throw new Exception("** unknown varna ($current) in ($word) **")
      }
      varnas << current
    }
    //println varnas
    varnas
  }

  @Override
  public List syllablize(String sentence) {
    def syllables = []
    def syllable = ''

    def v = sentence.varnas() - TOKENS
    for (int i=0; i<v.size(); i++) {
      def (prev, current, next) = v.triplet(i)

      if (next && prev?.svara()) {
        syllables << syllable
        syllable = ''
      }
      syllable += current
    }
    syllables << syllable
  }
  
//  @Override
//  public List syllablize(String sentence) {
//    def syllables = []
//    def tokens = new StringTokenizer(sentence, ' |', true)
//    tokens.each { syllables.addAll( (it in TOKENS) ? it : syllablizeWord(it)) }
//    //println syllables
//    syllables - TOKENS
//  }
//
//  protected List syllablizeWord(String word) {
//    def syllables = []
//    def syllable = ''
//
//    def v = word.varnas()
//    for (int i=0; i<v.size(); i++) {
//      def (prev, current, next) = v.triplet(i)
//
//      if (next && prev?.svara()) {
//        syllables << syllable
//        syllable = ''
//      }
//      syllable += current
//    }
//    syllables << syllable
//  }

  @Override
  public String unicodize(String sentence) {
    toUnicodeSentence(sentence).collect { it in TOKENS ? it : '&#x' + it + ';' }.join()
  }

  protected List toUnicodeSentence(String sentence) {
    def unicodeList = []
    def tokens = new StringTokenizer(sentence, ' |', true)
    tokens.each { unicodeList.addAll( (it in TOKENS) ? it : toUnicodeWord(it)) }
    unicodeList
  }

  protected List toUnicodeWord(String word) {
    def unicodeList = []
    def v = word.varnas()

    for (int i=0; i<=v.size()-1; i++) {
      def (prev, current, next) = v.triplet(i)
      def halfCode = UnicodeMap.unicodeMap[current][0]
      def fullCode = UnicodeMap.unicodeMap[current][1]

      //println "$prev, $current, $next; $halfCode, $fullCode"
      if (current.svara() && prev?.hal()) {
        if (halfCode) unicodeList << halfCode
      } else {
        unicodeList << fullCode
        if (current.hal() && (!next || next?.hal())) unicodeList << halfCode
      }
    }
    unicodeList
  }

}