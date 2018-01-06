package com.rolodex.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.rolodex.models.Robot;
import com.rolodex.rolodex.R;

import java.util.List;

/**
 * Created by mauli on 1/5/2018.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Context mContext;
    private List<Robot> robotList;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public CardAdapter(Context context, List<Robot> robotList) {
        mContext = context;
        this.robotList = robotList;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.row_card_view, parent, false);
        ViewHolder listHolder = new ViewHolder(mainGroup);
        return listHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Robot model = robotList.get(position);

        ViewHolder viewHolder = (ViewHolder) holder;// holder

       /* Bitmap image = BitmapFactory.decodeResource(mContext.getResources(),
                model.getImage());// This will convert drawbale image into*/
        // setting title
        viewHolder.title.setText(model.getCompany());

        imageLoader.displayImage(robotList.get(position).getAvatar(), viewHolder.imageview, options);
        // new ImageAsync.DownloadImageTask(viewHolder.imageview).execute(robotList.get(position).getAvatar());
    }

    @Override
    public int getItemCount() {
        return ((robotList != null) ? robotList.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        private TextView title;
        private ImageView imageview;

        public ViewHolder(View v) {
            super(v);
            this.title = (TextView) v
                    .findViewById(R.id.tv_company_name);
            this.imageview = (ImageView) v
                    .findViewById(R.id.ivAvatar);
        }
    }

}
