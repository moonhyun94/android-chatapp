package com.example.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chat_app.fragment.ChatRoomFragment;
import com.example.chat_app.fragment.FriendPageFragment;
import com.example.chat_app.fragment.MyPageFragment;
import com.example.chat_app.fragment.StoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPageNavigation extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction fTransaction;

    private FriendPageFragment friendPageFragment = new FriendPageFragment();
    private ChatRoomFragment chatRoomFragment = new ChatRoomFragment();
    private StoryFragment storyFragment = new StoryFragment();
    private MyPageFragment myPageFragment = new MyPageFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        Intent mainPageIntent = getIntent();

        fTransaction = fragmentManager.beginTransaction();
        fTransaction.replace(R.id.mainFrame, friendPageFragment).commitAllowingStateLoss();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                fTransaction = fragmentManager.beginTransaction();

                switch (menuItem.getItemId()) {
                    case R.id.friend_menu:
                        fTransaction.replace(R.id.mainFrame, friendPageFragment).commitAllowingStateLoss();
                        break;

                    case R.id.chat_menu:
                        fTransaction.replace(R.id.mainFrame, chatRoomFragment).commitAllowingStateLoss();
                        break;

                    case R.id.story_menu:
                        fTransaction.replace(R.id.mainFrame, storyFragment).commitAllowingStateLoss();
                        break;

                    case R.id.mypage_menu:
                        fTransaction.replace(R.id.mainFrame, myPageFragment).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });
    }
}
