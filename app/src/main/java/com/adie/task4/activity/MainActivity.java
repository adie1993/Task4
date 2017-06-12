package com.adie.task4.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.adie.task4.fragment.DashboardFragment;
import com.adie.task4.R;
import com.adie.task4.fragment.TransactionFragment;
import com.adie.task4.model.Expenses;
import com.adie.task4.model.Income;
import com.adie.task4.sqlitedb.SqliteHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SqliteHelper sqliteHelper;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    NavigationView navigationView;
    private ArrayList<Expenses> mExpenseArrayList;
    private ArrayList<Income> mIncomeArrayList;
    private ProgressDialog pdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sqliteHelper = new SqliteHelper(this);
        sqliteHelper.CreateTabel();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("db_advance");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //display fragment by param id
        displaySelectedScreen(R.id.nav_menu1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void synch() {
        // In real apps this userId should be fetched
        // Storing db sqlite to firebase
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }
        //showing progress dialog
        pdialog = new ProgressDialog(MainActivity.this);
        pdialog.setMessage("Processing");
        pdialog.setCancelable(false);
        pdialog.setIndeterminate(false);
        pdialog.show();
        //get sqlitedb
        mExpenseArrayList = sqliteHelper.getAllExpenses();
        mIncomeArrayList = sqliteHelper.getAllIncome();
        //create firebase by id
        //mFirebaseDatabase.child(userId).child("expense").child("description").setValue(expe);

        //create firebase by list
        mFirebaseDatabase.child(userId).child("expense").setValue(mExpenseArrayList);
        mFirebaseDatabase.child(userId).child("income").setValue(mIncomeArrayList);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                addChangeListener();
            }
        }, 2000);



    }

    private void addChangeListener() {
        // Data change listener

        mFirebaseDatabase.child(userId).child("expense").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pdialog.hide();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    //get value from firebase by class
                    Expenses expenses = dataSnapshot1.getValue(Expenses.class);
                    //get value from firebase by id
                    //String desk = (String) dataSnapshot1.child("description").getValue();

                    //if model class is null then trigger the alert dialog
                    if (expenses==null){
                    showdialog();
                    }
                    //if successfully storing data to firebase then showing a snackbar
                    Snackbar snackbar = Snackbar
                            .make(navigationView, "Data Success Synchronize to server", Snackbar.LENGTH_LONG);
                    snackbar.show();

                        Log.e("MainAct", "Exp data is changed!"  +expenses.getDescription());



                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                pdialog.hide();
                showdialog();
                Log.e("MainAct", "Failed to read exp", error.toException());
            }
        });

        mFirebaseDatabase.child(userId).child("income").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pdialog.hide();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    //get value from firebase by class
                    Income income = dataSnapshot1.getValue(Income.class);
                    //get value from firebase by id
                    //String desk = (String) dataSnapshot1.child("description").getValue();

                    //if model class is null then trigger the alert dialog
                    if (income==null){
                        showdialog();
                    }
                    //if successfully storing data to firebase then showing a snackbar
                    Snackbar snackbar = Snackbar
                            .make(navigationView, "Data Success Synchronize to server", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    Log.e("MainAct", "Inc data is changed!"  +income.getDescription());



                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                pdialog.hide();
                showdialog();
                Log.e("MainAct", "Failed to read exp", error.toException());
            }
        });
    }

    private void showdialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        //Setting Dialog
        alertDialog.setTitle("Failed To Synchronize");
        alertDialog.setMessage("Do you want to retry?");
        alertDialog.setIcon(R.mipmap.ic_dash);


        alertDialog.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                // Write your code here to invoke YES event
                synch();
                dialog.cancel();
            }
        });


        alertDialog.setNegativeButton("SKIP", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_menu1:
                fragment = new DashboardFragment();
                break;
            case R.id.nav_menu2:
                fragment = new TransactionFragment();
                break;
            case R.id.nav_menu3:
                synch();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            sqliteHelper.HapusTabel();
            sqliteHelper.CreateTabel();
            displaySelectedScreen(R.id.nav_menu1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
