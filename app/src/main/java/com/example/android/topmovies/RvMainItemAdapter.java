package com.example.android.topmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by User on 06-Mar-18.
 */

public class RvMainItemAdapter extends RecyclerView.Adapter<RvMainItemAdapter.RvMainItemViewHolder> {

    private Context mContext;
    private ArrayList<RvMainItem> mRvMainItemList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener  {
        void onItemClick  (int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        mListener = listener;
    }

    public RvMainItemAdapter (Context context, ArrayList<RvMainItem> rvMainItemList) {
        mContext = context;
        mRvMainItemList = rvMainItemList;
    }

    @NonNull
    @Override
    public RvMainItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.rv_main_item, parent, false);
        return new RvMainItemViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RvMainItemViewHolder holder, int position) {

        RvMainItem currentRvMainItem = mRvMainItemList.get(position);

        String imageUrl = currentRvMainItem.getmImageUrl();

        Picasso.with(mContext)
                .load(imageUrl)
                .into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return mRvMainItemList.size();
    }

    public class RvMainItemViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;

        public RvMainItemViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_main);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
