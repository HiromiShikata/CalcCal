package com.imorih.calccal;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by hiromi on 16/12/25.
 */

public class CfListAdapter extends ArrayAdapter<CfRow> {

    final int baseTextColor;
    final int minusTextColor;
    final LayoutInflater inflator;

    public CfListAdapter(Context context) {
        super(context, R.layout.cf_row);
        inflator = LayoutInflater.from(context);
        baseTextColor = ContextCompat.getColor(context, R.color.baseText);
        minusTextColor = ContextCompat.getColor(context, R.color.minusTextColor);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.cf_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CfRow item = getItem(position);
        holder.date.setText(item.getYmd());
        holder.title.setText(item.event.getTitle());
        holder.in.setText(String.valueOf(item.getIn()));
        holder.out.setText(String.valueOf(item.getOut()));
        holder.diff.setText(String.valueOf(item.total));

        final Uri uri = Uri.parse("content://com.android.calendar/events/" + item.event.getEventId());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                getContext().startActivity(i);
            }
        });

        final int diffColor;
        if (item.total < 0) diffColor = minusTextColor;
        else diffColor = baseTextColor;
        holder.diff.setTextColor(diffColor);

        return convertView;

    }

    class ViewHolder {
        TextView date;
        TextView title;
        TextView in;
        TextView out;
        TextView diff;

        ViewHolder(View view) {
            date = (TextView) view.findViewById(R.id.ch_row_date);
            title = (TextView) view.findViewById(R.id.ch_row_title);
            in = (TextView) view.findViewById(R.id.ch_row_in);
            out = (TextView) view.findViewById(R.id.ch_row_out);
            diff = (TextView) view.findViewById(R.id.ch_row_diff);
        }
    }
}
