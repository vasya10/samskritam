package ch8.config

import ch8.schemes.HKScriptScheme
import ch8.schemes.NativeScriptScheme

Tokens = [' ', '|']
Guru = '1'
Laghu = '0'
Avasanam = [' ', '|', '||'] //#() viramo avasaanam

NativeScript {
  varnamala = ['a','A','e','E','u','U','r.','R.','l.','E.','I','O','O.','M',':','k','K','g','G','n','c','C','j','J','n.','t','T','d','D','N','t.','T.','d.','D.','N.','p','P','b','B','m','y','r','l','v','s','s.','S','h','a.','M.',' ','|','||','OM'   ]
  anunasika = '-'
  anusvara = 'M'
  visarga = ':'
  candrabindu = 'M.'
  avagraha = 'a.'
  t = 't.'
  scheme = new NativeScriptScheme()
}

HKScript {
  varnamala = ['a','A','i','I','u','U','R','RR','L','e','ai','o','au','M','H','k','kh','g','gh','G','c','ch','j','jh','J','T','Th','D','Dh','N','t','th','d','dh','n','p','ph','b','bh','m','y','r','l','v','z','S','s','h','a.','M.',' ','|','||','oM'   ]
  anusvara = 'M'
  visarga = 'H'
  t = 't'
  scheme = new HKScriptScheme()
}

UnicodeScript {
  varnamala = 
  ['a' :['',       '\u0905'], 'A': ['\u093E', '\u0906'],  'e':['\u093F', '\u0907'], 'E': ['\u0940', '\u0908'], 'u': ['\u0941', '\u0909'], 'U':['\u0942', '\u090A'], 'r.':['\u0943', '\u090B'], 
   'R.':['\u0944', '\u090C'], 'l.':['\u0943', '\u0960'], 'L.':['\u0944', '\u0961'], 'E.':['\u0947', '\u090F'], 'I': ['\u0948', '\u0910'], 'O':['\u094B', '\u0913'], 'O.':['\u094C', '\u0914'], 'M': ['','\u0902'], ':' :['','\u0903'], 
   'k' :['\u094D', '\u0915'], 'K': ['\u094D', '\u0916'], 'g': ['\u094D', '\u0917'], 'G': ['\u094D', '\u0918'], 'n': ['\u094D', '\u0919'], 
   'c' :['\u094D', '\u091A'], 'C': ['\u094D', '\u091B'], 'j': ['\u094D', '\u091C'], 'J': ['\u094D', '\u091D'], 'n.':['\u094D', '\u091E'], 
   't' :['\u094D', '\u091F'], 'T': ['\u094D', '\u0920'], 'd': ['\u094D', '\u0921'], 'D': ['\u094D', '\u0922'], 'N': ['\u094D', '\u0923'], 
   't.':['\u094D', '\u0924'], 'T.':['\u094D', '\u0925'], 'd.':['\u094D', '\u0926'], 'D.':['\u094D', '\u0927'], 'N.':['\u094D', '\u0928'], 
   'p' :['\u094D', '\u092A'], 'P': ['\u094D', '\u092B'], 'b': ['\u094D', '\u092C'], 'B': ['\u094D', '\u092D'], 'm': ['\u094D', '\u092E'], 
   'y' :['\u094D', '\u092F'], 'r': ['\u094D', '\u0930'], 'l': ['\u094D', '\u0932'], 'v': ['\u094D', '\u0935'], 
   's' :['\u094D', '\u0936'], 's.':['\u094D', '\u0937'], 'S': ['\u094D', '\u0938'], 'h': ['\u094D', '\u0939'], 
   ' ': [' ',' '], '|': ['','\u0964'], '||': ['','\u0965'],
   '0' :['', '\u0966'], '1' :['', '\u0967'], '2' :['', '\u0968'], '3' :['', '\u0969'], '4' :['', '\u096A'], '5' :['', '\u096B'], '6' :['', '\u096C'], '7' :['', '\u096D'], '8' :['', '\u096E'], '9' :['', '\u096F'],
   'OM':['', '\u0950'] 
  ]
  findKey = { value -> (UnicodeScript.varnamala.find { value in it.value }?.key)?:'' }
}

Gana {
  name = ['N.a':'000','Sa':'001','ja':'010','ya':'011','Ba':'100','ra':'101','t.a':'110','ma':'111','ga':'1','la':'0']
  GURU = '1'
  LAGHU = '0'
  
  find { gana -> name[gana] }
  findName = { metre-> Gana.name.find { it.value == metre }?.key } 
  //ganaDefs.find{it.value ==  _value}?.key }
}
