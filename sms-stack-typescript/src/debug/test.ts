import { Observable, Observer } from 'rxjs';
import { SmsReceived } from "../index";
import { SmsStackControler } from "./smsMTController"
import { TelefonicaSender } from "./smsBroadcaster"
import { TextMessage } from "./textMessage"


let emitter: Observer<SmsReceived>;
const observer = Observable.create((observer: Observer<SmsReceived>) => {
    emitter = observer;
});
const stackController = new SmsStackControler(observer, new TelefonicaSender(), (textMessage: TextMessage) => {
    console.log(textMessage);
});

const newSms: SmsReceived = {
    Originator: "+34617135734",
	MessageText: "61880800000000WRvIGNvYmVydHVyYQ=="
}

emitter!.next(newSms)
