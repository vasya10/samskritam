package ch8.util

import groovy.time.TimeCategory

import java.util.Date

class StopWatch {

  static Date begin
  static Date end

  static void start() {
    begin = new Date()
  }

  static String stop() {
    end = new Date()
    TimeCategory.minus(end, begin).toString()
  }
}

