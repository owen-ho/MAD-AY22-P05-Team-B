package sg.edu.np.MulaSave.messages;

public class messagelistiner {
    private String uid, lastmessage;

    private int unseenMessages;

    public messagelistiner(String uid, String lastmessage, int unseenMessages) {
        this.uid = uid;
        this.lastmessage = lastmessage;
        this.unseenMessages = unseenMessages;
    }

    public String getUid() {
        return uid;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }
}
