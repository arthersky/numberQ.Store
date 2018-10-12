package langotec.numberq.store.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import langotec.numberq.store.R;
import langotec.numberq.store.adapter.RecyclerViewAdapter;
import langotec.numberq.store.menu.Order;

public class OrderListFragment extends Fragment {

    //RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //View
    private View view;

    //Context
    private Context context;

    //OrderList
    private ArrayList<Order> orderList;

    public OrderListFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_list, container, false);
        context = getContext();



        return view;


    }

    private void setupRecyclerView(){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.order_list);
        // 若設為FixedSize可以增加效率不過就喪失了彈性
        mRecyclerView.setHasFixedSize(true);
        // 選擇一種Layout管理器這邊是選擇（linear layout manager）
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 設定適配器
        mAdapter = new RecyclerViewAdapter(orderList);
        mRecyclerView.setAdapter(mAdapter);
    }

}
