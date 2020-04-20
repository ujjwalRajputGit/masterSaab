package in.mastersaab.mastersaab.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import in.mastersaab.mastersaab.R;
import in.mastersaab.mastersaab.adapter.FirestoreAdapter;
import in.mastersaab.mastersaab.adapter.ViewPagerAdapter;
import in.mastersaab.mastersaab.fragment.LatestFragment;
import in.mastersaab.mastersaab.fragment.TrendingFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String previousData ="default";

    private AdView adView;

    private TrendingFragment trendingFragment = new TrendingFragment();
    private LatestFragment latestFragment = new LatestFragment();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        //initialising Banner Ad
        FrameLayout adContainerView = findViewById(R.id.ad_view_container);
        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        adContainerView.addView(adView);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        //adding the toolbar
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);


        //tabLayout setup with viewPager
        tabLayout.setupWithViewPager(viewPager);

        //setting viewPager Adapter
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        //adding fragment to viewPager Adapter start
        viewPagerAdapter.addFragment(latestFragment, "Latest");
        viewPagerAdapter.addFragment(trendingFragment, "Trending");

        //adding viewPager Adapter
        viewPager.setAdapter(viewPagerAdapter);

        //adding icon to tab layout start
//        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_new_icon_black);
//        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_trending_icon_black);


        //navigation drawer start
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_home);

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

        //loading Ad
        loadBanner();

    }

    private void loadBanner() {
        AdRequest adRequest =
                new AdRequest.Builder().build();

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    public void drawerItemClick(String drawer_item,String title_name) {
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
            Objects.requireNonNull(getSupportActionBar()).setTitle(title_name);
        }
    }

    //navigation drawer item click method
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        switch (item.getItemId()) {

            case R.id.nav_home:
                drawerItemClick("default","masterSaab");

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
                drawerItemClick("artAndCulture","Art & Culture");

                break;
            case R.id.nav_defenceAndSecurity:
                drawerItemClick("defenceAndSecurity", "Defence & Security");

                break;
            case R.id.nav_disasterManagement:
                drawerItemClick("disasterManagement", "Disaster Management");

                break;
            case R.id.nav_economics:
                drawerItemClick("economics", "Economics");

                break;
            case R.id.nav_environment:
                drawerItemClick("environment", "Environment");

                break;
            case R.id.nav_geography:
                drawerItemClick("geography", "Geography");

                break;
            case R.id.nav_governanceAndSocialJustice:
                drawerItemClick("governanceAndSocialJustice", "Governance & SocialJustice");

                break;
            case R.id.nav_indianSociety:
                drawerItemClick("indianSociety", "Indian Society");

                break;
            case R.id.nav_internationalRelations:
                drawerItemClick("internationalRelations", "International Relations");

                break;
            case R.id.nav_polity:
                drawerItemClick("polity", "polity");

                break;
            case R.id.nav_scienceAndTechnology:
                drawerItemClick("scienceAndTechnology", "Science & Technology");

                break;
            case R.id.nav_miscellaneous:
                drawerItemClick("miscellaneous", "Miscellaneous");

                break;

        }

      if(item.getItemId() != R.id.nav_category && item.getItemId() != R.id.nav_BackPress) {
          drawerLayout.closeDrawer(GravityCompat.START);
      }

        return true;
    }

}
