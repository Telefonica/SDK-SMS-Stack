import { SmsBroadcaster } from "../index";
import axios from "axios";

export class TelefonicaSender implements SmsBroadcaster {

    private broadcastMessage(text: string, sender: string) {
        const data = { smsMmsOriginator: { shortcode: '34638444671' },
        numericDestination: `${sender}`,
        messageText: `${text}` };

        const url = 'https://gem.telefonica.com/REST/SMS';

        const config = {
            headers: {
                'cache-control': 'no-cache',
                'Content-Type': 'application/json',
                Authorization: 'Basic NjAxNjAjNTA3NTkjTFVDQS53czpMVUNBLndz' }
        }

        axios.post(url, data, config).then(function(response) {
            //console.log(response)
        })
    }

    public sendSms(sms: string, sender: string) {
        this.broadcastMessage(sms, sender);
    }
}