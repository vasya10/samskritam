package ch8.scratch

import groovy.time.TimeCategory

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.Date

@Retention(RetentionPolicy.RUNTIME)
@interface StopWatch {
}

class TimerConsumer implements GroovyInterceptable {

  @StopWatch
  void calc() {
    1..10.each { println it }
  }

  def test(String name, args) {
    println TimerConsumer.class.getMethod(name, args).getDeclaredAnnotations()
    //def result = metaMethod.invoke(this, args)
    //System.out.println ("Completed $name")
    //return result
  }
}



TimerConsumer tc = new TimerConsumer()
tc.test('calc', null)
