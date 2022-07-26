package sg.edu.np.MulaSave.chat;

public class ChatListener {

    private String uid,username, message, date, time;

    public ChatListener(String uid, String username, String message, String date, String time) {
        this.uid = uid;
        this.username = username;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}

