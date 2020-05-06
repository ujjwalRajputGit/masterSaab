package in.mastersaab.mastersaab.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;


import java.text.DateFormat;
import java.text.SimpleDateFormat;

import in.mastersaab.mastersaab.activity.FullScreenActivity;
import in.mastersaab.mastersaab.R;
import in.mastersaab.mastersaab.activity.MainActivity;
import in.mastersaab.mastersaab.dataModel.ContentData;

public class FirestoreAdapter extends FirestorePagingAdapter<ContentData, FirestoreAdapter.ProductsViewHolder> {


    public FirestoreAdapter(@NonNull FirestorePagingOptions<ContentData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull final ContentData model) {

        holder.recView_title.setText(model.getTitle());
//        holder.recView_content.setText(Html.fromHtml(model.getContent()).toString());
        holder.recView_content.setText(model.getContent().replaceAll("<.*?>",""));
        //date formatting
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String date = dateFormat.format(model.getDate());
        holder.recView_date.setText(date);

        holder.recView_document_id.setText(model.getDocument_id());

        final String imageUrl = model.getImageUrl();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(holder.recView_image);

        holder.view.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FullScreenActivity.class);
            intent.putExtra("title",model.getTitle());
            intent.putExtra("content",model.getContent());
            intent.putExtra("imageUrl",imageUrl);
            intent.putExtra("date",date);
            intent.putExtra("document id",model.getDocument_id());
            v.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.recycler_view_layout, parent,false);
        return new ProductsViewHolder(view);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
        switch (state) {
            case LOADING_MORE:
                MainActivity.progressMore("enable");
                Log.d("PAGING_LOG","Loading Next Page");
                break;
            case LOADING_INITIAL:
                MainActivity.progressInitial("enable");
                Log.d("PAGING_LOG","Loading Initial Data");
                break;
            case FINISHED:
                MainActivity.progressMore("disable");
                MainActivity.progressInitial("disable");
                Log.d("PAGING_LOG","All Data Loaded");
                break;
            case ERROR:
                Log.d("PAGING_LOG","ERROR Loading Data");
                break;
            case LOADED:
                MainActivity.progressInitial("disable");
                MainActivity.progressMore("disable");
                Log.d("PAGING_LOG","Total Item Loaded : " + getItemCount());
                break;
        }
    }

    //View Holder
    public class ProductsViewHolder extends RecyclerView.ViewHolder {

        TextView recView_title, recView_content, recView_date, recView_document_id;
        ImageView recView_image;

        View view;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            recView_title = itemView.findViewById(R.id.title);
            recView_content = itemView.findViewById(R.id.content);
            recView_image = itemView.findViewById(R.id.imageView);
            recView_date = itemView.findViewById(R.id.date);
            recView_document_id = itemView.findViewById(R.id.document_id);
            view = itemView;
        }
    }
}
