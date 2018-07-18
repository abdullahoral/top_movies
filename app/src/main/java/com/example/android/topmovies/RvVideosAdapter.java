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

public class RvVideosAdapter extends RecyclerView.Adapter<RvVideosAdapter.RvVideosItemViewHolder> {

    private Context mContext;
    private List<RvVideosItem> mRvVideosItemList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener  {
        void onItemClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        mListener = listener;
    }

    public RvVideosAdapter(Context context, List<RvVideosItem> rvVideosItemList) {
        mContext = context;
        mRvVideosItemList = rvVideosItemList;
    }

    @NonNull
    @Override
    public RvVideosItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.rv_videos_item, parent, false);
        return new RvVideosItemViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RvVideosItemViewHolder holder, int position) {

        RvVideosItem currentRvVideosItem = mRvVideosItemList.get(position);

        String videoName = currentRvVideosItem.getmVideoName();
        holder.mVideoName.setText(videoName);


    }

    @Override
    public int getItemCount() {
        return mRvVideosItemList.size();
    }

    public class RvVideosItemViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.video_name) TextView mVideoName;


        public RvVideosItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

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
