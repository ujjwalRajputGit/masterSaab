package in.mastersaab.mastersaab.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import in.mastersaab.mastersaab.R;
import in.mastersaab.mastersaab.adapter.ViewPagerAdapter;
import in.mastersaab.mastersaab.fragment.LatestFragment;
import in.mastersaab.mastersaab.fragment.TrendingFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBarInitial;
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBarMore;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String previousData ="default";

    private TrendingFragment trendingFragment = new TrendingFragment();
    private LatestFragment latestFragment = new LatestFragment();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBarInitial = findViewById(R.id.progressBar_initial);
        progressBarMore = findViewById(R.id.progressBar_more);


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
            case R.id.nav_about:
                Intent intent = new Intent(this, CommunicateActivity.class);
                intent.putExtra("fragment", "About masterSaab");
                startActivity(intent);
                break;
            case R.id.nav_contact:
                intent = new Intent(this, CommunicateActivity.class);
                intent.putExtra("fragment", "Contact us");
                startActivity(intent);
                break;
            case R.id.nav_Privacy_Policy:
                intent = new Intent(this, CommunicateActivity.class);
                intent.putExtra("fragment", "Privacy Policy");
                startActivity(intent);
                break;
            case R.id.nav_rate_app:
                Uri uri = Uri.parse(getString(R.string.play_store_link) + getPackageName());
                Intent rateIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(rateIntent);
                break;
            case R.id.nav_share_app:
                Uri uriToImage = Uri.parse("android.resource://"
                        + getPackageName()
                        +"/drawable/share_image");

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);

                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.play_store_link)
                        + getPackageName());
                shareIntent.setType("text/plain");

                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setType("image/jpg");

                startActivity(Intent.createChooser(shareIntent, "Share with"));
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
            case R.id.nav_history:
                drawerItemClick("history", "History");

                break;
            case R.id.nav_indianSociety:
                drawerItemClick("indianSociety", "Indian Society");

                break;
            case R.id.nav_internationalRelations:
                drawerItemClick("internationalRelations", "International Relations");

                break;
            case R.id.nav_miscellaneous:
                drawerItemClick("miscellaneous", "Miscellaneous");

                break;
            case R.id.nav_polity:
                drawerItemClick("polity", "polity");

                break;
            case R.id.nav_scienceAndTechnology:
                drawerItemClick("scienceAndTechnology", "Science & Technology");

                break;

        }

      if(item.getItemId() != R.id.nav_category && item.getItemId() != R.id.nav_BackPress) {
          drawerLayout.closeDrawer(GravityCompat.START);
      }

        return true;
    }

    public static void progressInitial(String state) {
        switch (state) {
            case "enable":
                progressBarInitial.setVisibility(View.VISIBLE);
                break;
            case "disable":
                progressBarInitial.setVisibility(View.GONE);
                break;
        }
    }

    public static void progressMore(String state) {
        switch (state) {
            case "enable":
                progressBarMore.setVisibility(View.VISIBLE);
                break;
            case "disable":
                progressBarMore.setVisibility(View.GONE);
                break;
        }
    }

}
