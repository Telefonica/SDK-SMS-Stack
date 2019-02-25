export interface TextMessage {
    text: string;
    chat: string;
    state: "SMS";
    time: Date;
    senderId: string;
    recipientId: string;
    senderName: string;
    type: "TEXT";
}