package in.mastersaab.mastersaab.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import in.mastersaab.mastersaab.R;
import in.mastersaab.mastersaab.adapter.ViewPagerAdapter;
import in.mastersaab.mastersaab.fragment.LatestFragment;
import in.mastersaab.mastersaab.fragment.TrendingFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String previousData ="previousData";

    private TrendingFragment trendingFragment = new TrendingFragment();
    private LatestFragment latestFragment = new LatestFragment();




    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        //adding the toolbar
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);


        //tablayout setup with viewPager
        tabLayout.setupWithViewPager(viewPager);

        //setting viewPager Adapter
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        //adding fragment to viewPager Adapter start
        viewPagerAdapter.addFragment(latestFragment, "नवीनतम");
        viewPagerAdapter.addFragment(trendingFragment, "ट्रेंडिंग");
        //adding fragment to viewPager Adapter end

        //adding viewPager Adapter
        viewPager.setAdapter(viewPagerAdapter);

        //adding icon to tab layout start
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_new_icon_black);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_trending_icon_black);
        //adding icon to tab layout start

        //navigation drawer start
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //navigation drawer end

    }


    public void drawerItemClick(String drawer_item) {
        Bundle bundle = new Bundle();
        bundle.putString("collection", drawer_item);

        if(!previousData.equals(drawer_item)) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(latestFragment);
            ft.detach(trendingFragment);
            latestFragment.setArguments(bundle);
            trendingFragment.setArguments(bundle);
            ft.attach(latestFragment);
            ft.attach(trendingFragment);
            ft.commit();
            previousData = drawer_item;
        }
    }


    //navigation drawer item click method
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        switch (item.getItemId()) {

            case R.id.nav_home:
                drawerItemClick("default");

                break;
            case R.id.nav_category:
                navigationView.getMenu().clear(); //clear old inflated items.
                navigationView.removeHeaderView(navigationView.getHeaderView(0));
                navigationView.inflateMenu(R.menu.category_nav_menu);
                navigationView.setNavigationItemSelectedListener(this);
                break;
            case R.id.nav_contactUs:

                break;
            case R.id.nav_about:

                break;
            case R.id.nav_Privacy:

                break;
            case R.id.nav_BackPress:
                navigationView.getMenu().clear(); //clear old inflated items.
                navigationView.inflateMenu(R.menu.nav_menu);
                navigationView.inflateHeaderView(R.layout.nav_header_layout);
                navigationView.setNavigationItemSelectedListener(this);
                break;
            case R.id.nav_artAndCulture:
                drawerItemClick("artAndCulture");

                break;
            case R.id.nav_defenceAndSecurity:
                drawerItemClick("defenceAndSecurity");

                break;
            case R.id.nav_disasterManagement:
                drawerItemClick("disasterManagement");

                break;
            case R.id.nav_economics:
                drawerItemClick("economics");

                break;
            case R.id.nav_environment:
                drawerItemClick("environment");

                break;
            case R.id.nav_geography:
                drawerItemClick("geography");

                break;
            case R.id.nav_governanceAndSocialJustice:
                drawerItemClick("governanceAndSocialJustice");

                break;
            case R.id.nav_history:
                drawerItemClick("history");

                break;
            case R.id.nav_indianSociety:
                drawerItemClick("indianSociety");

                break;
            case R.id.nav_internationalRelations:
                drawerItemClick("internationalRelations");

                break;
            case R.id.nav_polity:
                drawerItemClick("polity");

                break;
            case R.id.nav_scienceAndTechnology:
                drawerItemClick("scienceAndTechnology");

                break;
            case R.id.nav_vividha:
                drawerItemClick("vividha");

                break;

        }

      if(item.getItemId() != R.id.nav_category) {
          drawerLayout.closeDrawer(GravityCompat.START);
      }

        return true;
    }

}
