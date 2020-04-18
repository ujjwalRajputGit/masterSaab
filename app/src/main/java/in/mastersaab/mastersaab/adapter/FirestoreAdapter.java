package in.mastersaab.mastersaab.adapter;

import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;


import in.mastersaab.mastersaab.activity.FullScreenActivity;
import in.mastersaab.mastersaab.R;
import in.mastersaab.mastersaab.dataModel.ContentData;

public class FirestoreAdapter extends FirestorePagingAdapter<ContentData, FirestoreAdapter.ProductsViewHolder> {


    public FirestoreAdapter(@NonNull FirestorePagingOptions<ContentData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull final ContentData model) {
        Log.d("position","positin" + position);

        holder.recView_title.setText(model.getTitle());
        holder.recView_content.setText(Html.fromHtml(model.getContent()));

        final String imageUrl = model.getImageUrl();

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(holder.recView_image);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FullScreenActivity.class);
                intent.putExtra("title",model.getTitle());
                intent.putExtra("content",model.getContent());
                intent.putExtra("imageUrl",imageUrl);
                v.getContext().startActivity(intent);
            }
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
                Log.d("PAGING_LOG","Loading Next Page");
                break;
            case LOADING_INITIAL:
                Log.d("PAGING_LOG","Loading Initial Data");
                break;
            case FINISHED:
                Log.d("PAGING_LOG","All Data Loaded");
                break;
            case ERROR:
                Log.d("PAGING_LOG","ERROR Loading Data");
                break;
            case LOADED:
                Log.d("PAGING_LOG","Total Item Loaded : " + getItemCount());

                break;
        }
    }

    //View Holder
    public class ProductsViewHolder extends RecyclerView.ViewHolder {

        TextView recView_title, recView_content;
        ImageView recView_image;
        View view;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            recView_title = itemView.findViewById(R.id.title);
            recView_content = itemView.findViewById(R.id.content);
            recView_image = itemView.findViewById(R.id.imageView);
            view = itemView;
        }
    }
}
