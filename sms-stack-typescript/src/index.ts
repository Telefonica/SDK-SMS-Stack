export { SMSTCPlayer } from "./smsTcpLayer";
export { SmsTcpController } from "./smsTcpController";
export { SmsTcpAppLayer } from "./smsTcpAppLayer";
export { SmsTcpControllerObserver } from "./smsTcpControllerObserver";

export interface SmsBroadcaster {
    sendSms(sms: string, sender: string): void;
}

export interface SmsReceived {
    Originator: string,
    MessageText: string
}



