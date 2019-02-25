// import { SMSStack } from '../index';
// test('New Sms', () => {
//   expect(SMSStack('Hola que tal')).toBe('Hola que tal');
// });

import { SMSTCPlayer, TcpLayer } from '../smsTcpLayer';
test('Encode Tcp Layer', () => {
  const layerTest: TcpLayer = {id: 0, key: 17, syn: 1, ack: 1, psh: 1, fin: 1, sBegin: 0, cipher: 0, checkSum: 0, data: "hola que tal" };
  expect(SMSTCPlayer.encodeSMS(layerTest)).toBe('08f80000000000hola que tal');
});

test('Decode Tcp Layer', () => {
  const layerTest: TcpLayer = {id: 0, key: 17, syn: 1, ack: 1, psh: 1, fin: 1, sBegin: 0, cipher: 0, checkSum: 0, data: "hola que tal" };
  expect(SMSTCPlayer.decodeSMS('08f80000000000hola que tal')).toEqual(layerTest);
})


import { SMSTCP } from '../smsTcp';
test('Encode Base64', () => {
  expect(SMSTCP.encodeBase64('Hello world')).toBe('SGVsbG8gd29ybGQ=');
})
