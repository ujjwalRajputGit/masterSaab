package in.mastersaab.mastersaab.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.Objects;

import in.mastersaab.mastersaab.R;
import in.mastersaab.mastersaab.fragment.communicate.AboutFragment;
import in.mastersaab.mastersaab.fragment.communicate.ContactFragment;
import in.mastersaab.mastersaab.fragment.communicate.PrivacyPolicyFragment;

public class CommunicateActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private AboutFragment aboutFragment;
    private ContactFragment contactFragment;
    private PrivacyPolicyFragment privacyPolicyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);

        Intent intentData = getIntent();
        String fragment = intentData.getStringExtra("fragment");

        aboutFragment = new AboutFragment();
        contactFragment = new ContactFragment();
        privacyPolicyFragment = new PrivacyPolicyFragment();

        Toolbar toolbar = findViewById(R.id.communicate_toolbar);
        setSupportActionBar(toolbar);
        setToolbarTitle(fragment);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();

        chooseFragment(fragment);
    }

    private void chooseFragment(String fragment) {
        switch (fragment) {
            case "About masterSaab":
                addFragment(aboutFragment);
                break;
            case "Contact us":
                addFragment(contactFragment);
                break;
            case "Privacy Policy":
                addFragment(privacyPolicyFragment);
                break;
        }
    }

    private void addFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.communicate_fragment, fragment);
        fragmentTransaction.commit();
    }

    private void setToolbarTitle(String title_name) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title_name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.update_snakeBar);
        if (fragment != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        } else {
            super.onBackPressed();
        }
    }
}
