package com.load.third.jqm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.load.third.jqm.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressListAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private List<String> statusStr;

    public ProgressListAdapter(Context context, List<String> list, List<String> statusStr) {
        this.context = context;
        this.list = list;
        this.statusStr = statusStr;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_borrow_progress, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag( );
        }
        if (position + 1 == list.size( )) {
            holder.line2.setVisibility(View.GONE);
        } else {
            holder.line2.setVisibility(View.VISIBLE);
        }
        holder.tvTitle.setText(list.get(position));
        holder.tvDes.setText(statusStr.get(position));
        if(statusStr.get(position).contains("待")) {
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.text_8d8d8d));
            holder.tvPosition.setText((position + 1) + "");
            holder.tvPosition.setBackgroundResource(R.drawable.bg_frame_main_color);
        }else {
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.main_color));
            holder.tvPosition.setText("");
            holder.tvPosition.setBackgroundResource(R.drawable.icon_selected);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return list.size( );
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        @BindView(R.id.line2)
        View line2;
        @BindView(R.id.tv_position)
        TextView tvPosition;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_des)
        TextView tvDes;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}