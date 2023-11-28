package com.example.mongtron_t.adpater;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mongtron_t.R;

import java.util.ArrayList;
import java.util.List;

public class NationalityListViewAdapter extends BaseAdapter {
    private final List<Pair<String, Integer>> nationalityList;

    public List<Pair<String, Integer>> getNationalityList(){
        return this.nationalityList;
    }

    public NationalityListViewAdapter(){
        this.nationalityList = new ArrayList<>();
        nationalityList.add(new Pair<>("Korea", R.drawable.korea_img));
        nationalityList.add(new Pair<>("Japan", R.drawable.japan_img));
        nationalityList.add(new Pair<>("USA", R.drawable.usa_img));
        nationalityList.add(new Pair<>("UK", R.drawable.uk_img));
        nationalityList.add(new Pair<>("Germany", R.drawable.germany_img));
        nationalityList.add(new Pair<>("Russia", R.drawable.russia_img));
        nationalityList.add(new Pair<>("Turkey", R.drawable.turkey_img));
        nationalityList.add(new Pair<>("China", R.drawable.china_img));
    }

    @Override
    public int getCount() {
        return nationalityList.size();
    }

    @Override
    public Object getItem(int position) {
        return nationalityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        Pair<String, Integer> nationality = nationalityList.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.nationality_listview_item, parent, false);
        }

        ImageView listViewItemImageView = convertView.findViewById(R.id.listViewItemImageView);
        TextView listViewItemTextView = convertView.findViewById(R.id.listViewItemTextView);

        String nationalityName = nationality.first;
        int nationalityImg = nationality.second;

        listViewItemImageView.setImageResource(nationalityImg);
        listViewItemTextView.setText(nationalityName);

        return convertView;
    }
}
