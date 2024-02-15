package com.example.mongtron_t.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.mongtron_t.R;
import com.example.mongtron_t.adpater.FriendListViewAdapter;
import com.example.mongtron_t.service.AddedFriendService;

public class FriendListActivity extends AppCompatActivity {
//    AddedFriendService addedFriendService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

//        addedFriendService = new AddedFriendService(this);

        ListView addedFriendListView = findViewById(R.id.addedFriendListView);
        FriendListViewAdapter addedFriendVOArrayAdapter = new FriendListViewAdapter();
        addedFriendListView.setAdapter(addedFriendVOArrayAdapter);

        initButton();
    }

    private void initButton(){
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //친구 정보 업데이트 버튼튼
//       ImageButton friendUpdateButton = findViewById(R.id.friendUpdateButton);
//        friendUpdateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AddedFriendService addedFriendService = new AddedFriendService(getApplicationContext());
//                addedFriendService.getServerFriendList();
//            }
//        });
    }
}