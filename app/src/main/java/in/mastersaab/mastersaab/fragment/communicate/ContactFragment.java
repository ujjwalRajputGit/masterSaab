package in.mastersaab.mastersaab.fragment.communicate;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import in.mastersaab.mastersaab.R;


public class ContactFragment extends Fragment {

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton facebookButton = view.findViewById(R.id.facebook_button);
        ImageButton instagramButton = view.findViewById(R.id.instagram_button);
        ImageButton twitterButton = view.findViewById(R.id.twitter_button);


        facebookButton.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.facebook.com/themastersaab");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        instagramButton.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.instagram.com/themastersaab/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        twitterButton.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://twitter.com/themastersaab");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

    }
}