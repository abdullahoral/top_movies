package com.example.android.topmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 06-Mar-18.
 */

public class RvReviewsAdapter extends RecyclerView.Adapter<RvReviewsAdapter.RvReviewsItemViewHolder> {

    private Context mContext;
    private List<RvReviewsItem> mRvReviewsItemList;

    public RvReviewsAdapter(Context context, List<RvReviewsItem> rvReviewsItemList) {
        mContext = context;
        mRvReviewsItemList = rvReviewsItemList;
    }

    @NonNull
    @Override
    public RvReviewsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.rv_reviews_item, parent, false);
        return new RvReviewsItemViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RvReviewsItemViewHolder holder, int position) {

        RvReviewsItem currentRvReviewsItem = mRvReviewsItemList.get(position);

        String reviewAuthor = currentRvReviewsItem.getmReviewAuthor();
        String reviewContent = currentRvReviewsItem.getmReviewContent();

        holder.mReviewAuthor.setText(reviewAuthor + " : ");
        holder.mReviewContent.setText(reviewContent);

    }

    @Override
    public int getItemCount() {
        return mRvReviewsItemList.size();
    }

    public class RvReviewsItemViewHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.review_author) TextView mReviewAuthor;
        @BindView(R.id.review_content) TextView mReviewContent;


        public RvReviewsItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
