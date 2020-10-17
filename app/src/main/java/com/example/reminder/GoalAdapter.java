package com.example.reminder;
import android.content.Context;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.example.reminder.database.EachGoal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder>
{
    private Context mContext;
    private List<EachGoal> mGoalList;
    final private ItemClickListener mItemClickListener;

    private OnItemClickIndividual.checkBoxClickCallback onItemClickCallback;

    public GoalAdapter(Context mContext, ItemClickListener listener, OnItemClickIndividual.checkBoxClickCallback onItemClickCallback)
    {
        this.mContext = mContext;
        mItemClickListener = listener;
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public GoalAdapter.GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.each_goal_layout,parent,false);

        return new GoalViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull GoalAdapter.GoalViewHolder holder, int position)
    {
        //String goalName, Date originalDeadlineDate, Date virtualDeadlineDate, boolean allowVD, boolean vDAlreadyCreatedForIt
        EachGoal eachGoal = mGoalList.get(position);
        String goalDetail = eachGoal.getGoalName();

        Date virtualDeadlineDate = new Date(System.currentTimeMillis());
        if(eachGoal.getVirtualDeadlineDate() != null)
        {
            virtualDeadlineDate = eachGoal.getVirtualDeadlineDate();
        }

        //String deadlineDateString = DateFormat.getDateInstance(DateFormat.FULL).format(virtualDeadlineDate);
        String deadlineDateString = DateFormat.getDateInstance(DateFormat.FULL).format(virtualDeadlineDate);

        DateFormat format = new SimpleDateFormat("hh:mm a");

        String deadlineTimeString = format.format(virtualDeadlineDate);

        if(eachGoal.isNearestToTodayDate())
        {
            holder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.nearestday_round_corners));
        }
        else if(!eachGoal.isNearestToTodayDate())
        {
            holder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.round_corners));
        }

        if(eachGoal.isPastDate())
        {
            holder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.past_day_rounded_corner));
        }

        boolean taskDone = eachGoal.isTaskDone();
        holder.mCheckBox.setChecked(taskDone);

        holder.mCheckBox.setOnClickListener(new OnItemClickIndividual(position,onItemClickCallback));

        if(taskDone)
        {
            SpannableStringBuilder spannableStringBuilderName = new SpannableStringBuilder(goalDetail);
            StrikethroughSpan strikethroughSpanName = new StrikethroughSpan();
            spannableStringBuilderName.setSpan(strikethroughSpanName,0,goalDetail.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.mTvGoalDetail.setText(spannableStringBuilderName);

            SpannableStringBuilder sSBDate = new SpannableStringBuilder(deadlineDateString);
            StrikethroughSpan sTSDate = new StrikethroughSpan();
            sSBDate.setSpan(sTSDate,0,deadlineDateString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.mTvDeadlineDate.setText(sSBDate);

            SpannableStringBuilder sSBTime = new SpannableStringBuilder(deadlineTimeString);
            StrikethroughSpan sTSTime = new StrikethroughSpan();
            sSBTime.setSpan(sTSTime,0,deadlineTimeString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.mTvDeadlineTime.setText(sSBTime);


        }
        else
        {
            holder.mTvGoalDetail.setText(goalDetail);
            holder.mTvDeadlineDate.setText(deadlineDateString);
            holder.mTvDeadlineTime.setText(deadlineTimeString);
        }


    }

    @Override
    public int getItemCount()
    {
        if(mGoalList == null)
            return 0;
        return mGoalList.size();
    }

    public List<EachGoal> getGoalList()
    {
        return mGoalList;
    }

    public void setGoalList(List<EachGoal> goalList)
    {
        mGoalList = goalList;
        notifyDataSetChanged();
    }

    public interface ItemClickListener
    {
        void onItemClickListener(int itemId);
    }

    class GoalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView mTvGoalDetail;
        TextView mTvDeadlineDate;
        TextView mTvDeadlineTime;
        CheckBox mCheckBox;

        public GoalViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mTvGoalDetail = itemView.findViewById(R.id.tvGoalDetail);
            mTvDeadlineDate = itemView.findViewById(R.id.tvDeadlineDate);
            mTvDeadlineTime = itemView.findViewById(R.id.tvDeadlineTime);
            mCheckBox = itemView.findViewById(R.id.checkbox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int itemId = mGoalList.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(itemId);

        }
    }
}

