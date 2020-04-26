package in.mastersaab.mastersaab.activity;

import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

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

    private static final int MY_REQUEST_CODE = 2020;
    private AppUpdateManager appUpdateManager;
    private Task<AppUpdateInfo> appUpdateInfoTask;

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

        progressBarInitial = findViewById(R.id.progressBar_initial);
        progressBarMore = findViewById(R.id.progressBar_more);

        MobileAds.initialize(this, initializationStatus -> {

        });

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

        //app update
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateChecker();

    }

    private void appUpdateChecker() {
        appUpdateManager.registerListener(installStateUpdatedListener);
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (appUpdateInfo.clientVersionStalenessDays() > 5 &&
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                                AppUpdateType.IMMEDIATE,
                                this,
                                MY_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }

                } else if (appUpdateInfo.clientVersionStalenessDays() != null &&
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                                AppUpdateType.FLEXIBLE,
                                this,
                                MY_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED){
                        popupSnackBarForCompleteUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED){
                        if (appUpdateManager != null){
                            appUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    }
                }
            };

    private void popupSnackBarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.update_snakeBar),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(
                getResources().getColor(R.color.secondaryColor));
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(appUpdateInfo -> {
                    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackBarForCompleteUpdate();
                    }
                    else if (appUpdateInfo.updateAvailability()
                            == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo,
                                    AppUpdateType.FLEXIBLE,
                                    this,
                                    MY_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
