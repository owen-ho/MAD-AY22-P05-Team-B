package sg.edu.np.MulaSave.HomePage;


import java.time.LocalDateTime;

import sg.edu.np.MulaSave.User;

public class Post {
    private User creator;
    private String postImageUrl;
    private String postDesc;
    private String postUuid;
    private LocalDateTime postDateTime;

    public Post(){};

    public Post(User _creator, String _postImageUrl, String _postDesc, String _postUuid, LocalDateTime _postDateTime){
        creator = _creator;
        postImageUrl = _postImageUrl;
        postDesc = _postDesc;
        postUuid = _postUuid;
        postDateTime = _postDateTime;
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

    public LocalDateTime getPostDateTime() {
        return postDateTime;
    }

    public void setPostDateTime(LocalDateTime postDateTime) {
        this.postDateTime = postDateTime;
    }
}
