package sg.edu.np.MulaSave.messages;

import java.io.Serializable;

public class MessageListener implements Serializable {
    private String uid, lastmessage, username,profilepic,chatkey,sellerid;

    private int unseenMessages;

    public MessageListener(){
    }

    public MessageListener(String username, String uid, String lastmessage, String profilepic, int unseenMessages, String chatkey, String sellerid) {
        this.username= username;
        this.uid = uid;
        this.lastmessage = lastmessage;
        this.profilepic = profilepic;
        this.unseenMessages = unseenMessages;
        this.chatkey=chatkey;
        this.sellerid=sellerid;

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

    public String getChatkey() {
        return chatkey;
    }
    public String getSellerid() {
        return sellerid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public void setChatkey(String chatkey) {
        this.chatkey = chatkey;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public void setUnseenMessages(int unseenMessages) {
        this.unseenMessages = unseenMessages;
    }
}
