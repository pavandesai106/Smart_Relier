package ccom.example.chat_app_pavan;


public class Message {

    public final String message;
    public final String toEmail;
    public final String fromEmail;
    public final boolean isMine;

    public Message(String message, String toEmail, String fromEmail,boolean isMine) {
        this.message = message;
        this.toEmail = toEmail;
        this.fromEmail = fromEmail;
        this.isMine=isMine;
    }
}
