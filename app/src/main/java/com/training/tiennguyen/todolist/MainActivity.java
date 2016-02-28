/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.training.tiennguyen.adapter.ToDoListAdapter;
import com.training.tiennguyen.constants.VariableConstants;
import com.training.tiennguyen.database.SQLiteConnection;
import com.training.tiennguyen.model.ToDoElement;

import java.util.ArrayList;
import java.util.List;

/**
 * This contain the main activity. <br>
 * Such as: Showing list, Add and Edit buttons, etc.
 *
 * @author TienNguyen
 */
public class MainActivity extends AppCompatActivity {
    private List<ToDoElement> toDoElementList = new ArrayList<>();
    private ListView toDoListObject;
    private TextView noItemsObject;
    private TextView appLogoObject;
    private TextView authorBody1Object;
    private TextView authorBody2Object;
    private TextView authorBody3Object;
    private ImageView removeEditIconObject;
    private ImageView closeIconObject;
    private ImageView addSaveIconObject;
    private LinearLayout authorDetailsObject;
    private int removeEditIconLevel;
    private int addSaveIconLevel;

    /**
     * Starting point of main activity.
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial view(s) inside of main activity
        initView();

        // Initial function(s) inside of main activity
        initFunction();
    }

    /**
     * Initial view(s) inside of main activity
     */
    private void initView() {
        // Hide Support Action Bar if it is possible
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Init Internal Object for action
        toDoListObject = (ListView) findViewById(R.id.toDoList);
        noItemsObject = (TextView) findViewById(R.id.noItems);
        appLogoObject = (TextView) findViewById(R.id.appLogo);
        authorBody1Object = (TextView) findViewById(R.id.authorBody1);
        authorBody2Object = (TextView) findViewById(R.id.authorBody2);
        authorBody3Object = (TextView) findViewById(R.id.authorBody3);
        removeEditIconObject = (ImageView) findViewById(R.id.removeEditIcon);
        closeIconObject = (ImageView) findViewById(R.id.closeIcon);
        addSaveIconObject = (ImageView) findViewById(R.id.addSaveIcon);
        authorDetailsObject = (LinearLayout) findViewById(R.id.authorDetails);

        // Set default icon level
        removeEditIconLevel = 0;
        removeEditIconObject.setImageLevel(removeEditIconLevel);
        addSaveIconLevel = 0;
        addSaveIconObject.setImageLevel(addSaveIconLevel);

        // Set logo text
        TextView appLogoObject = (TextView) findViewById(R.id.appLogo);
        appLogoObject.setText(R.string.to_do_list);
    }

