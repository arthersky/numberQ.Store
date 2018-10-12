package langotec.numberq.store;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import langotec.numberq.store.R;
import langotec.numberq.store.adapter.BottomNavigationViewHelper;
import langotec.numberq.store.adapter.ViewPagerAdapter;
import langotec.numberq.store.fragment.MoreFragment;
import langotec.numberq.store.fragment.OrderAnalysisFragment;
import langotec.numberq.store.fragment.OrderFinishFragment;
import langotec.numberq.store.fragment.OrderListFragment;
import langotec.numberq.store.menu.Order;

public class MainActivity extends AppCompatActivity {

    public static ArrayList orderList;

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private MenuItem menuItem;

    //Fragments
    private OrderListFragment orderListFragment;
    private OrderFinishFragment orderFinishFragment;
    private OrderAnalysisFragment orderAnalysisFragment;
    private MoreFragment moreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //take data
        if (orderList == null){
            try{
                orderList = new ArrayList<>();
                orderList = (ArrayList) getIntent().getSerializableExtra("orderList");
                Log.e("data","orderList 1:"+orderList.get(0).toString());
            }catch (Exception e){
                Log.e("dataErr",e.toString());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupView();
//        viewPager.setCurrentItem(getIntent().getExtras("currentPage", 0));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPage",viewPager.getCurrentItem());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_backHome).setVisible(false);
        menu.findItem(R.id.menu_cart_clear).setVisible(false);
        menu.findItem(R.id.menu_cart_createOrder).setVisible(false);
        menu.findItem(R.id.menu_search).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_camera:
                Toast.makeText(this, "Scan QR Code", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView(){
        //init bottomNavigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_order_list:
                        MainActivity.this.viewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_order_finish:
                        MainActivity.this.viewPager.setCurrentItem(1);
                        break;
                    case R.id.navigation_order_analysis:
                        MainActivity.this.viewPager.setCurrentItem(2);
                        break;
                    case R.id.navigation_more:
                        MainActivity.this.viewPager.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });

        //init Viewpager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null){
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page","onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                menuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ViewPagerAdapter VPAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        orderListFragment = new OrderListFragment();
        orderFinishFragment = new OrderFinishFragment();
        orderAnalysisFragment = new OrderAnalysisFragment();
        moreFragment = new MoreFragment();

        VPAdapter.addFragment(orderListFragment);
        VPAdapter.addFragment(orderFinishFragment);
        VPAdapter.addFragment(orderAnalysisFragment);
        VPAdapter.addFragment(moreFragment);

        viewPager.setAdapter(VPAdapter);
        Log.e("init","init finish");
//        orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_order_list);
    }
}
