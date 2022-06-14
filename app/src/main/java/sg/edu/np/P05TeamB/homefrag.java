package sg.edu.np.P05TeamB;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class homefrag extends Fragment {

    public homefrag() {
        // Required empty public constructor
    }
    public static homefrag newInstance(String param1, String param2) {
        homefrag fragment = new homefrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    //create this method because getView() only works after onCreateView()
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //start the search product activity (testing only)
        Button search = (Button) getView().findViewById(R.id.goSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SearchProduct.class);
                startActivity(i);
            }
        });
    }
}