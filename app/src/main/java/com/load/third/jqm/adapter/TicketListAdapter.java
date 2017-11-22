package com.load.third.jqm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.load.third.jqm.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.load.third.jqm.activity.mine.TicketActivity.TYPE_TICKET_CHOSE;
import static com.load.third.jqm.activity.mine.TicketActivity.TYPE_TICKET_LIST;

public class TicketListAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private int type;

    public TicketListAdapter(Context context, List<String> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_ticket, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag( );
        }
        if (position + 1 == list.size( )) {
            holder.tvTips.setVisibility(View.VISIBLE);
        }
        switch (type) {
            case TYPE_TICKET_LIST:
                holder.ivItemSelect.setVisibility(View.GONE);
                break;
            case TYPE_TICKET_CHOSE:
                holder.ivItemSelect.setVisibility(View.VISIBLE);
                break;
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
        @BindView(R.id.tv_value)
        TextView tvValue;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.iv_item_select)
        ImageView ivItemSelect;
        @BindView(R.id.tv_tips)
        TextView tvTips;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}