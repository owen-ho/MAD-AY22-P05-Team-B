package sg.edu.np.MulaSave.HomePage;


import java.time.LocalDateTime;

import sg.edu.np.MulaSave.User;

public class Post {
    private String creatorUid;
    private String postImageUrl;
    private String postDesc;
    private String postUuid;
    private String postDateTime;

    public Post(){};

    public Post(String _creatorUid, String _postImageUrl, String _postDesc, String _postUuid, String _postDateTime){
        creatorUid = _creatorUid;
        postImageUrl = _postImageUrl;
        postDesc = _postDesc;
        postUuid = _postUuid;
        postDateTime = _postDateTime;
    }

    public String getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(String creator) {
        this.creatorUid = creator;
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

    public String getPostDateTime() {
        return postDateTime;
    }

    public void setPostDateTime(String postDateTime) {
        this.postDateTime = postDateTime;
    }
}
