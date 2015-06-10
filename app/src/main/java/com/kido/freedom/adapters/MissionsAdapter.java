package com.kido.freedom.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kido.freedom.R;
import com.kido.freedom.model.Mission;

import java.util.List;

public class MissionsAdapter extends RecyclerView.Adapter<MissionsAdapter.MissionsViewHolder> {
    private List<Mission> mItems;

    public MissionsAdapter( List<Mission> missions) {
        mItems = missions;
    }

    @Override
    public MissionsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mission_cardview, viewGroup, false);
        MissionsViewHolder sviewHolder = new MissionsViewHolder(v);
        return sviewHolder;
    }

    @Override
    public void onBindViewHolder(MissionsViewHolder missionsViewHolder, int i) {
        Mission mission = mItems.get(i);
        missionsViewHolder.txtName.setText("Миссия №"+Long.toString(mission.getmNumber()));
        missionsViewHolder.txtPoints.setText(Long.toString(mission.getmPoints()));
    }

    @Override
    public int getItemCount() {
        if (mItems == null) {
            return 0;
        } else {
            return mItems.size();
        }
    }

    public Mission getItem(int position)
    {
        return mItems.get(position);
    }
    @Override
    public long getItemId(int position) {
        return mItems.get(position).getmNumber();
    }

    class MissionsViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imgCoins;
        protected TextView txtName;
        protected TextView txtPoints;
        protected RecyclerView tasksList;

        public MissionsViewHolder(final View itemView) {
            super(itemView);
            imgCoins = (ImageView) itemView.findViewById(R.id.img_coins);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtPoints = (TextView) itemView.findViewById(R.id.txt_points);
            tasksList=(RecyclerView)itemView.findViewById(R.id.tasksList);
        }
    }

}
