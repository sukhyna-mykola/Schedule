package com.mykola.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mykola on 16.01.17.
 */

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.ViewHolder> {
    private ArrayList<Lesson> lessons;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameLesson;
        TextView teatherLesson;
        TextView roomLesson;
        TextView numberLesson;

        public ViewHolder(View v) {
            super(v);
            nameLesson = (TextView) v.findViewById(R.id.name_lesson);
            teatherLesson = (TextView) v.findViewById(R.id.teather_lesson);
            roomLesson = (TextView) v.findViewById(R.id.room_lesson);
            numberLesson = (TextView) v.findViewById(R.id.number_lesson);

        }
    }

    public LessonsAdapter(ArrayList<Lesson> lessonses) {
        this.lessons = lessonses;
    }

    @Override
    public LessonsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Lesson lessons = this.lessons.get(position);
        if (Integer.parseInt(lessons.getLessonWeek()) == MainActivity.weekNumber) {
            holder.numberLesson.setText(lessons.getLessonNumber());
            holder.roomLesson.setText(lessons.getLessonRoom());
            holder.teatherLesson.setText(lessons.getTeacherName());
            holder.nameLesson.setText(lessons.getLessonName()+"("+ lessons.getLessonType()+")");
        }

    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }
}



