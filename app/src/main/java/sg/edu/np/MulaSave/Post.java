package sg.edu.np.MulaSave;



public class Post {
    private User creator;
    private String postImageUrl;
    private String postDesc;
    private String postUuid;

    public Post(){};

    public Post(User _creator, String _postImageUrl, String _postDesc, String _postUuid){
        creator = _creator;
        postImageUrl = _postImageUrl;
        postDesc = _postDesc;
        postUuid = _postUuid;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostUuid() {
        return postUuid;
    }

    public void setPostUuid(String postUuid) {
        this.postUuid = postUuid;
    }
}
