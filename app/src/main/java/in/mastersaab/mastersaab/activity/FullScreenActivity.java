package in.mastersaab.mastersaab.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.Objects;

import in.mastersaab.mastersaab.R;

public class FullScreenActivity extends AppCompatActivity {

    private TextView title;
    private  TextView date;
    private HtmlTextView htmlTextView;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_srcreen);

        // Initialize the InterstitialAd
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        Toolbar toolbar = findViewById(R.id.fullscreen_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent data = getIntent();

        title = findViewById(R.id.fullscreen_title);
        date = findViewById(R.id.fullscreen_date);
        htmlTextView = findViewById(R.id.fullscreen_htmlView);
        ImageView imageView = findViewById(R.id.fullscreen_imageView);


        title.setText(data.getStringExtra("title"));
        date.setText(data.getStringExtra("date"));
        htmlTextView.setHtml(data.getStringExtra("content"),new HtmlHttpImageGetter(htmlTextView));
        String imageUrl = data.getStringExtra("imageUrl");

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(imageView);

        loadInterstitialAd();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.fullscreen_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        switch (item.getItemId()) {
            case R.id.zoomIn:
                float titleTextSize = pixelsToSp(this, title.getTextSize());
                float contentTextSize = pixelsToSp(this, htmlTextView.getTextSize());
                float dateTextSize = pixelsToSp(this,date.getTextSize());
                if (titleTextSize < 30f) {
                    title.setTextSize(titleTextSize + 2f);
                    date.setTextSize(dateTextSize + 2f);
                    htmlTextView.setTextSize(contentTextSize + 2f);
                }
                break;
            case R.id.zoomOut:
                titleTextSize = pixelsToSp(this, title.getTextSize());
                contentTextSize = pixelsToSp(this, htmlTextView.getTextSize());
                dateTextSize = pixelsToSp(this,date.getTextSize());

                if (titleTextSize > 12f) {
                    title.setTextSize(titleTextSize - 2f);
                    date.setTextSize(dateTextSize - 2f);
                    htmlTextView.setTextSize(contentTextSize - 2f);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    public void loadInterstitialAd() {
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onBackPressed() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            super.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }

}
