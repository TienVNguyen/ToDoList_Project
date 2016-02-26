/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.training.tiennguyen.model.ToDoElement;
import com.training.tiennguyen.model.ToDoElementHolder;
import com.training.tiennguyen.todolist.R;

import java.util.List;

/**
 * This class is adapter's functionality to set data to list.
 *
 * @author TienNguyen
 */
public class ToDoListAdapter extends BaseAdapter {
    private Activity activity;
    private List<ToDoElement> toDoElementsList;
    private LayoutInflater inflater;

    /**
     * Constructor which inflater is coming from activity's system service.
     */
    public ToDoListAdapter(Activity activity, List<ToDoElement> toDoElementsList) {
        this.activity = activity;
        this.toDoElementsList = toDoElementsList;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return toDoElementsList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return toDoElementsList.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        // Temperately
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Init Holder
        final ToDoElementHolder toDoElementHolder;

        // Check if Holder already existed
        if (convertView == null) {
            // Initial Holder
            toDoElementHolder = new ToDoElementHolder();

            // Get convertView
            convertView = inflater.inflate(R.layout.adapter_list, parent, false);

            // Set value to Holder
            toDoElementHolder.setTitleElementHolder((TextView) convertView.findViewById(R.id.txtTitleElement));
            toDoElementHolder.setDetailsElementHolder((TextView) convertView.findViewById(R.id.txtDetailsElement));
            toDoElementHolder.setPriorityElementHolder((TextView) convertView.findViewById(R.id.txtPriorityElement));

            // Set tag for later view
            convertView.setTag(toDoElementHolder);
        } else {
            // Holder existed.
            toDoElementHolder = (ToDoElementHolder) convertView.getTag();
        }

        // Set title for Holder
        toDoElementHolder.getTitleElementHolder().setText(toDoElementsList.get(position).getTitle());

        // Set details for Holder
        // If details are over 20 characters, it will be trimmed.
        String limitedDetails = toDoElementsList.get(position).getDetails();
        if (limitedDetails.length() > 20) {
            limitedDetails = limitedDetails.substring(0, 20) + "...";
            TextView detailsElementObject = (TextView) convertView.findViewById(R.id.txtDetailsElement);

        }
        toDoElementHolder.getDetailsElementHolder().setText(limitedDetails);

        // Set priority for Holder
        // The priority will be print with color
        // TODO: Temperately use the String directly
        String priorityColor;
        String priorityText;
        switch (toDoElementsList.get(position).getPriority()) {
            case 1:
                // Medium
                priorityColor = "#CDFF3F";
                priorityText = "Medium";
                break;
            case 2:
                // Low
                priorityColor = "#0000FF";
                priorityText = "Low";
                break;
            default:
                // High
                priorityColor = "#FF0909";
                priorityText = "High";
                break;
        }
        toDoElementHolder.getPriorityElementHolder().setText(priorityText);
        TextView priorityElementObject = (TextView) convertView.findViewById(R.id.txtPriorityElement);
        priorityElementObject.setTextColor(Color.parseColor(priorityColor));

        // Return the convertView
        return convertView;
    }
}