    /**
     * Initial function(s) inside of main activity
     */
    private void initFunction() {
        // Set animation for author details
        authorDetailsInit();

        // Set animation for list
        toDoElementListInit();

        // Set action when clicking Add
        addSaveIconObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addSaveIconLevel == 0) {
                    // Add zone
                    Intent intent = new Intent(MainActivity.this, ModifyActivity.class);
                    intent.setFlags(VariableConstants.ADD_ELEMENT);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Set animation for author details
     */
    private void authorDetailsInit() {
        // Animation from Left to Right
        final Animation slideToRight = AnimationUtils.loadAnimation(this, R.anim.slide_left_to_right);
        slideToRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                authorDetailsObject.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // Animation from Right to Left
        final Animation slideToLeft = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);
        slideToLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                authorDetailsObject.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // Set action when clicking Logo
        appLogoObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authorDetailsObject.getVisibility() == View.GONE) {
                    // Set text (temperately)
                    authorBody1Object.setText(Html.fromHtml(VariableConstants.AUTHOR_MAJOR));
                    authorBody2Object.setText(Html.fromHtml(VariableConstants.AUTHOR_INTEREST));
                    authorBody3Object.setText(Html.fromHtml(VariableConstants.AUTHOR_DESCRIBE));

                    // Show up the authorDetails layout
                    authorDetailsObject.setVisibility(View.VISIBLE);
                    authorDetailsObject.startAnimation(slideToRight);
                }
            }
        });

        // Set action when clicking Close
        closeIconObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authorDetailsObject.getVisibility() == View.VISIBLE) {
                    authorDetailsObject.startAnimation(slideToLeft);
                }
            }
        });
    }

    /**
     * Function to check there is any record inside of list.
     */
    private void toDoElementListInit() {
        // Check the total number count function
        SQLiteConnection sqLiteConnection = new SQLiteConnection(MainActivity.this);
        int resultCount = sqLiteConnection.selectCountToDoObjects();
        sqLiteConnection.close();

        if (resultCount > 0) {
            // If it's true, list contains some elements
            // Only toDoList & removeIcon are visible.
            toDoListObject.setVisibility(View.VISIBLE);
            removeEditIconObject.setVisibility(View.VISIBLE);

            noItemsObject.setVisibility(View.INVISIBLE);
            addSaveIconObject.setVisibility(View.VISIBLE);

            // Get list's records from database
            sqLiteConnection = new SQLiteConnection(MainActivity.this);
            toDoElementList = sqLiteConnection.selectAllToDoObjects();
            sqLiteConnection.close();

            // Create adapter
            ToDoListAdapter toDoListAdapter = new ToDoListAdapter(this, toDoElementList);

            // Set adapter to list
            toDoListObject.setAdapter(toDoListAdapter);

            // If user chooses any elements, move to edit page
            toDoListObject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    // Base on the CancelIcon is visible or not
                    if (removeEditIconLevel == 1) {

                        // Remove zone
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(VariableConstants.REMOVE_MESSAGE_TITLE)
                                .setMessage("Do you really want to remove [" + toDoElementList.get(position).getTitle() + "] ?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        ToDoElement element = new ToDoElement();
                                        element.setTitle(toDoElementList.get(position).getTitle());

                                        // Remove element
                                        SQLiteConnection sqLiteConnection = new SQLiteConnection(MainActivity.this);
                                        int deleteFlag = sqLiteConnection.deleteElement(element);
                                        sqLiteConnection.close();

                                        if (deleteFlag > 0) {
                                            // If it's removable
                                            Toast.makeText(MainActivity.this, VariableConstants.REMOVE_MESSAGE_SUCCESS + element.getTitle(), Toast.LENGTH_SHORT).show();
                                            onResume();
                                        } else {
                                            // If there is error, show error message
                                            Toast.makeText(MainActivity.this, VariableConstants.REMOVE_MESSAGE_FAILED + element.getTitle(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .show();
                    } else {
                        // Edit zone
                        Intent intent = new Intent(MainActivity.this, ModifyActivity.class);
                        intent.putExtra(VariableConstants.TITLE, toDoElementList.get(position).getTitle());
                        intent.putExtra(VariableConstants.DETAILS, toDoElementList.get(position).getDetails());
                        intent.putExtra(VariableConstants.PRIORITY, toDoElementList.get(position).getPriority());
                        intent.setFlags(VariableConstants.EDIT_ELEMENT);
                        startActivity(intent);
                    }
                }
            });

            // Set action when clicking Remove
            removeEditIconObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (removeEditIconLevel == 0) {
                        // If it's Remove
                        removeEditIconLevel = 1;
                        removeEditIconObject.setImageLevel(removeEditIconLevel);
                        Toast.makeText(MainActivity.this, VariableConstants.REMOVE_MODE, Toast.LENGTH_SHORT).show();
                    } else {
                        //  If it's Cancel
                        removeEditIconLevel = 0;
                        removeEditIconObject.setImageLevel(removeEditIconLevel);
                        Toast.makeText(MainActivity.this, VariableConstants.EDIT_MODE, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            // If it's false, it's empty list.
            // Only noItems is visible.
            noItemsObject.setVisibility(View.VISIBLE);

            toDoListObject.setVisibility(View.INVISIBLE);
            removeEditIconObject.setVisibility(View.INVISIBLE);
            addSaveIconObject.setVisibility(View.VISIBLE);

            // Init list
            toDoElementList = new ArrayList<>();
        }
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        // Hide author details layout
        authorDetailsObject.setVisibility(View.GONE);

        // Initial view(s) inside of main activity
        initView();

        // Initial function(s) inside of main activity
        initFunction();

        super.onResume();
    }

    /**
     * Github 1 onclick action
     *
     * @param view view
     */
    public void link1OnClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(VariableConstants.GITHUB_URL));
        startActivity(browserIntent);
    }

    /**
     * Linkedin onclick action
     *
     * @param view view
     */
    public void link2OnClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(VariableConstants.LINKEDIN_URL));
        startActivity(browserIntent);
    }

}
