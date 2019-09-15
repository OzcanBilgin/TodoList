package com.example.todolist.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todolist.R;
import com.example.todolist.sqlite.DatabaseHelper;
import java.util.ArrayList;
import com.example.todolist.sqlite.TagDBHelper;
import com.example.todolist.model.TagsModel;
import  com.example.todolist.adapters.TagAdapter;


public class HomeActivity extends AppCompatActivity {

    private AppCompatActivity activity = HomeActivity.this;
    private AppCompatTextView textViewName;
    private DatabaseHelper databaseHelper;
    private TagDBHelper tagDBHelper;
    private ArrayList<TagsModel> tagsModels;
    private TagAdapter tagAdapter;
    private RecyclerView allTags;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("TodoList");
        initViews();
        loadTags();
    }

    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
    }

    private void loadTags(){
        allTags=(RecyclerView)findViewById(R.id.viewAllTags);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.no_tags_available);
        tagDBHelper=new TagDBHelper(this);
        if(tagDBHelper.countTags()==0){
            linearLayout.setVisibility(View.VISIBLE);
            allTags.setVisibility(View.GONE);
        }else{
            allTags.setVisibility(View.VISIBLE);
            tagsModels=new ArrayList<>();
            tagsModels=tagDBHelper.fetchTags();
            tagAdapter=new TagAdapter(tagsModels,this);
            linearLayout.setVisibility(View.GONE);
        }
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        allTags.setAdapter(tagAdapter);
        allTags.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                showInputDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    protected void showInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(HomeActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setView(promptView);
        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(tagDBHelper.addNewTag(new TagsModel(editText.getText().toString()))){
                            Toast.makeText(HomeActivity.this,"Başarılı",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                        }

                    }
                })
                .setNegativeButton("Kapat",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}