package com.adie.task4.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.adie.task4.R;
import com.adie.task4.sqlitedb.SqliteHelper;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TransactionFragment extends Fragment {
    private SqliteHelper sqliteHelper;
    private TextInputLayout inp_lay_desc,inp_lay_desc2,inp_lay_amo,inp_lay_amo2;
    private EditText desc,desc2,amount,amount2;
    private Button btnadd,btnadd2;
    private int i = -1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitView(view);
        getActivity().setTitle("Transactions");
    }

    //initial view fragment
    private void InitView(View view) {
        this.sqliteHelper = new SqliteHelper(getActivity());
        this.desc = (EditText)view.findViewById(R.id.input_descr);
        this.desc2 = (EditText)view.findViewById(R.id.input_descr2);
        this.amount = (EditText)view.findViewById(R.id.input_amount);
        this.amount2 = (EditText)view.findViewById(R.id.input_amount2);
        this.btnadd = (Button)view.findViewById(R.id.btn_exp);
        this.btnadd2 = (Button)view.findViewById(R.id.btn_inc);
        this.inp_lay_desc = (TextInputLayout)view.findViewById(R.id.input_layout_descr);
        this.inp_lay_desc2 = (TextInputLayout)view.findViewById(R.id.input_layout_descr2);
        this.inp_lay_amo = (TextInputLayout)view.findViewById(R.id.input_layout_amount);
        this.inp_lay_amo2 = (TextInputLayout)view.findViewById(R.id.input_layout_amount2);

        this.desc.addTextChangedListener(new MyTextWatcher(desc));
        this.desc2.addTextChangedListener(new MyTextWatcher(desc2));
        this.amount.addTextChangedListener(new MyTextWatcher(amount));
        this.amount2.addTextChangedListener(new MyTextWatcher(amount2));

        //onclick button add expensive
        this.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validating first the input
                if(!validateDesc()){
                    return;
                }
                else if(!validateLimDesc()){
                    return;
                }
                else if(!validateAmount()){
                    return;
                }
                else if(!validateLimAmount()){
                    return;

                }else{
                    //make call to create new data expenses
                    String descr = desc.getText().toString();
                    int amo = Integer.parseInt(amount.getText().toString());
                    sqliteHelper.AddExpenses(descr,amo);
                    final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("Add New Expenses");
                    pDialog.show();
                    pDialog.setCancelable(false);
                    //show alertdialog with countdowntimer 3 seconds
                    new CountDownTimer(3000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            i++;
                            switch (i){
                                case 0:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                                    break;
                                case 1:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                                    break;
                                case 2:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                    break;
                                case 3:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                                    break;
                                case 4:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                                    break;
                                case 5:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                                    break;
                                case 6:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                    break;
                            }
                        }

                        public void onFinish() {
                            i = -1;
                            pDialog.setTitleText("Success!")
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        }
                    }.start();
                }
            }
        });

        //onclick button add income
        this.btnadd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validating first the input
                if(!validateDesc2()){
                return;
                }
                else if(!validateLimDesc2()){
                return;
                }
                else if(!validateAmount2()){
                return;
                }
                else if(!validateLimAmount2()) {
                    return;
                }else{
                    //make call to create new data income
                    String descr2 = desc2.getText().toString();
                    int amou2 = Integer.parseInt(amount2.getText().toString());
                    sqliteHelper.AddIncome(descr2,amou2);
                    final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("Add New Income");
                    pDialog.show();
                    pDialog.setCancelable(false);
                    //show alertdialog with countdowntimer 3 seconds
                    new CountDownTimer(3000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            // you can change the progress bar color by ProgressHelper every 800 millis
                            i++;
                            switch (i){
                                case 0:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                                    break;
                                case 1:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                                    break;
                                case 2:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                    break;
                                case 3:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                                    break;
                                case 4:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                                    break;
                                case 5:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                                    break;
                                case 6:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                    break;
                            }
                        }

                        public void onFinish() {
                            i = -1;
                            pDialog.setTitleText("Success!")
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        }
                    }.start();
                }

            }
        });
    }

    //validate edittext with required value
    private boolean validateDesc() {
        if (desc.getText().toString().trim().isEmpty()) {
            inp_lay_desc.setError("This Field Should Not Be Empty");
            requestFocus(desc);
            return false;
        } else {
            inp_lay_desc.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateLimDesc() {
        if (desc.getText().toString().length()<4) {
            inp_lay_desc.setError("Minimal Character is 4");
            requestFocus(desc);
            return false;
        } else {
            inp_lay_desc.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateDesc2() {
        if (desc2.getText().toString().trim().isEmpty()) {
            inp_lay_desc2.setError("This Field Should Not Be Empty");
            requestFocus(desc2);
            return false;
        } else {
            inp_lay_desc2.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateLimDesc2() {
        if (desc2.getText().toString().length()<4) {
            inp_lay_desc2.setError("Minimal Character is 4");
            requestFocus(desc2);
            return false;
        } else {
            inp_lay_desc2.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateAmount() {
        if (amount.getText().toString().trim().isEmpty()) {
            inp_lay_amo.setError("This Field Should Not Be Empty");
            requestFocus(amount);
            return false;
        } else {
            inp_lay_amo.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateLimAmount() {
        if (amount.getText().toString().length()<2) {
            inp_lay_amo.setError("Minimal Character is 2");
            requestFocus(amount);
            return false;
        } else {
            inp_lay_amo.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAmount2() {
        if (amount2.getText().toString().trim().isEmpty()) {
            inp_lay_amo2.setError("This Field Should Not Be Empty");
            requestFocus(amount2);
            return false;
        } else {
            inp_lay_amo2.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateLimAmount2() {
        if (amount2.getText().toString().length()<2) {
            inp_lay_amo2.setError("Minimal Character is 2");
            requestFocus(amount2);
            return false;
        } else {
            inp_lay_amo2.setErrorEnabled(false);
        }

        return true;
    }

    //focus on view target
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //Textwatcher validate every text changed
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_descr:
                    validateDesc();
                    validateLimDesc();
                    break;
                case R.id.input_descr2:
                    validateDesc2();
                    validateLimDesc2();
                    break;
                case R.id.input_amount:
                    validateAmount();
                    validateLimAmount();
                    break;
                case R.id.input_amount2:
                    validateAmount2();
                    validateLimAmount2();
            }
        }
    }
}
