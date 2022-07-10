package sg.edu.np.MulaSave;

public class User {
    public String uid;
    public String email;
    public String username;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String email,String username){
        this.uid = id;
        this.email = email;
        this.username = username;
    }

    public String getUid() {
        return this.uid;
    }
    public void setUid(String _uid){
        this.uid = _uid;
    }

    public String getEmail() {
        return this.email;
    }
    public void setEmail(String _email){
        this.email = _email;
    }

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String _username){
        this.username = _username;
    }
}
