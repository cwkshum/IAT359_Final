package com.example.finalproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText;
    private Button newTaskSaveButton;

    private ChecklistDatabase db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Set the layout
        View view = inflater.inflate(R.layout.activity_newtask, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = Objects.requireNonNull(getView()).findViewById(R.id.newTaskText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);

        // boolean to control whether or not to update the db
        boolean isUpdate = false;

        // get the bundle arguments
        final Bundle bundle = getArguments();

        if(bundle != null){
            // if there are contents in the bundle, boolean for updating the db is set to true
            isUpdate = true;

            // set the text in the input field to be the list item
            String task = bundle.getString("toDoItem");
            newTaskText.setText(task);
//            assert task != null;
            if(task.length()>0) {
                // change the colour of the save button
                newTaskSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));
            }
        }

        // Access the database to retrieve activity
        db = new ChecklistDatabase(getActivity());

        // attach text changed listener to text input
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    // if noting has been entered, save button is not activated
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    // if text has been entered, save button is activated
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;

        // set onclick listener to the save button
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get user's input
                String text = newTaskText.getText().toString();
                if(finalIsUpdate){
                    // Update the item name in the database
                    if(db.updateToDoData(bundle.getString("username"), bundle.getString("checklistName"), bundle.getString("toDoItem"), text)){
                        dismiss();
                    } else {
                        Toast.makeText(ToDoAdapter.getContext(), "Failed to update, item name already exists", Toast.LENGTH_SHORT).show();
                    }
                }
//                else {
//                    ToDoModel task = new ToDoModel();
//                    task.setTask(text);
//                    task.setStatus(0);
//                    db.insertTask(task);
//                }
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        // close the dialog
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }
    }
}

