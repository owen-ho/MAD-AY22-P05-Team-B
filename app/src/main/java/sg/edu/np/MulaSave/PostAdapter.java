package sg.edu.np.MulaSave;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    ArrayList<Post> postList;

    public PostAdapter(ArrayList<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;//find view and return the viewholder
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.creatorUsername.setText("nbcb");
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView creatorImage, postImage;
        TextView creatorUsername, postCaption;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            creatorImage = itemView.findViewById(R.id.creatorImage);
            postImage = itemView.findViewById(R.id.postImage);
            creatorUsername = itemView.findViewById(R.id.creatorUsername);
            postCaption = itemView.findViewById(R.id.postCaption);
        }
    }
}
