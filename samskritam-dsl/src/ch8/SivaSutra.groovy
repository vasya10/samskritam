package ch8

import java.util.List

/**
 * Implementation of Maheshvara Sutra using NativeScript transliteration scheme
 * The table itself can be moved to a groovy configuration file to allow a different scheme like HK, ITRANS or IAST
 * 
 * @author vsrinivasan
 */
@Singleton
class SivaSutra  {

  boolean lazy = false
  
  //siva-sutraani, bang marks it-s
  List table =
  [
    ['a', 'e', 'u', 'N!'],
    ['r.', 'l.', 'k!'],
    ['E.', 'O', 'n!'],
    ['I', 'O.', 'c!'],
    ['h', 'y', 'v', 'r', 't!'],
    ['l', 'N!'],
    ['n.', 'm', 'n', 'N', 'N.', 'm!'],
    ['J', 'B', 'n.!'],
    ['G', 'D', 'D.', 's.!'],
    ['j', 'b', 'g', 'd', 'd.', 's!'],
    ['K', 'P', 'C', 'T', 'T.', 'c', 't', 't.', 'v!'],
    ['k', 'p', 'y!'],
    ['s', 's.', 'S', 'r!'],
    ['h', 'l!']
  ]

  List list = table.flatten()

  int indexOf(String varna) { list.indexOf(varna) }

  int lastIndexOf(String varna) { list.lastIndexOf(varna) }

  @Override
  Iterator iterator() { list.iterator() }

  //eShaam antyaaH it
  List itVarnas = table.collect { it.last() }

  /**
   * is this iT-marker?
   * this finds only 'pratyahara iT' is defined, for other it-s see ItRules.groovy
   * 
   * @see ItRules
   */
  boolean isIt(f) { itVarnas.contains(f + '!') }

  /**
   * expands a given pratyahara, including all the iT-s
   * not for practical purposes, but good for testing
   * 
   * @param pratyahara
   * @return
   */
  List range(String pratyahara) {
    def (begin, end) = pratyahara.varnas()
    list[indexOf(begin)..indexOf(end + '!')]
  }

  /**
   * returns the real pratyahara varna-s, excluding the intermediate it-markers
   * 
   * @param pratyahara
   * @return
   */
  List collect(String pratyahara) {
    def (begin, end) = pratyahara.varnas()
    
    def (b, e1, e2) = [indexOf(begin), indexOf(end+'!'), lastIndexOf(end+'!')]
    //N! occurs twice, so need to find the N! that comes after the beginning varna
    def e = b<e1?e1:e2 
    list[b..e].collect { it.endsWith('!')?'':it } - ''
  }
}
