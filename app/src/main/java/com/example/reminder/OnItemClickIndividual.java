package com.example.reminder;

import android.view.View;

public class OnItemClickIndividual implements View.OnClickListener{

    private int position;
    private checkBoxClickCallback checkBoxCallback;

    public OnItemClickIndividual(int position, checkBoxClickCallback checkBoxCallback) {
        this.position = position;
        this.checkBoxCallback = checkBoxCallback;
    }

    @Override
    public void onClick(View view) {
        checkBoxCallback.onItemClicked(view, position);
    }

    public interface checkBoxClickCallback {
        void onItemClicked(View view, int position);
    }
}
