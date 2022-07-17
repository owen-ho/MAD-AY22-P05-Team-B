package sg.edu.np.MulaSave.messages;

public class messagelistiner {
    private String uid, lastmessage, username,profilepic;

    private int unseenMessages;

    public messagelistiner(String username,String uid, String lastmessage,String profilepic,int unseenMessages) {
        this.username= username;
        this.uid = uid;
        this.lastmessage = lastmessage;
        this.profilepic = profilepic;
        this.unseenMessages = unseenMessages;

    }

    public String getUid() {
        return uid;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }
}
