package com.elec_coen_390.uvme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {


    private Context context;
    private List<String> questionlist;
    private HashMap<String, List<String>> answerslist;

    public ExpandableListViewAdapter(Context context, List<String> questionlist, HashMap<String, List<String>> answerslist){

        this.context = context;
        this.questionlist = questionlist;
        this.answerslist = answerslist;
    }

    @Override
    public int getGroupCount() {
        return this.questionlist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.answerslist.get(this.questionlist.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.questionlist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.answerslist.get(this.questionlist.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String questionTitle = (String) getGroup(groupPosition);

        if (convertView == null){

            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.question_list, null);

        }

        TextView questiontv = convertView.findViewById(R.id.question_tv);
        questiontv.setText(questionTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        String answersTitle = (String) getChild(groupPosition, childPosition);

        if (convertView == null){

            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.answers_list, null);

        }

        TextView answerstv = convertView.findViewById(R.id.answer_tv);
        answerstv.setText(answersTitle);

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
