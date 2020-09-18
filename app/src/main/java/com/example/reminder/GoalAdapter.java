package com.example.reminder;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    public GoalAdapter(Context mContext, ItemClickListener listener)
    {
        this.mContext = mContext;
        mItemClickListener = listener;
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
        else
        {
            holder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.round_corners));
        }

        holder.mTvGoalDetail.setText(goalDetail);
        holder.mTvDeadlineDate.setText(deadlineDateString);
        holder.mTvDeadlineTime.setText(deadlineTimeString);
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

        public GoalViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mTvGoalDetail = itemView.findViewById(R.id.tvGoalDetail);
            mTvDeadlineDate = itemView.findViewById(R.id.tvDeadlineDate);
            mTvDeadlineTime = itemView.findViewById(R.id.tvDeadlineTime);
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

