/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.model;

import android.widget.TextView;

/**
 * This class contains the holder of ToDoElement.
 *
 * @author TienNguyen
 */
public class ToDoElementHolder {
    private TextView titleElementHolder;
    private TextView detailsElementHolder;
    private TextView priorityElementHolder;

    public TextView getTitleElementHolder() {
        return titleElementHolder;
    }

    public void setTitleElementHolder(TextView titleElementHolder) {
        this.titleElementHolder = titleElementHolder;
    }

    public TextView getDetailsElementHolder() {
        return detailsElementHolder;
    }

    public void setDetailsElementHolder(TextView detailsElementHolder) {
        this.detailsElementHolder = detailsElementHolder;
    }

    public TextView getPriorityElementHolder() {
        return priorityElementHolder;
    }

    public void setPriorityElementHolder(TextView priorityElementHolder) {
        this.priorityElementHolder = priorityElementHolder;
    }
}
