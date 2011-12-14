package ch8.schemes

import java.util.List;

/**
 * A simple script tokenizer
 * uses only three non-alphabetic chars
 * 
 * @author vsrinivasan
 */
class NativeScriptScheme implements ScriptScheme {

  static def Script = new ConfigSlurper().parse(ch8.config.Script)
  static def Tokens = Script.Tokens

  static def Varnamala = Script.NativeScript.varnamala
  static def Anunasika = Script.NativeScript.anunasika
  static def Visarga = Script.NativeScript.visarga
  static def Anusvara = Script.NativeScript.anusvara
  static def unicodeMap = Script.UnicodeScript.varnamala

  //hyphen denotes anunasika
  static List NotationMarkers = ['.', '-']

  /** 
   * tokenize a given sentence into words
   * 
   * @see tokenizeWord
   */
  @Override
  public List tokenize(def text) {
    if (text instanceof List) {
      text = text.join()
    }
    def varnas = []
    def words = new StringTokenizer(text, Tokens.join(), true)
    words.each { varnas.addAll( (it in Tokens) ? it : tokenizeWord(it)) }
    varnas
  }

  protected List tokenizeWord(String word) {
    def varnas = []
    for (int i=0; i<word.length(); i++) {
      def current = word[i] - Anunasika, next = ((i<word.length()-1) ? word[i+1] : '') //- Anunasika
      if ((current+next) in Varnamala || next == '-') {
        varnas << (current+next)
        i++
      } else if ((current) in Varnamala){
        varnas << current
      } else {
        throw new Exception("** unknown varna ($current) in ($word) **")
      }
    }
    varnas
  }

  @Override
  public List syllablize(String sentence) {
    def syllables = []
    def syllable = ''

    def v = sentence.varnas() - Tokens
    for (int i=0; i<v.size(); i++) {
      def (prev, current, next) = v.triplet(i)

      if (current == Visarga || current == Anusvara) {
        syllable += current
        syllables << syllable
        syllable = ''
      } else {
        if (next && prev?.svara()) {
          syllables << syllable
          syllable = ''
        }
        syllable += current
      }
    }
    if (syllable) syllables << syllable
    syllables - ''
  }

  /**
   * converts a text from native script to unicode
   * @see toUnicodeWord
   * @param sentence
   * @return
   */
  @Override
  public String toUnicode(String text) {
    def unicodeList = []
    def words = new StringTokenizer(text, Tokens.join(), true)
    words.each { unicodeList.addAll( (it in Tokens) ? it : toUnicodeWord(it)) }
    //println "unicodelist : " + unicodeList
    unicodeList.join()
  }

  /**
   * converts from unicode to native script
   * iterates over each unicode character, finds it within the UnicodeScript.varnamala and gets the key
   * the key is NativeScript
   * @param unicodeText
   * @return
   */
  @Override
  public String fromUnicode(def unicode) {
    if (unicode instanceof String) unicode = unicode.toList()
    def result = ''
    for (int i=0; i<unicode.size();i++) {
      def current = unicode[i], next = unicode[i+1]

      boolean vyanjana = current >= '\u0915' && current <= '\u0939'
      boolean nextHalfStop= (next >= '\u093E' && next <= '\u094C') || (next == '\u094D')
      boolean halfStopVyanjana = current == '\u094D'

      if (halfStopVyanjana) {
        //skip; do nothing
      } else if (current in Tokens) {
        result += current
      } else {
        result += Script.UnicodeScript.findKey(current)
        if (vyanjana && !nextHalfStop) result += 'a'
        //println "adding ${u.key} to: $result"
      }
    }
    result
  }

  /**
   * tokenizes a given word into a list of varnas
   * the word could be a pada, shabda, pratyaya or pratyahara
   * TODO needs to handle anunasika (third letter)
   *
   * @calledby String.metaClass.varnas()
   * @calledby String.metaClass.iterator
   * @param word
   * @return list of varnas
   * @throws Exception if an unknown varna is encountered
   */
  protected List tokenizeWord2(String word) {
    def varnas = []
    for (int i=0; i<word.length(); i++) {
      def current = word[i], next = (i<word.length()-1) ? word[i+1] : ''
      if (current in NotationMarkers) continue
      current = (next in NotationMarkers) ? current+next : current
      if (!((current - Anunasika) in Varnamala)) {
        throw new Exception("** unknown varna ($current) in ($word) **")
      }
      varnas << current
    }
    varnas
  }

  public String toUnicodeHtml(String text) {
    toUnicode(text).collect { it in Tokens ? it : '&#x' + it + ';' }.join()
  }

  /**
   * converts a word from NativeScript to Unicode
   * @param word
   * @return
   */
  protected List toUnicodeWord(def word) {
    def unicodeList = []
    def v = word.varnas()

    for (int i=0; i<=v.size()-1; i++) {
      def (prev, current, next) = v.triplet(i)
      def halfCode = unicodeMap[current][0]
      def fullCode = unicodeMap[current][1]

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