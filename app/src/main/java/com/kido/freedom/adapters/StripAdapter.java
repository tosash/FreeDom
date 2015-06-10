package com.kido.freedom.adapters;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kido.freedom.R;
import com.kido.freedom.model.Strip;
import com.kido.freedom.ui.FragmentNews;
import com.kido.freedom.utils.CircularNetworkImageView;
import com.kido.freedom.utils.Utils;
import com.kido.freedom.utils.VolleySingleton;

import java.util.List;

public class StripAdapter extends RecyclerView.Adapter<StripAdapter.StripViewHolder> {
    public long currID;
    private List<Strip> mItems;
    private FragmentManager fm;

    public StripAdapter(FragmentManager f, List<Strip> strips) {
        fm = f;
        mItems = strips;
    }

    @Override
    public StripViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.strip_cardview_row, viewGroup, false);
        StripViewHolder sviewHolder = new StripViewHolder(v);
        return sviewHolder;
    }

    @Override
    public void onBindViewHolder(StripViewHolder stripViewHolder, int i) {
        Strip strip = mItems.get(i);
        stripViewHolder.txtName.setText(strip.getName());
        stripViewHolder.txtDesc.setText(strip.getDesc());
        stripViewHolder.txtDate.setText(Utils.getStrDateFromJsonDate(strip.getDate()));
        stripViewHolder.imgThumbnail.setCircled(false);
        stripViewHolder.imgThumbnail.setImageUrl(strip.getUrl(), VolleySingleton.getInstance(null).getImageLoader());
        if (strip.isStripNew()) {
            stripViewHolder.imgNew.setVisibility(View.VISIBLE);
        } else {
            stripViewHolder.imgNew.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (mItems == null) {
            return 0;
        } else {
            return mItems.size();
        }
    }

    public Strip getItem(int position)
    {
        return mItems.get(position);
    }
    @Override
    public long getItemId(int position) {
        return mItems.get(position).getId();
    }

    class StripViewHolder extends RecyclerView.ViewHolder {

        protected CircularNetworkImageView imgThumbnail;
        protected ImageView imgNew;
        protected TextView txtName;
        protected TextView txtDesc;
        protected TextView txtDate;

        public StripViewHolder(final View itemView) {
            super(itemView);
            imgThumbnail = (CircularNetworkImageView) itemView.findViewById(R.id.img_thumbnail);
            imgNew = (ImageView) itemView.findViewById(R.id.img_new);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtDesc = (TextView) itemView.findViewById(R.id.txt_desc);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Bundle bundle = new Bundle();
                        int pos=getLayoutPosition();
                        bundle.putLong("NewsId", getItem(pos).getId());
                        FragmentNews fragNews = new FragmentNews();
                        fragNews.setArguments(bundle);
                        fm.beginTransaction()
//                                .add(R.id.container, fragNews)
                                .replace(R.id.container, fragNews)
                                .addToBackStack(null)
                                .commit();
                        ;
                    } catch (Exception e) {
                        Log.e("News", e.toString());
                    }
//                    Intent intent = new Intent(v.getContext(), SecondPage.class);
//                    v.getContext().startActivity(intent);
//                    Toast.makeText(v.getContext(), "os version is: " + v.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
