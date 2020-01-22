package com.sdwnmt.smartcollector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Collector_Home extends AppCompatActivity {

    private long backPressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector__home);

//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        HomeFragment homeFragment = new HomeFragment();
        final ProfileFragment profileFragment = new ProfileFragment();
        final NotificationFragment notificationFragment = new NotificationFragment();
        //startActivity(new Intent(Collector_Home.this,Main2Activity.class));
        setFragment(profileFragment);

//        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                int id = menuItem.getItemId();
//
//                return false;
//            }
//        });
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();

    }
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(this, "Hit Back Again To Exit !", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

}
