package ch8.scripts

List.metaClass.range = { first, last ->
  if (first == String && last == String) {
    delegate[delegate.indexOf(first)..delegate.indexOf(last)]
  } else {
    delegate[first..last]
  }
}

List.metaClass.triplet = { i -> delegate.size() <= 0 ? [null,null,null] : i == 0 ? [null, delegate[i], (delegate.size()>1) ? delegate[i+1] : null] : (i==delegate.size()) ? [(i<1) ? null : delegate[i-1]] : [delegate[i-1], delegate[i], delegate[i+1]] }

void testTuple() {
  Tuple tuple = new Tuple(5,8)
  println tuple
  assert tuple == [5, 8]

  def (begin, end) = [5, 8, 9, 10]
  assert begin == 5
  assert end == 8
  println begin
  println end
}

void testListRange() {
  def letters = "aAeEuUr.R.l.E.IOO.M:kKgGncCjJn.tTdDNt.T.d.D.N.pPbBmyrlvss.Sh".toList()
  assert ['a','A'] == letters.range('a','A')
}

void testListTriplet() {
  println "\n ---- testListTriplet ---- \n\n"
  
  def prev, current, next
  List list = 1..10 as List
  
  println "\n test full list \n\n"
  (0..list.size()-1).each {
    println list.triplet(it)
  }
  
  println "\n test partial list \n\n"
  (0..(list.size()-1)/2).each {
    println list.triplet(it)
  }

  println "\n test one value \n\n"
  list = 1..1 as List
  (0..list.size()-1).each {
    println list.triplet(it)
  }
  
  println "\n test no values \n\n"
  list = []
  (0..0).each {
    println list.triplet(it)
  }
}

void testGroovyTruth() {
  String s = ''
  assert !s
}

void testArrayIndexOutOfBoundException() {
  def a = [1,2,3,4,5,6,7,100,120]
  for (int i = 0; i<=a.size(); i++) {
    println a[i+5]
  }
}

void testStringIndexOutOfBoundException() {
  def a = 'hello world'
  for (int i = 0; i<=a.size(); i++) {
    println a[i+5]
  }
}

void testTokenizer() {
  String s = 'samskritam | bharati ||'
  println s.tokenize(' |')
  println s.split(/ \|\|/)
  def st = new StringTokenizer(s, ' |', true)
  st.each { print it + ","}
}

void methods() {
  def list = [1,2,3,4,5,6,7,8,9,10]
  //println list.length()
  String s = "134513511"
  println s.length()
  println s.size()
}

void nearest3() {
  def k = [0,1,2,3,4,5,6,7,8,9,10]
  k.eachWithIndex { a, i-> def p = (int) a/3; println (p) * a }
  //k.each { println ((int) (it/3))*it } 
}
//testTuple()
//testListRange()
//testListTriplet()
//testGroovyTruth()
//testStringIndexOutOfBoundException()
//testTokenizer()
//methods()
nearest3()
