package com.example.test;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NotiAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Noti> notiList;

    public NotiAdapter(Context context, int layout, List<Noti> notiList) {
        this.context = context;
        this.layout = layout;
        this.notiList = notiList;
    }

    @Override
    public int getCount() {
        return notiList.size();
    }

    @Override
    public Object getItem(int i) {
        return notiList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if ( view == null ) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder = new ViewHolder();

            holder.photo = (ImageView) view.findViewById(R.id.photo);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.pubdate = (TextView) view.findViewById(R.id.pubdate);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Noti noti = notiList.get(i);
        holder.title.setText(noti.getTitle());
        holder.pubdate.setText(noti.getPubdate());

        Picasso.with(context).load(noti.getPhoto_url())
            .placeholder(android.R.drawable.ic_menu_camera)
            .error(android.R.drawable.presence_away)
            .into(holder.photo);

        return view;
    }

    private class ViewHolder {
        ImageView photo;
        TextView title;
        //TextView description;
        TextView pubdate;
    }
}
