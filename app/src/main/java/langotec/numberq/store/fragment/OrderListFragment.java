package langotec.numberq.store.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import langotec.numberq.store.MainActivity;
import langotec.numberq.store.R;
import langotec.numberq.store.WelcomeActivity;
import langotec.numberq.store.adapter.RecyclerViewAdapter;
import langotec.numberq.store.login.Member;
import langotec.numberq.store.map.PhpDB;
import langotec.numberq.store.menu.MainOrder;
import langotec.numberq.store.menu.MenuBaseAdapter;
import langotec.numberq.store.menu.Order;

public class OrderListFragment extends Fragment {

    private WelcomeActivity.OrderHandler orderHandler;
    public static Fragment orderListFragment;

    public OrderListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        WeakReference<Context> weakReference = new WeakReference<>(getContext());
        orderHandler = new WelcomeActivity.OrderHandler(weakReference, "OrderListFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View orderView;
        ArrayList<MainOrder> orderList = MainActivity.orderList;
        if (orderList == null){
            orderView = inflater.inflate(R.layout.fragment_empty, container, false);
            TextView emptyText = (TextView) orderView.findViewById(R.id.emptyText);
            emptyText.setText(getString(R.string.order_queryProcessing));
        }else if (orderList.size() == 0){
            orderView = inflater.inflate(R.layout.fragment_empty, container, false);
            TextView emptyText = (TextView) orderView.findViewById(R.id.emptyText);
            emptyText.setText(getString(R.string.order_emptyOrders));
        }else {
            orderView = inflater.inflate(R.layout.fragment_order_list, container, false);
            RecyclerView mRecyclerView = orderView.findViewById(R.id.order_list);
            mRecyclerView.setHasFixedSize(false);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(orderList);
            mRecyclerView.setAdapter(mAdapter);
        }
        return orderView;
    }

    @Override
    public void onResume() {
        super.onResume();
        orderListFragment = this;
    }

    @Override
    public void onPause() {
        super.onPause();
        orderHandler.removeCallbacksAndMessages(null);
        System.gc();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.order_refresh).setVisible(true);
        menu.findItem(R.id.menu_cart_clear).setVisible(false);
        menu.findItem(R.id.menu_cart_createOrder).setVisible(false);
        menu.findItem(R.id.menu_search).setVisible(false);
        menu.findItem(R.id.menu_backHome).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.order_refresh:
                MainActivity.orderList = null;
                refreshOrder();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //延遲100毫秒避免畫面更新異常
                        WelcomeActivity.getOrderData("OrderListFragment");
                    }
                }, 100);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void refreshOrder(){
        Fragment fragment = OrderListFragment.orderListFragment;
        if (fragment.isResumed()) {
            fragment.getFragmentManager().beginTransaction().detach(fragment)
                    .attach(fragment).commit();
        }
    }
}
