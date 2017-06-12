package com.adie.task4.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.adie.task4.R;
import com.adie.task4.model.Expenses;

import java.util.ArrayList;



public class ExpenseAdapter extends Adapter<ExpenseAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Expenses> mExpenses;



    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder  {
        public Expenses mExpenses;
        public TextView mPrimary;
        public TextView mSecondary;
        public TextView mThird;
        public ImageView mSale;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.mPrimary = (TextView) itemLayoutView.findViewById(R.id.primary);
            this.mSecondary = (TextView) itemLayoutView.findViewById(R.id.secondary);


        }

    }
        public ExpenseAdapter(Context context, ArrayList<Expenses> expenses) {
            this.mContext = context;
            this.mExpenses = expenses;
        }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null));
    }

   @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Expenses expenses = (Expenses) this.mExpenses.get(position);
        viewHolder.mExpenses = expenses;
        viewHolder.mPrimary.setText(expenses.getDescription());
        viewHolder.mSecondary.setText("$"+String.valueOf(expenses.getAmount()));

    }


    public int getItemCount() {
        return this.mExpenses.size();
    }
}
