package in.mastersaab.mastersaab.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import in.mastersaab.mastersaab.R;
import in.mastersaab.mastersaab.adapter.FirestoreAdapter;
import in.mastersaab.mastersaab.dataModel.ContentData;

public class LatestFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private String collection = "default";


    public LatestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_latest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.latest_recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setQuery();

    }

    private void fetchData(Query query) {
        Log.d("firebaseInstance", firebaseFirestore + " F1");

        //Pagination
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(5)
                .setPageSize(3)
                .build();

        //RecyclerOption
        FirestorePagingOptions<ContentData> options = new FirestorePagingOptions.Builder<ContentData>()
                .setLifecycleOwner(this)
                .setQuery(query, config, ContentData.class)
                .build();

        FirestoreAdapter firestorePagingAdapter = new FirestoreAdapter(options);
        recyclerView.setAdapter(firestorePagingAdapter);
    }

    private void setQuery() {

        Bundle bundle = getArguments();
        if (getArguments() != null) {
            collection = bundle.getString("collection");
            Log.d("collection", collection);
        }
        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query;
        switch (collection) {
            case "artAndCulture":
                query = firebaseFirestore.collection("Content")
                        .whereEqualTo("category","Art And Culture")
                        .orderBy("date", Query.Direction.DESCENDING);
                break;
            case "defenceAndSecurity":
                query = firebaseFirestore.collection("Content")
                        .whereEqualTo("category","Defence And Security")
                        .orderBy("date", Query.Direction.DESCENDING);
                break;
            case "disasterManagement":
                query = firebaseFirestore.collection("Content")
                        .whereEqualTo("category","Disaster Management")
                        .orderBy("date", Query.Direction.DESCENDING);
                break;
            case "economics":
                query = firebaseFirestore.collection("Content")
                        .whereEqualTo("category","Economics")
                        .orderBy("date", Query.Direction.DESCENDING);
                break;
            case "environment":
                query = firebaseFirestore.collection("Content")
                        .whereEqualTo("category","Environment")
                        .orderBy("date", Query.Direction.DESCENDING);
                break;
            case "geography":
                query = firebaseFirestore.collection("Content")
                        .whereEqualTo("category","Geography")
                        .orderBy("date", Query.Direction.DESCENDING);
                break;
            case "governanceAndSocialJustice":
                query = firebaseFirestore.collection("Content")
                        .whereEqualTo("category","Governance And Social Justice")
                        .orderBy("date", Query.Direction.DESCENDING);
                break;
            case "indianSociety":
                query = firebaseFirestore.collection("Content")
                        .whereEqualTo("category","IndianSociety")
                        .orderBy("date", Query.Direction.DESCENDING);
                break;
            case "internationalRelations":
                query = firebaseFirestore.collection("Content")
                        .whereEqualTo("category","International Relations")
                        .orderBy("date", Query.Direction.DESCENDING);
                break;
            case "polity":
                query = firebaseFirestore.collection("Content")
                        .whereEqualTo("category","Polity")
                        .orderBy("date", Query.Direction.DESCENDING);
                break;
            case "scienceAndTechnology":
                query = firebaseFirestore.collection("Content")
                        .whereEqualTo("category","Science And Technology")
                        .orderBy("date", Query.Direction.DESCENDING);
                break;
            case "miscellaneous":
                query = firebaseFirestore.collection("Content")
                        .whereEqualTo("category","Miscellaneous")
                        .orderBy("date", Query.Direction.DESCENDING);
                break;
            default:
                query = firebaseFirestore.collection("Content")
                        .orderBy("date", Query.Direction.DESCENDING);
        }

        fetchData(query);
    }

}