package ch8.scripts

static void testTuple() {
  Tuple tuple = new Tuple(5,8)
  println tuple
  assert tuple == [5, 8]

  def (begin, end) = [5, 8, 9, 10]
  assert begin == 5
  assert end == 8
  println begin
  println end
}

List.metaClass.range = { first, last ->
  if (first == String && last == String) {
    delegate[delegate.indexOf(first)..delegate.indexOf(last)]
  } else {
    delegate[first..last]
  }
}

static void testListRange() {
  def letters = "aAeEuUr.R.l.E.IOO.M:kKgGncCjJn.tTdDNt.T.d.D.N.pPbBmyrlvss.Sh".toList()
  assert ['a','A'] == letters.range('a','A')
}

testListRange()