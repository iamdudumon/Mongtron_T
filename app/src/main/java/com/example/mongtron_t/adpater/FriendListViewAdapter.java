package com.example.mongtron_t.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mongtron_t.R;
import com.example.mongtron_t.user.AddedFriendDAO;
import com.example.mongtron_t.model.AddedFriendVO;

import java.util.List;

public class FriendListViewAdapter extends BaseAdapter {
    List<AddedFriendVO> items;

    public FriendListViewAdapter(){
        items = AddedFriendVO.friendsList;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        AddedFriendVO addedFriendVO = (AddedFriendVO) this.getItem(position);
        AddedFriendDAO addedFriendDAO = new AddedFriendDAO(context);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friend_list_view_item, parent, false);
        }

        ImageView addedFriendImgView = convertView.findViewById(R.id.addedFriendImgView);
        TextView addedFriendNickNameTextView = convertView.findViewById(R.id.addedFriendNickNameTextView);
        TextView addedFriendDistanceTextView = convertView.findViewById(R.id.addedFriendDistanceTextView);
        TextView addedFriendGpsStateTextView = convertView.findViewById(R.id.addedFriendGpsStateTextView);
        ImageButton addedFriendDeleteButton = convertView.findViewById(R.id.addedFriendDeleteButton);

        int marketImg = R.drawable.profile_null_img;
        switch (addedFriendVO.getFriendSex()) {                            //기본 값은 null 이미지고 성별에 따른 아이콘 이미지가 변환
            case '1':
                marketImg = R.drawable.profile_male_img;
                break;
            case '2':
                marketImg = R.drawable.profile_female_img;
                break;
            default:
                break;
        }

        addedFriendImgView.setImageResource(marketImg);
        addedFriendNickNameTextView.setText(addedFriendVO.getFriendNickName().toString());
        addedFriendDistanceTextView.setText(addedFriendVO.getDistance() > 0 ? String.valueOf(Math.round(addedFriendVO.getDistance()*100)/100.0)  + " km" : "?? km");
        addedFriendGpsStateTextView.setText("GPS: " + (addedFriendVO.isFriendGpsState() ? "ON" : "OFF"));

        addedFriendDeleteButton.setOnClickListener(new View.OnClickListener() {                     //리스트 뷰의 각 아이템의 버튼 클릭 시 친구 삭제
            @Override
            public void onClick(View v) {
                int friendId = addedFriendVO.getFriendId();
                addedFriendDAO.deleteAddedFriend(friendId);              //db 에서 삭제
                AddedFriendVO.friendsList.remove(position);              //메모리 상에 삭제
                FriendListViewAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
