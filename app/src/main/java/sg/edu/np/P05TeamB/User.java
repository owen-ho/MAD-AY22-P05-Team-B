package sg.edu.np.P05TeamB;

public class User {
    public String uid;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String email){
        this.uid = id;
        this.email = email;
    }
}
