package com.example.todolist.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todolist.R;
import com.example.todolist.activities.HomeActivity;
import com.example.todolist.activities.TodoActivity;
import com.example.todolist.sqlite.TagDBHelper;
import com.example.todolist.model.TagsModel;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagDataHolder> {
    private ArrayList<TagsModel> tagsModels;
    private Context context;
    private TagDBHelper tagDBHelper;
    private  View view;
    public TagAdapter(ArrayList<TagsModel> tagsModels, Context context) {
        this.tagsModels = tagsModels;
        this.context = context;
    }

    @Override
    public TagDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_all_tags_layout,parent,false);
        return new TagDataHolder(view);
    }

    @Override
    public void onBindViewHolder(TagDataHolder holder, int position) {
        final TagsModel tagsModel=tagsModels.get(position);
        final int pos = position;
        holder.tag_title.setText(tagsModel.getTagTitle());
        tagDBHelper=new TagDBHelper(context);
        holder.tag_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(context,view);
                popupMenu.getMenuInflater().inflate(R.menu.tag_edit_del_option,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){

                            case R.id.delete:
                                removeTag(tagsModel.getTagID());
                                return true;
                            case R.id.share:
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, tagsModel.getTagTitle());
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, tagsModel.getTagID());
                                context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(context, TodoActivity.class);
                context.startActivity(intentRegister);
                //Toast.makeText(context, String.valueOf(tagsModels.get(pos).getTagTitle()), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagsModels.size();
    }

    public class TagDataHolder extends RecyclerView.ViewHolder{
        TextView tag_title;
        ImageView tag_option;
        public TagDataHolder(View itemView) {
            super(itemView);
            tag_title=(TextView)itemView.findViewById(R.id.tag_title);
            tag_option=(ImageView)itemView.findViewById(R.id.tags_option);
        }
    }

    private void removeTag(final int tagID){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Uyarı");
        builder.setMessage("Listen ve altındaki işler silinecektir.");
        builder.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(tagDBHelper.removeTag(tagID)){
                    Toast.makeText(context, "Başarılı", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, HomeActivity.class));
                }
            }
        }).setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Silinemedi", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, HomeActivity.class));
            }
        }).create().show();
    }

    public void filterTags(ArrayList<TagsModel> newTagsModels){
        tagsModels=new ArrayList<>();
        tagsModels.addAll(newTagsModels);
        notifyDataSetChanged();
    }
}
