package sg.edu.np.P05TeamB;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profilefrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profilefrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profilefrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profilefrag newInstance(String param1, String param2) {
        profilefrag fragment = new profilefrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false);
        View V = inflater.inflate(R.layout.fragment_profile, container, false);
        ImageView profilepic = V.findViewById(R.id.imageView3);
        Picasso.get().load("https://s.yimg.com/ny/api/res/1.2/XQDn25S2GxDbItD31nyahQ--/YXBwaWQ9aGlnaGxhbmRlcjt3PTk2MDtoPTU4OTtjZj13ZWJw/https://s.yimg.com/os/creatr-uploaded-images/2021-02/e2698180-6b90-11eb-97fb-e40529b50439").into(profilepic);
        return  V;
    }
}