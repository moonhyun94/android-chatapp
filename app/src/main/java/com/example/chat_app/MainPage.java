package com.example.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chat_app.fragment.Frag1;
import com.example.chat_app.fragment.Frag2;
import com.example.chat_app.fragment.Frag3;
import com.example.chat_app.fragment.Frag4;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction fTransaction;
    private Frag1 frag1 = new Frag1();
    private Frag2 frag2 = new Frag2();
    private Frag3 frag3 = new Frag3();
    private Frag4 frag4 = new Frag4();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        Intent mainPageIntent = getIntent();

        fTransaction = fragmentManager.beginTransaction();
        fTransaction.replace(R.id.mainFrame, frag1).commitAllowingStateLoss();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               fTransaction = fragmentManager.beginTransaction();

                switch (menuItem.getItemId()) {
                    case R.id.main_menu:
                        fTransaction.replace(R.id.mainFrame, frag1).commitAllowingStateLoss();
                        break;

                    case R.id.chat_menu:
                        fTransaction.replace(R.id.mainFrame, frag2).commitAllowingStateLoss();
                        break;

                    case R.id.story_menu:
                        fTransaction.replace(R.id.mainFrame, frag3).commitAllowingStateLoss();
                        break;

                    case R.id.setting_menu:
                        fTransaction.replace(R.id.mainFrame, frag4).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });
    }
}
