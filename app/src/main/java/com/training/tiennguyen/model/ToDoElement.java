/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.model;

/**
 * This class contains elements of a ToDoElement.
 *
 * @author TienNguyen
 */
public class ToDoElement {
    private String title;
    private String details;
    private int priority;

    public ToDoElement() {
    }

    public ToDoElement(String title, String details, int priority) {
        this.title = title;
        this.details = details;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
