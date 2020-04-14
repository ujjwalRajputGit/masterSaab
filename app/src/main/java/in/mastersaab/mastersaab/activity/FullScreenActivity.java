package in.mastersaab.mastersaab.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Objects;

import in.mastersaab.mastersaab.R;

public class FullScreenActivity extends AppCompatActivity {

    TextView title;
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_srcreen);

        Toolbar toolbar = findViewById(R.id.fullscreen_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent data = getIntent();

        title = findViewById(R.id.fullscreen_title);
        content = findViewById(R.id.fullscreen_content);
        ImageView imageView = findViewById(R.id.fullscreen_imageView);

        Spanned contentText = Html.fromHtml(data.getStringExtra("content"));

        title.setText(data.getStringExtra("title"));
        content.setText(contentText);
        content.setMovementMethod(LinkMovementMethod.getInstance());
        String imageUrl = data.getStringExtra("imageUrl");

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(imageView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.fullscreen_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        switch (item.getItemId()){
            case R.id.zoomIn:
                float titleTextSize = pixelsToSp(this,title.getTextSize());
                float contentTextSize = pixelsToSp(this,content.getTextSize());
                if(titleTextSize < 30f) {
                    title.setTextSize(titleTextSize + 2f);
                    content.setTextSize(contentTextSize + 2f);
                }
                break;
            case R.id.zoomOut:
                titleTextSize = pixelsToSp(this, title.getTextSize());
                contentTextSize = pixelsToSp(this, content.getTextSize());
                if(titleTextSize > 12f) {
                    title.setTextSize(titleTextSize - 2f);
                    content.setTextSize(contentTextSize - 2f);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

}
