package com.example.todolist.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import com.example.todolist.activities.TodoActivity;
import 	androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.example.todolist.R;
import com.example.todolist.sqlite.TodoDBHelper;
import com.example.todolist.model.PendingTodoModel;
import java.util.ArrayList;

public class PendingTodoAdapter extends RecyclerView.Adapter<PendingTodoAdapter.PendingDataHolder>{
    private ArrayList<PendingTodoModel> pendingTodoModels;
    private Context context;
    private TodoDBHelper todoDBHelper;

    public PendingTodoAdapter(ArrayList<PendingTodoModel> pendingTodoModels, Context context) {
        this.pendingTodoModels = pendingTodoModels;
        this.context = context;
    }

    @Override
    public PendingTodoAdapter.PendingDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pending_todo_layout,parent,false);
        return new PendingDataHolder(view);
    }

    @Override
    public void onBindViewHolder(PendingTodoAdapter.PendingDataHolder holder, int position) {
        todoDBHelper=new TodoDBHelper(context);
        final PendingTodoModel pendingTodoModel=pendingTodoModels.get(position);
        holder.todoTitle.setText(pendingTodoModel.getTodoTitle());
        holder.todoContent.setText(pendingTodoModel.getTodoContent());
        holder.todoDate.setText(pendingTodoModel.getTodoDate());
        holder.todoTag.setText(pendingTodoModel.getTodoTag());
        holder.todoTime.setText(pendingTodoModel.getTodoTime());
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(context,view);
                popupMenu.getMenuInflater().inflate(R.menu.todo_edit_del_options,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){

                            case R.id.delete:
                                showDeleteDialog(pendingTodoModel.getTodoID());
                                return true;
                            case R.id.share:
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, pendingTodoModel.getTodoContent());
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, pendingTodoModel.getTodoTitle());
                                context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });
        holder.makeCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showCompletedDialog(pendingTodoModel.getTodoID());
            }
        });
    }


    private void showDeleteDialog(final int tagID){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Todo delete confirmation");
        builder.setMessage("Do you really want to delete ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(todoDBHelper.removeTodo(tagID)){
                    Toast.makeText(context, "Todo deleted successfully !", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, TodoActivity.class));
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Todo not deleted !", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, TodoActivity.class));
            }
        }).create().show();
    }

    @Override
    public int getItemCount() {
        return pendingTodoModels.size();
    }

    public class PendingDataHolder extends RecyclerView.ViewHolder {
        TextView todoTitle,todoContent,todoTag,todoDate,todoTime;
        ImageView option,makeCompleted;
        public PendingDataHolder(View itemView) {
            super(itemView);
            todoTitle=(TextView)itemView.findViewById(R.id.pending_todo_title);
            todoContent=(TextView)itemView.findViewById(R.id.pending_todo_content);
            todoTag=(TextView)itemView.findViewById(R.id.todo_tag);
            todoDate=(TextView)itemView.findViewById(R.id.todo_date);
            todoTime=(TextView)itemView.findViewById(R.id.todo_time);
            option=(ImageView)itemView.findViewById(R.id.option);
            makeCompleted=(ImageView)itemView.findViewById(R.id.make_completed);
        }
    }

    public void filterTodos(ArrayList<PendingTodoModel> newPendingTodoModels){
        pendingTodoModels=new ArrayList<>();
        pendingTodoModels.addAll(newPendingTodoModels);
        notifyDataSetChanged();
    }
}
