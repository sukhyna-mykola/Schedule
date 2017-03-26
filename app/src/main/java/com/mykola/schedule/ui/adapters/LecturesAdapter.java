package com.mykola.schedule.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mykola.schedule.R;
import com.mykola.schedule.data.managers.EditSheduleManager;
import com.mykola.schedule.data.storage.models.EditLecture;
import com.mykola.schedule.ui.dialogs.edit.CallbackDialog;
import com.mykola.schedule.ui.dialogs.edit.DialogCreate;
import com.mykola.schedule.ui.dialogs.edit.DialogEdit;
import com.mykola.schedule.ui.dialogs.edit.DialogMoveWarning;
import com.mykola.schedule.ui.dialogs.edit.DialogRemove;
import com.mykola.schedule.ui.dialogs.edit.DialogLesson;

import java.util.ArrayList;

/**
 * Created by mykola on 16.01.17.
 */

public class LecturesAdapter extends RecyclerView.Adapter<LecturesAdapter.ViewHolder> implements PopupMenu.OnMenuItemClickListener {

    private static final String DIALOG_CREATE = "DIALOG_CREATE";
    private static final String DIALOG_EDIT = "DIALOG_EDIT";
    private static final String DIALOG_REMOVE = "DIALOG_REMOVE";
    private static final String DIALOG_MOVE_WARNING = "DIALOG_MOVE_WARNING";

    private ArrayList<EditLecture> lessons;

    private Context context;
    private CallbackDialog callback;

    private EditSheduleManager manager;

    private int selectedPosition = -1;

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit:
                showDialog(DialogEdit.newInstance(selectedPosition), DIALOG_EDIT);
                return true;
            case R.id.move:
                Toast.makeText(context, R.string.select_cell, Toast.LENGTH_SHORT).show();
                manager.runMove(selectedPosition);
                callback.update();
                return true;
            case R.id.remove:
                showDialog(DialogRemove.newInstance(selectedPosition), DIALOG_REMOVE);
                return true;
            default:
                return false;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        CardView cardView;
        TextView lessonNumber, lessonName, lessonType;
        ImageView imageView;


        public ViewHolder(View v) {
            super(v);
            lessonNumber = (TextView) v.findViewById(R.id.number_lesson);
            lessonName = (TextView) v.findViewById(R.id.name_lesson);
            lessonType = (TextView) v.findViewById(R.id.type_lesson);
            cardView = (CardView) v.findViewById(R.id.item_edit_lesson);
            imageView = (ImageView) v.findViewById(R.id.image_lesson);


        }
    }

    public LecturesAdapter(ArrayList<EditLecture> lessons, Context context, CallbackDialog callback) {
        this.lessons = lessons;
        this.context = context;
        this.callback = callback;

        manager = EditSheduleManager.getManager(context);
    }

    @Override
    public LecturesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_lesson, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final EditLecture lesson = lessons.get(position);


        if (lesson.getLesson() != null) {
            holder.cardView.setCardBackgroundColor(Color.GREEN);
            holder.lessonNumber.setText(String.valueOf(lesson.getLessonNumber()));
            holder.lessonName.setText(lesson.getLesson().getLessonName());
            holder.lessonType.setText(lesson.getLesson().getLessonType());

            holder.lessonNumber.setVisibility(View.VISIBLE);
            holder.lessonType.setVisibility(View.VISIBLE);
            holder.lessonName.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.INVISIBLE);
        } else {
            holder.lessonNumber.setVisibility(View.INVISIBLE);
            holder.lessonType.setVisibility(View.INVISIBLE);
            holder.lessonName.setVisibility(View.INVISIBLE);
            holder.imageView.setVisibility(View.VISIBLE);
            if (manager.isMoveState()) {
                holder.cardView.setCardBackgroundColor(Color.GRAY);
            } else
                holder.cardView.setCardBackgroundColor(Color.WHITE);

        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;

                if (!manager.isMoveState()) {
                    if (lesson.getLesson() != null) {
                        showPopup(v);

                    } else {
                        showDialog(DialogCreate.newInstance(selectedPosition), DIALOG_CREATE);

                    }
                } else {
                    if (lesson.getLesson() != null) {
                        showDialog(DialogMoveWarning.newInstance(selectedPosition),DIALOG_MOVE_WARNING);
                    } else {
                        manager.endMove(selectedPosition);
                        callback.update();
                    }
                }
            }
        });

    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(this);

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup, popup.getMenu());

        popup.show();
    }

    private void showDialog(DialogLesson dialog, String tag) {
        dialog.registerCallback(callback);
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), tag);
    }


    @Override
    public int getItemCount() {
        return lessons.size();
    }
}



