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
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
    private EditText edtTitle;
    private EditText edtTitleObject;
    private EditText edtDetailsObject;
    private RadioButton rdbHighObject;
    private RadioButton rdbMediumObject;
    private RadioButton rdbLowObject;
    private View positiveAction;

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
        addSaveIconObject = (ImageView) findViewById(R.id.addIcon);
        authorDetailsObject = (LinearLayout) findViewById(R.id.authorDetails);
        removeEditIconObject = (ImageView) findViewById(R.id.removeEditIcon);

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
                    // Add zone (Old type)
                    /*Intent intent = new Intent(MainActivity.this, ModifyDialogActivity.class);
                    intent.setFlags(VariableConstants.ADD_ELEMENT);
                    startActivity(intent);*/

                    // Add zone (New type)
                    MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                            .title(R.string.add_element)
                            .customView(R.layout.modify_body_layout, true)
                            .positiveText(R.string.submit_link)
                            .negativeText(android.R.string.cancel)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    // Set object toDoElement for modifying database
                                    ToDoElement toDoElementTemp = new ToDoElement();
                                    toDoElementTemp.setTitle(edtTitleObject.getText().toString());
                                    toDoElementTemp.setDetails(edtDetailsObject.getText().toString());
                                    if (rdbHighObject.isChecked()) {
                                        toDoElementTemp.setPriority(0);
                                    } else if (rdbMediumObject.isChecked()) {
                                        toDoElementTemp.setPriority(1);
                                    } else {
                                        toDoElementTemp.setPriority(2);
                                    }

                                    SQLiteConnection sqLiteConnection = new SQLiteConnection(MainActivity.this);
                                    // Check existed before inserting more
                                    if (sqLiteConnection.checkElementExists(toDoElementTemp)) {
                                        // If it's already existed, show error message
                                        failedAction(VariableConstants.EXISTED_MESSAGE_TITLE, VariableConstants.EXISTED_MESSAGE);
                                    } else {
                                        // Insert action
                                        long insertFlag = sqLiteConnection.insertElement(toDoElementTemp);
                                        sqLiteConnection.close();

                                        if (insertFlag != -1) {
                                            // If it inserts successfully, show message
                                            Toast.makeText(MainActivity.this, VariableConstants.REGISTER_MESSAGE_SUCCESS + toDoElementTemp.getTitle(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            // If there is error, show error message
                                            failedAction(VariableConstants.REGISTER_MESSAGE_FAILED_TITLE, VariableConstants.REGISTER_MESSAGE_FAILED);
                                        }
                                    }
                                    onResume();
                                }
                            }).build();

                    positiveAction = dialog.getActionButton(DialogAction.POSITIVE);

                    // Constant Conditions
                    edtTitleObject = (EditText) dialog.getCustomView().findViewById(R.id.edtTitle);
                    edtTitleObject.setEnabled(true);
                    edtTitleObject.setText("");
                    edtTitleObject.requestFocus();
                    edtTitleObject.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (edtTitleObject.getText().length() != 0 && edtDetailsObject.getText().length() != 0)
                                positiveAction.setEnabled(true);
                            else
                                positiveAction.setEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    // Constant Conditions
                    edtDetailsObject = (EditText) dialog.getCustomView().findViewById(R.id.edtDetails);
                    edtDetailsObject.setText("");
                    edtDetailsObject.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (edtTitleObject.getText().length() != 0 && edtDetailsObject.getText().length() != 0)
                                positiveAction.setEnabled(true);
                            else
                                positiveAction.setEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    // Constant Conditions
                    rdbHighObject = (RadioButton) dialog.getCustomView().findViewById(R.id.rdbHigh);
                    rdbMediumObject = (RadioButton) dialog.getCustomView().findViewById(R.id.rdbMedium);
                    rdbLowObject = (RadioButton) dialog.getCustomView().findViewById(R.id.rdbLow);
                    rdbHighObject.setChecked(true);
                    rdbMediumObject.setChecked(false);
                    rdbLowObject.setChecked(false);

                    dialog.show();
                    positiveAction.setEnabled(false); // disabled by default
                }
            }
        });
    }

    private Toast mToast;

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
                        // Edit zone (Old type)
                        /*Intent intent = new Intent(MainActivity.this, ModifyActivity.class);
                        intent.putExtra(VariableConstants.TITLE, toDoElementList.get(position).getTitle());
                        intent.putExtra(VariableConstants.DETAILS, toDoElementList.get(position).getDetails());
                        intent.putExtra(VariableConstants.PRIORITY, toDoElementList.get(position).getPriority());
                        intent.setFlags(VariableConstants.EDIT_ELEMENT);
                        startActivity(intent);*/

                        // Edit zone (New type)
                        MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                                .title(R.string.edit_element)
                                .customView(R.layout.modify_body_layout, true)
                                .positiveText(R.string.submit_link)
                                .negativeText(android.R.string.cancel)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        // Set object toDoElement for modifying database
                                        ToDoElement toDoElementTemp = new ToDoElement();
                                        toDoElementTemp.setTitle(edtTitleObject.getText().toString());
                                        toDoElementTemp.setDetails(edtDetailsObject.getText().toString());
                                        if (rdbHighObject.isChecked()) {
                                            toDoElementTemp.setPriority(0);
                                        } else if (rdbMediumObject.isChecked()) {
                                            toDoElementTemp.setPriority(1);
                                        } else {
                                            toDoElementTemp.setPriority(2);
                                        }

                                        SQLiteConnection sqLiteConnection = new SQLiteConnection(MainActivity.this);
                                        int updateFlag = sqLiteConnection.updateElement(toDoElementTemp);
                                        sqLiteConnection.close();

                                        if (updateFlag != -1) {
                                            // If it updates successfully, show message
                                            Toast.makeText(MainActivity.this, VariableConstants.UPDATE_MESSAGE_SUCCESS + toDoElementTemp.getTitle(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            // If there is error, show error message
                                            failedAction(VariableConstants.UPDATE_MESSAGE_FAILED_TITLE, VariableConstants.UPDATE_MESSAGE_FAILED);
                                        }
                                        onResume();
                                    }
                                }).build();

                        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);

                        // Constant Conditions
                        edtTitleObject = (EditText) dialog.getCustomView().findViewById(R.id.edtTitle);
                        edtTitleObject.setEnabled(false);
                        edtTitleObject.setText(toDoElementList.get(position).getTitle());

                        // Constant Conditions
                        edtDetailsObject = (EditText) dialog.getCustomView().findViewById(R.id.edtDetails);
                        edtDetailsObject.setText(toDoElementList.get(position).getDetails());
                        edtDetailsObject.requestFocus();
                        edtDetailsObject.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (edtTitleObject.getText().length() != 0 && edtDetailsObject.getText().length() != 0)
                                    positiveAction.setEnabled(true);
                                else
                                    positiveAction.setEnabled(false);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        // Constant Conditions
                        rdbHighObject = (RadioButton) dialog.getCustomView().findViewById(R.id.rdbHigh);
                        rdbMediumObject = (RadioButton) dialog.getCustomView().findViewById(R.id.rdbMedium);
                        rdbLowObject = (RadioButton) dialog.getCustomView().findViewById(R.id.rdbLow);
                        switch (toDoElementList.get(position).getPriority()) {
                            case 1:
                                // Medium
                                rdbHighObject.setChecked(false);
                                rdbMediumObject.setChecked(true);
                                rdbLowObject.setChecked(false);
                                break;
                            case 2:
                                // Low
                                rdbHighObject.setChecked(false);
                                rdbMediumObject.setChecked(false);
                                rdbLowObject.setChecked(true);
                                break;
                            default:
                                // High
                                rdbHighObject.setChecked(true);
                                rdbMediumObject.setChecked(false);
                                rdbLowObject.setChecked(false);
                                break;
                        }

                        dialog.show();
                        positiveAction.setEnabled(true); // disabled by default
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
     * There is error issue with action
     *
     * @param messageTitle   title of message
     * @param messageContain contain of message
     */
    private void failedAction(String messageTitle, String messageContain) {
        // Create onClickListener
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            /**
             * This method will be invoked when a button in the dialog is clicked.
             *
             * @param dialog The dialog that received the click.
             * @param which  The button that was clicked (e.g.
             *               {@link DialogInterface#BUTTON1}) or the position
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        break;
                }
            }
        };

        // Build dialog and show it
        android.support.v7.app.AlertDialog.Builder builderDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        builderDialog.setIcon(android.R.drawable.ic_dialog_alert);
        builderDialog.setTitle(messageTitle);
        builderDialog.setPositiveButton(VariableConstants.REGISTER_MESSAGE_AGAIN, onClickListener);
        builderDialog.setMessage(messageContain);
        builderDialog.show();

        // Set focus to Title
        edtTitleObject.requestFocus();
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
