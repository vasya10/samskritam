package ch8.schemes

import java.util.List;

/**
 * A simple script tokenizer
 * uses only three non-alphabetic chars
 * 
 * @author vsrinivasan
 */
class NativeScriptScheme implements ScriptScheme {

  static def script = new ConfigSlurper().parse(ch8.config.Script)
  static def tokens = script.Tokens

  static def varNamAlA = script.NativeScript.varnamala
  static def anunAsikA = script.NativeScript.anunasika
  static def visarga = script.NativeScript.visarga
  static def anusvara = script.NativeScript.anusvara
  static def unicodeMap = script.UnicodeScript.varnamala

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
    def words = new StringTokenizer(text, tokens.join(), true)
    words.each { varnas.addAll( (it in tokens) ? it : tokenizeWord(it)) }
    varnas
  }

  /**
   * tokenizes a given word into a list of varnas
   * the word could be a pada, shabda, pratyaya or pratyahara
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
      def current = word[i] - anunAsikA, next = ((i<word.length()-1) ? word[i+1] : '') //- Anunasika
      if ((current+next) in varNamAlA || next == '-') {
        varnas << (current+next)
        i++
      } else if ((current) in varNamAlA){
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

    def v = sentence.varnas() - tokens
    for (int i=0; i<v.size(); i++) {
      def (prev, current, next) = v.triplet(i)

      if (current == visarga || current == anusvara) {
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
    def words = new StringTokenizer(text, tokens.join(), true)
    words.each { unicodeList.addAll( (it in tokens) ? it : toUnicodeWord(it)) }
    //println "unicodelist : " + unicodeList
    unicodeList.join()
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
      } else if (current in tokens) {
        result += current
      } else {
        result += script.UnicodeScript.findKey(current)
        if (vyanjana && !nextHalfStop) result += 'a'
        //println "adding ${u.key} to: $result"
      }
    }
    result
  }

  //TODO Not working
  public String toUnicodeHtml(String text) {
    toUnicode(text).collect { it in tokens ? it : '&#x' + it + ';' }.join()
  }

}