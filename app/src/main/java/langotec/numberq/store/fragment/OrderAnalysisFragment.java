package langotec.numberq.store.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import langotec.numberq.store.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderAnalysisFragment extends Fragment {


    public OrderAnalysisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_analysis, container, false);
    }

}
