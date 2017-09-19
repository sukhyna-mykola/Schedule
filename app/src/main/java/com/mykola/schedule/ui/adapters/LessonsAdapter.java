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
import com.mykola.schedule.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.ViewHolder> {
    private List<LessonDTO> lessons;
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

    public LessonsAdapter(List<LessonDTO> lessonses, Context context) {
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
        if (lesson.getLessonWeek() == ScheduleManager.get(context).getWeekNumber()) {
            holder.numberLesson.setText(String.valueOf(lesson.getLessonNumber()));
            holder.roomLesson.setText(lesson.getLessonRoom());
            holder.teatherLesson.setText(lesson.getTeacherName());
            holder.nameLesson.setText(lesson.getLessonName() + "(" + lesson.getLessonType() + ")");

            configureTimeLesson(lesson, holder);
        }
    }

    private void configureTimeLesson(LessonDTO lesson, ViewHolder holder) {

        String[] times = Constants.times.get(lesson.getLessonNumber()).split("-");
        String timeStart = times[0];
        String timeEnd = times[1];

        holder.timeLesson.setText(timeStart + " - " + timeEnd);

        try {

            int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
            int currentWeek = ScheduleManager.get(context).getCurrentWeek();

            if (currentDay == lesson.getDayNumber() && currentWeek == lesson.getLessonWeek()) {

                holder.cardView.setCardBackgroundColor(Color.YELLOW);

                if (isLectionNow(timeStart, timeEnd)) {
                    holder.cardView.setCardBackgroundColor(Color.GREEN);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private boolean isLectionNow(String timeStart, String timeEnd) throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");

        String timeThis = df.format(Calendar.getInstance().getTime());

        Date startTime = df.parse(timeStart);
        Date endTime = df.parse(timeEnd);
        Date thisTime = df.parse(timeThis);

        if (thisTime.after(startTime) && thisTime.before(endTime)) {
            return true;
        }

        return false;

    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

}



