/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.training.tiennguyen.constants.VariableConstants;
import com.training.tiennguyen.database.SQLiteConnection;
import com.training.tiennguyen.model.ToDoElement;

/**
 * This contain the modify activity. <br>
 * Such as: Add and Edit functions.
 *
 * @author TienNguyen
 */
public class ModifyActivity extends AppCompatActivity {
    private int actionFlags;
    private TextView appLogoObject;
    private ImageView removeIconObject;
    private ImageView cancelIconObject;
    private ImageView saveIconObject;
    private ImageView addIconObject;
    private EditText edtTitleObject;
    private EditText edtDetailsObject;
    private RadioButton rdbHighObject;
    private RadioButton rdbMediumObject;
    private RadioButton rdbLowObject;

    /**
     * Starting point of main activity.
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        // Initial view(s) inside of main activity
        initView();

        // Initial function(s) inside of main activity
        initFunction();
    }

    /**
     * Initial function(s) inside of main activity
     */
    private void initFunction() {

        // Set action when clicking Cancel
        cancelIconObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set action when clicking Save
        saveIconObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check input
                if (!inputCheck()) {
                    Toast.makeText(ModifyActivity.this, VariableConstants.INPUT_CHECK_FAILED, Toast.LENGTH_SHORT).show();
                    return;
                }

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

                SQLiteConnection sqLiteConnection = new SQLiteConnection(ModifyActivity.this);
                if (actionFlags == 1) {
                    // Insert zone
                    // Check existed before inserting more
                    if (sqLiteConnection.checkElementExists(toDoElementTemp)) {
                        // If it's already existed, show error message
                        failedAdd(VariableConstants.EXISTED_MESSAGE_TITLE, VariableConstants.EXISTED_MESSAGE);
                    } else {
                        // Insert action
                        long insertFlag = sqLiteConnection.insertElement(toDoElementTemp);
                        sqLiteConnection.close();

                        if (insertFlag != -1) {
                            // If it inserts successfully, show message
                            Toast.makeText(ModifyActivity.this, VariableConstants.REGISTER_MESSAGE_SUCCESS + toDoElementTemp.getTitle(), Toast.LENGTH_SHORT).show();

                            // Move back to MainActivity
                            finish();
                        } else {
                            // If there is error, show error message
                            failedAdd(VariableConstants.REGISTER_MESSAGE_FAILED_TITLE, VariableConstants.REGISTER_MESSAGE_FAILED);
                        }
                    }
                } else {
                    // Edit zone
                    int updateFlag = sqLiteConnection.updateElement(toDoElementTemp);
                    sqLiteConnection.close();

                    if (updateFlag != -1) {
                        // If it updates successfully, show message
                        Toast.makeText(ModifyActivity.this, VariableConstants.UPDATE_MESSAGE_SUCCESS + toDoElementTemp.getTitle(), Toast.LENGTH_SHORT).show();

                        // Move back to MainActivity
                        finish();
                    } else {
                        // If there is error, show error message
                        failedAdd(VariableConstants.UPDATE_MESSAGE_FAILED_TITLE, VariableConstants.UPDATE_MESSAGE_FAILED);
                    }
                }
            }
        });
    }

    /**
     * This function will check the value to see it suitable or not
     *
     * @return boolean result condition check
     */
    private boolean inputCheck() {
        // Title must not empty or length over 15 characters
        Editable object = edtTitleObject.getText();
        if (object == null || object.toString().isEmpty()) {
            edtTitleObject.setError(VariableConstants.TITLE_ERROR_EMPTY);
            edtTitleObject.requestFocus();
            return false;
        } else if (object.length() > 15) {
            edtTitleObject.setError(VariableConstants.TITLE_ERROR_OVER_15_CHARACTERS);
            edtTitleObject.requestFocus();
            return false;
        }

        // Details must not empty or length over 200 characters
        object = edtDetailsObject.getText();
        if (object == null || object.toString().isEmpty()) {
            edtDetailsObject.setError(VariableConstants.DETAILS_ERROR_EMPTY);
            edtDetailsObject.requestFocus();
            return false;
        } else if (object.length() > 200) {
            edtDetailsObject.setError(VariableConstants.DETAILS_ERROR_OVER_200_CHARACTERS);
            edtDetailsObject.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * There is error issue with insert record
     *
     * @param messageTitle   title of message
     * @param messageContain contain of message
     */
    private void failedAdd(String messageTitle, String messageContain) {
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
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(ModifyActivity.this);
        builderDialog.setIcon(android.R.drawable.ic_dialog_alert);
        builderDialog.setTitle(messageTitle);
        builderDialog.setPositiveButton(VariableConstants.REGISTER_MESSAGE_AGAIN, onClickListener);
        builderDialog.setMessage(messageContain);
        builderDialog.show();

        // Set focus to Title
        edtTitleObject.requestFocus();
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
        appLogoObject = (TextView) findViewById(R.id.appLogo);
        removeIconObject = (ImageView) findViewById(R.id.removeIcon);
        cancelIconObject = (ImageView) findViewById(R.id.cancelIcon);
        saveIconObject = (ImageView) findViewById(R.id.saveIcon);
        addIconObject = (ImageView) findViewById(R.id.addIcon);
        edtTitleObject = (EditText) findViewById(R.id.edtTitle);
        edtDetailsObject = (EditText) findViewById(R.id.edtDetails);
        rdbHighObject = (RadioButton) findViewById(R.id.rdbHigh);
        rdbMediumObject = (RadioButton) findViewById(R.id.rdbMedium);
        rdbLowObject = (RadioButton) findViewById(R.id.rdbLow);

        // Get intent to verify the type of actions
        Intent intent = getIntent();
        actionFlags = intent.getFlags();
        if (actionFlags == 1) {
            // Add case, all the view will be default
            edtTitleObject.setEnabled(true);
            edtTitleObject.setText("");
            edtDetailsObject.setText("");
            rdbHighObject.setChecked(true);
            rdbMediumObject.setChecked(false);
            rdbLowObject.setChecked(false);
            appLogoObject.setText(R.string.add_element);
        } else {
            // Edit case, all the view will get values which transfer from the list's element
            edtTitleObject.setEnabled(false);
            edtTitleObject.setText(intent.getStringExtra(VariableConstants.TITLE));
            edtDetailsObject.setText(intent.getStringExtra(VariableConstants.DETAILS));
            switch (intent.getIntExtra(VariableConstants.PRIORITY, 0)) {
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
            appLogoObject.setText(R.string.edit_element);
        }

        // Set icons' appearance
        removeIconObject.setVisibility(View.INVISIBLE);
        cancelIconObject.setVisibility(View.VISIBLE);
        saveIconObject.setVisibility(View.VISIBLE);
        addIconObject.setVisibility(View.INVISIBLE);
    }
}
