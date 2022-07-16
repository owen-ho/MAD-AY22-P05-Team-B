package sg.edu.np.MulaSave.messages;

public class messagelistiner {
    private String uid, lastmessage, username;

    private int unseenMessages;

    public messagelistiner(String uid, String lastmessage, String username,int unseenMessages) {
        this.uid = uid;
        this.lastmessage = lastmessage;
        this.unseenMessages = unseenMessages;
        this.username= username;
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

    public String getUsername() {
        return username;
    }
}
