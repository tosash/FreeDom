package com.kido.freedom.adapters;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kido.freedom.R;
import com.kido.freedom.model.MissionTaskStatus;

import java.util.List;

public class MissionsTaskAdapter extends RecyclerView.Adapter<MissionsTaskAdapter.MissionsTaskViewHolder> {
    private List<MissionTaskStatus> mItems;

    public MissionsTaskAdapter(List<MissionTaskStatus> missionTasks) {
        mItems = missionTasks;
    }

    @Override
    public MissionsTaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mission_task_row, viewGroup, false);
        return new MissionsTaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MissionsTaskViewHolder missionsTaskViewHolder, int i) {
        MissionTaskStatus mission = mItems.get(i);
        missionsTaskViewHolder.txtName.setText(mission.getmTask());
        if (mission.getmComplete()){
            missionsTaskViewHolder.imgCheck.setImageResource(R.drawable.checkbox);
            missionsTaskViewHolder.txtName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            missionsTaskViewHolder.imgCheck.setImageResource(R.drawable.checkbox_no);
        }
        missionsTaskViewHolder.crdTask.setCardBackgroundColor(Color.parseColor("#4A6472"));
    }

    @Override
    public int getItemCount() {
        if (mItems == null) {
            return 0;
        } else {
            return mItems.size();
        }
    }

    public MissionTaskStatus getItem(int position)
    {
        return mItems.get(position);
    }

    class MissionsTaskViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imgCheck;
        protected TextView txtName;
        protected CardView crdTask;

        public MissionsTaskViewHolder(final View itemView) {
            super(itemView);
            imgCheck = (ImageView) itemView.findViewById(R.id.img_new);
            txtName = (TextView) itemView.findViewById(R.id.txt_desc);
            crdTask = (CardView) itemView.findViewById(R.id.crd_task);
        }
    }

}
