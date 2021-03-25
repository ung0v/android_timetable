package com.ulan.timetable.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ulan.timetable.R;
import com.ulan.timetable.model.Mark;

import java.util.List;

public class MarkAdapter extends BaseAdapter {
    private Context mContext;
    private List<Mark> markList;



    public MarkAdapter(Context mContext, List<Mark> markList) {
        this.mContext = mContext;
        this.markList = markList;
    }

    @Override
    public int getCount() {
        return markList.size();
    }

    @Override
    public Object getItem(int i) {
        return markList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public static class Holder {
        public TextView s_name, diligence, credits, mid_term, end_term, grade;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Holder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.mark_layout, null);
            holder = new Holder();
            holder.s_name = convertView.findViewById(R.id.s_name);
            holder.credits = convertView.findViewById(R.id.credits);
            holder.diligence = convertView.findViewById(R.id.diligence);
            holder.mid_term = convertView.findViewById(R.id.mid_term);
            holder.end_term = convertView.findViewById(R.id.end_term);
            holder.grade = convertView.findViewById(R.id.grade);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        
        holder.s_name.setText(markList.get(position).getS_name());
        holder.credits.setText(markList.get(position).getCredits());
        holder.diligence.setText(markList.get(position).getDiligence());
        holder.mid_term.setText(markList.get(position).getMid_term());
        holder.end_term.setText(markList.get(position).getEnd_term());
        holder.grade.setText(markList.get(position).getGrade());
        return convertView;
    }
}
