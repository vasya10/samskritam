package ch8.config

import java.util.List;

evaluate(new File("ch8/Bootstrap.groovy"))

script = loadConfig(ch8.config.Script) 

nativeScript = script.NativeScript

varnamala = nativeScript.varnamala
svara = varnamala.range('a', ':')
vyanjana = varnamala.range('k','h')
anusvara = [nativeScript.anusvara]
visarga = [nativeScript.visarga]
candrabindu = [nativeScript.candrabindu]
avagraha = [nativeScript.avagraha]
t = nativeScript.t
anunAsikA = nativeScript.anunasika
avasAnam = varnamala.range(' ', '||')

ku = varnamala.range('k','n')
cu = varnamala.range('c','n.')
tu = varnamala.range('t','N')
thu = varnamala.range('t.','N.')
pu = varnamala.range('p','m')
antastha = varnamala.range('y','v')
USmAna = varnamala.range('s','h')
allVarnas = varnamala.range('a','OM')

dIrgha = ['A','E','U','R.','E.','I','O','O.','M',':'] //'AEUR.E.IOO.M:'.varnas()
hrasva = svara - dIrgha

/** sthAna */

//a-ku-ha-visarjaniyaanaam kantaH
//TODO include all 18 types of a, using the spread operator
kaNTha = ['a', 'A'] + ku + ['h'] + visarga
//i-cu-ya-Saanaam taalu
tAlu = ['e', 'E'] + cu + ['y', 's']
//r.-tu-ra-Saanaam murdha
mUrdha = ['r.', 'R.'] + tu + ['r', 's.']
//l.-tu-la-saanaam danta
danta = ['l.'] + thu + ['l', 'S']

//TODO upu-upadhmaniyaanaam oStaH
upadhmAna = ''
oSTha = ['u', 'U'] + pu + ['v'] //+ upadhmaniya
//nasika
nAsikA = [cu, pu, ku, tu, thu]*.last()

kaNTha_tAlu = ['E.','I']
kaNTha_OSTham = ['O','O.']

//dantoStam
dantoSTham = ['v']
//TODO jihvAmulIyasya jihvAmulam
jihvAmula = ['']

sthAna = [kaNTha, tAlu, mUrdha, danta, oSTha, nAsikA, kaNTha_tAlu, kaNTha_OSTham, dantoSTham, jihvAmula]

prayatna {
  //aabhyantara_prayatna [saMvRta,vivRta, iizad_vivRta, spRSTa, iizad_spRSTa
  samvRta = ['a']
  //#(8.last) 
  vivRta = varnamala.range('a', 'O.')
  sparza = varnamala.range('k', 'm')
  iizad_spRSTa = varnamala.range('y','h')
  iiSad_vivRta = varnamala.range('y','h')
  
  //baahya prayatna
  //[samvAra, vivAra, svAra, nAda, ghoSa, agoSa, alpaprANa, mahAprANa, udAtta, anudAtta, svarita]
  samvAra = []
  vivAra = []
  zvAsa = []
  nAda = []
  goSa = []
  agoSa = []
  alpaprANa = ['k','g','c','j','t','d','t.','d.','p','b']
  mahaprANa = ['K','G','C','J','T','D','T.','D.','P','B']
}