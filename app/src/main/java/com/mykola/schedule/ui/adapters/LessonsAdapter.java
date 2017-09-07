package com.mykola.schedule.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mykola.schedule.R;
import com.mykola.schedule.data.managers.ScheduleManager;
import com.mykola.schedule.data.storage.models.LessonDTO;

import java.util.ArrayList;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.ViewHolder> {
    private ArrayList<LessonDTO> lessons;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameLesson;
        TextView teatherLesson;
        TextView roomLesson;
        TextView numberLesson;
        TextView timeLesson;

        CardView cardView;

        public ViewHolder(View v) {
            super(v);
            nameLesson = (TextView) v.findViewById(R.id.name_lesson);
            teatherLesson = (TextView) v.findViewById(R.id.teather_lesson);
            roomLesson = (TextView) v.findViewById(R.id.room_lesson);
            numberLesson = (TextView) v.findViewById(R.id.number_lesson);
            timeLesson = (TextView) v.findViewById(R.id.time_lesson);
            cardView = (CardView) v.findViewById(R.id.lesson_item);

        }
    }

    public LessonsAdapter(ArrayList<LessonDTO> lessonses, Context context) {
        this.lessons = lessonses;
        this.context = context;
    }

    @Override
    public LessonsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LessonDTO lesson = lessons.get(position);
        if (Integer.parseInt(lesson.getLessonWeek()) == ScheduleManager.get(context).getWeekNumber()) {
            holder.numberLesson.setText(lesson.getLessonNumber());
            holder.roomLesson.setText(lesson.getLessonRoom());
            holder.teatherLesson.setText(lesson.getTeacherName());
            holder.nameLesson.setText(lesson.getLessonName() + "(" + lesson.getLessonType() + ")");
            holder.timeLesson.setText(lesson.getTimeStart() + " - " + lesson.getTimeEnd());

            if (lesson.isCurrentDay()) {
                holder.cardView.setCardBackgroundColor(Color.YELLOW);
            }

            if (lesson.isCurrentLesson()) {
                holder.cardView.setCardBackgroundColor(Color.GREEN);
            }

        }

    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }
}



