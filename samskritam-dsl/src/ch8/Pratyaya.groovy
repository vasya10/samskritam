package ch8

class Pratyaya {

  String value
  String realValue
  
  Pratyaya(String _value) {
    value = _value
    realValue = value?.tasyaLopah()
  }
  
  @Override
  String toString() { realValue }
}
