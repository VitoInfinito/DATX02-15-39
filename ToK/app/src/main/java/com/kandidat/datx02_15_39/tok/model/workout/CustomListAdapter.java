package com.kandidat.datx02_15_39.tok.model.workout;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kandidat.datx02_15_39.tok.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emma on 15-04-13.
 */


public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> itemname;
    private final Integer[] imgid;

    public CustomListAdapter(Activity context, List<String> itemname, Integer[] imgid) {
        super(context, R.layout.workout_list_item, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    @Override
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();

        View rowView=inflater.inflate(R.layout.workout_list_item, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.workout_info);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.workout_image);

        txtTitle.setText(itemname.get(position));
        String tmp =itemname.get(position);
        switch(tmp.substring(0,2)){
            case"ST":
                imageView.setImageResource(R.drawable.strength);

                break;
            case"KO":
                imageView.setImageResource(R.drawable.sprint);
                break;
            case"FL":
                imageView.setImageResource(R.drawable.yoga_icon);
                break;
            case"SP":
                imageView.setImageResource(R.drawable.soccer);
                break;
            default:
                imageView.setImageResource(R.drawable.soccer);
                break;
        }
        return rowView;

    }
}