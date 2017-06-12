package com.adie.task4.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import static com.adie.task4.R.drawable.divider;
import com.adie.task4.R;
import com.adie.task4.adapter.ExpenseAdapter;
import com.adie.task4.adapter.IncomeAdapter;
import com.adie.task4.model.Expenses;
import com.adie.task4.model.Income;
import com.adie.task4.sqlitedb.SqliteHelper;
import com.adie.task4.view.DividerItemDecorationPadding;
import com.adie.task4.view.LoadingIndicator;

import java.util.ArrayList;
import java.util.List;


import static com.adie.task4.R.drawable.divider;

public class DashboardFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private boolean isProgress = false;
    private LoadingIndicator loadingIndicator;
    private RecyclerView recyclerView,recyclerView2;
    private SwipeRefreshLayout swipeRefreshLayout,swipeRefreshLayout2;
    private ArrayList<Expenses> mExpensesArrayList;
    private ArrayList<Income> mIncomeArrayList;
    private ExpenseAdapter expenseAdapter;
    private IncomeAdapter incomeAdapter;
    private SqliteHelper sqliteHelper;
    private TextView total,total2,balance,txttotal,txttotal2;
    public boolean setLoadingIndicator(LoadingIndicator mLoadingIndicator, boolean isShow) {
        if (isShow) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            return true;
        }
        mLoadingIndicator.setVisibility(View.GONE);
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitView(view);
        getActivity().setTitle("Dashboard");
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setLoadingIndicator(this.loadingIndicator,true);

        sqliteHelper = new SqliteHelper(getActivity());
        sqliteHelper.getAllExpenses();;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getExpense(true);
                getIncome(true);
            }
        }, 2000);


    }

    //init view for fragments
    private void InitView(View view) {
        this.recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        this.recyclerView2 = (RecyclerView)view.findViewById(R.id.recycler_view2);
        this.swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        this.swipeRefreshLayout2 = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout2);
        this.loadingIndicator = (LoadingIndicator)view.findViewById(R.id.loading_indicator);
        this.total = (TextView)view.findViewById(R.id.total_exp);
        this.total2 = (TextView)view.findViewById(R.id.total_inc);
        this.txttotal = (TextView)view.findViewById(R.id.txttotal);
        this.txttotal2 = (TextView)view.findViewById(R.id.txttotal2);
        this.balance = (TextView)view.findViewById(R.id.balance);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.addItemDecoration(new DividerItemDecorationPadding(getActivity(), divider));
        this.recyclerView2.setHasFixedSize(true);
        this.recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView2.addItemDecoration(new DividerItemDecorationPadding(getActivity(), divider));
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        this.swipeRefreshLayout2.setOnRefreshListener(this);
        this.swipeRefreshLayout2.setColorSchemeResources(R.color.colorPrimary);

        //set visibility to gone on textview total
        txttotal.setVisibility(View.GONE);
        txttotal2.setVisibility(View.GONE);
    }

    //get all expense from sqlite
    private void getExpense(boolean progress) {
        if(progress){
            setBalance();
            sqliteHelper.getAllExpenses();
            //array adapter
            this.mExpensesArrayList = sqliteHelper.getAllExpenses();
            // if expenses data didnt null
            if(mExpensesArrayList.size()>0){
                this.txttotal.setVisibility(View.VISIBLE);
                setLoadingIndicator(this.loadingIndicator,false);
                //sum value of amount
                int sum_exp= 0;
                for(int i=0; i<mExpensesArrayList.size(); i++){
                    sum_exp +=  mExpensesArrayList.get(i).getAmount();
                }
                this.total.setText("$"+String.valueOf(sum_exp));
                this.swipeRefreshLayout.setRefreshing(false);
                //parsing adapter to recycleriew
                this.expenseAdapter = new ExpenseAdapter(getActivity(),(ArrayList<Expenses>) this.mExpensesArrayList);
                this.recyclerView.setAdapter(this.expenseAdapter);
            }else{
                this.txttotal.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar
                        .make(recyclerView, "No Data Expense", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }


        if(!progress){
            this.swipeRefreshLayout.setRefreshing(false);
        }
    }

    //get all income from sqlite
    private void getIncome(boolean pro) {
        if(pro){
            setBalance();
            sqliteHelper.getAllIncome();
            //array adapter
            this.mIncomeArrayList = sqliteHelper.getAllIncome();
            // if income data didnt null
            if(mIncomeArrayList.size()>0){
                setLoadingIndicator(this.loadingIndicator,false);
                this.txttotal2.setVisibility(View.VISIBLE);
                //sum value of amount
                int sum_inc=0;
                for(int i=0; i<mIncomeArrayList.size(); i++){
                    sum_inc +=  mIncomeArrayList.get(i).getAmount();
                }
                this.total2.setText("$"+String.valueOf(sum_inc));
                this.swipeRefreshLayout2.setRefreshing(false);
                //parsing adapter to recycleriew
                this.incomeAdapter = new IncomeAdapter(getActivity(),(ArrayList<Income>) this.mIncomeArrayList);
                this.recyclerView2.setAdapter(this.incomeAdapter);
            }else{
                this.txttotal2.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar
                        .make(recyclerView2, "No Data Income", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }


        if(!pro){
            this.swipeRefreshLayout2.setRefreshing(false);
        }
    }
    @Override
    public void onRefresh() {
        //reload a request to make a set adapter
        getExpense(true);
        getIncome(true);
    }

    //balance of income & expense
    private void setBalance(){
        //TODO
        int bal = 0;
        int sum_exp=0;
        int sum_inc=0;
        ArrayList<Expenses> exp = sqliteHelper.getAllExpenses();
        for(int i=0;i<exp.size();i++){
            sum_exp +=  exp.get(i).getAmount();
        }

        ArrayList<Income> inc = sqliteHelper.getAllIncome();
        for(int i=0;i<inc.size();i++){
            sum_inc +=  inc.get(i).getAmount();
        }

        if(sum_exp!=0 && sum_inc !=0){
            bal = sum_inc - sum_exp;
            balance.setText("Balance : $"+String.valueOf(bal));
        }else{

        }

    }
}
