import { SMSTCPlayer } from "./smsTcpLayer";

// Abstract class to inherit
export abstract class SmsTcpControllerObserver {
    public handleFinalMessageReceived(messages: string[], sender: string){}

    public handleMessageReceived(layer: SMSTCPlayer, sender: string) {}
    
    public handleFinalMessageSent(messages: string[], recipient: string){}

    public handleMessageSent(layer: SMSTCPlayer, recipient: string) {}
}
