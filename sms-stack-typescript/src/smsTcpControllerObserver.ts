import { SMSTCPlayer } from "./smsTcpLayer";

export abstract class SmsTcpControllerObserver {
    public handleFinalMessageReceived(messages: string[], sender: string){}

    public handleMessageReceived(layer: SMSTCPlayer, sender: string) {}
    
    public handleFinalMessageSent(messages: string[], sender: string){}

    public handleMessageSent(layer: SMSTCPlayer, sender: string) {}
}
