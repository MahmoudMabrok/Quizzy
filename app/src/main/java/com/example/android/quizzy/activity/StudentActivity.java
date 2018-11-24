package com.example.android.quizzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.fragment.StudentReports;
import com.example.android.quizzy.fragment.student_quiz_list;
import com.example.android.quizzy.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public String studentID = "1";
    @BindView(R.id.containerStudent)
    FrameLayout containerStudent;
    FragmentManager manager = getSupportFragmentManager();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private FragmentTransaction transition;

    private DataRepo repo = new DataRepo();
    public String studentName;
    public String teacherUUID;

    private static final String TAG = "StudentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        teacherUUID = intent.getStringExtra(Constants.TEACHERS_KEY);
        if (teacherUUID != null) {
            show("tid " + teacherUUID);
            // studentID = repo.getUUID();
            studentID = "1";
            repo.getStudentName(studentID, teacherUUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    show("" + dataSnapshot);
                    studentName = (String) dataSnapshot.getValue();
                    Log.d(TAG, dataSnapshot + " onDataChange: " + studentName);
                    show("student_name " + studentName);
                    openQuizzListFragment();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

    }

    private void show(String studentName) {
        Toast.makeText(this, studentName, Toast.LENGTH_SHORT).show();
    }

    private void openQuizzListFragment() {
        transition = manager.beginTransaction();
        transition.setCustomAnimations(R.anim.slide_up, 0);
        student_quiz_list teacher = new student_quiz_list();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.STUDENT_NAME, studentName);
        bundle.putString(Constants.TEACHERS_KEY, teacherUUID);
        teacher.setArguments(bundle);
        transition.replace(R.id.containerStudent, teacher).commit();

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FirebaseAuth.getInstance().signOut();
            // teacherUUID = "0114919427";
            studentID = "1";
            repo.getCompleteListRef(teacherUUID, studentID).removeValue();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_quiz:
                openQuizzListFragment();
                break;

            case R.id.nav_reorts:
                openReports();
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openReports() {
        transition = manager.beginTransaction();
        StudentReports studentReports = new StudentReports();
        transition.replace(R.id.containerStudent, studentReports).commit();
    }
}
