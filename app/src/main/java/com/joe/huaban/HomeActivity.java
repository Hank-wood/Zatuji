package com.joe.huaban;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.joe.huaban.homepage.HomeFragment;
import com.joe.huaban.base.ui.BaseActivity;

public class HomeActivity extends BaseActivity{

    private static final String TAG_HOME_FRAG = "homeFragment";
    private FragmentManager mFragmentMananger;
    @Override
    protected int getContent() {return R.layout.activity_home;}

    @Override
    protected void initPresenter() {}

    @Override
    protected void initView() {
        initFragment();
    }

    private void initFragment(){

        HomeFragment homeFragment=new HomeFragment();
        mFragmentMananger = getSupportFragmentManager();
        mFragmentMananger.beginTransaction().add(R.id.fl_container_home,homeFragment,TAG_HOME_FRAG).commit();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_detail, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==R.id.action_about){
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
