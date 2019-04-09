export { SMSTCPlayer } from "./smsTcpLayer";
export { SmsTcpController } from "./smsTcpController";
export { SmsTcpAppLayer } from "./smsTcpAppLayer";
export { SmsTcpControllerObserver } from "./smsTcpControllerObserver";

// Interface to implement in order to be able to send sms
export interface SmsBroadcaster {
    sendSms(sms: string, sender: string): void;
}

// Interface to implement in order to receive messages
export interface SmsReceived {
    Originator: string,
    MessageText: string
}



