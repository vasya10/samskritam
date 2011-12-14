package ch8.config

Gana {
  name = ['N.a':'000','Sa':'001','ja':'010','ya':'011','Ba':'100','ra':'101','t.a':'110','ma':'111','ga':'1','la':'0']
  GURU = '1'
  LAGHU = '0'
  
  find { gana -> name[gana] }
  findName = { metre-> Gana.name.find { it.value == metre }?.key }
  //ganaDefs.find{it.value ==  _value}?.key }
}

kanya {
  chandas = ''
  name = 'kaN.yA'
  syllables = 4 
  aliases = []
  gana = 'maga'
  definition = 'gmO. cet. kaN.yA'
  examples = []
}

pankti {
  chandas = ''
  name = 'pankt.e:'
  syllables = 5
  gana = 'Bagaga'
  definition = 'BgO. get.e pankt.e:'
  examples = []
}

tanumadhya {
  chandas = ''
  name = 't.aN.umaD.yA'
  syllables = 6
  gana = 't.aya'
  definition = 't.yO. cet. t.aN.umaD.yA'
  examples = []
}

vasantatilaka {
  syllables = 16
}

sardulavikridita {
  syllables = 19
}

