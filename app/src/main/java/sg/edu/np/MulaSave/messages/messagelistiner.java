package sg.edu.np.MulaSave.messages;

public class messagelistiner {
    private String uid, lastmessage, profilepic;

    private int unseenMessages;

    public messagelistiner(String uid, String lastmessage, String profilepic,int unseenMessages) {
        this.uid = uid;
        this.lastmessage = lastmessage;
        this.unseenMessages = unseenMessages;
        this.profilepic= profilepic;
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

    public String getProfilepic() {
        return profilepic;
    }
}
