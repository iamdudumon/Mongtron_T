package com.example.mongtron_t.dialog;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mongtron_t.R;
import com.example.mongtron_t.adpater.NationalityListViewAdapter;
import com.example.mongtron_t.model.UserInfo;


public class SignupAddNationalityDialog extends DialogFragment {
    View view;
    View addView;
    String nationality;
    int nationalityDrawable;

    public SignupAddNationalityDialog(View addView){
        this.addView = addView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListView();
        initButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_signup_add_nationality, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    private void initListView() {
        ListView nationalityListView = view.findViewById(R.id.nationalityListView);
        NationalityListViewAdapter nationalityListViewAdapter = new NationalityListViewAdapter();

        Pair<LinearLayout, ImageView>[] listViewItems = new Pair[2];
        //0번 index가 이전의 선택된 item, 1번 index가 지금 선택된 item
        listViewItems[0] = new Pair<>(null, null);
        listViewItems[1] = new Pair<>(null, null);

        nationalityListView.setAdapter(nationalityListViewAdapter);
        nationalityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //국적 선택시 배경이랑 왼쪽에 체크 무늬 이미지 표시
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listViewItems[0].first != null && listViewItems[0].second != null) {
                    listViewItems[0].first.setBackgroundColor(getResources().getColor(R.color.nationality_basic));
                    listViewItems[0].second.setImageResource(0);
                }

                listViewItems[1] = new Pair<>(view.findViewById(R.id.listViewItem), view.findViewById(R.id.listViewCheckImageView));
                listViewItems[1].first.setBackgroundColor(getResources().getColor(R.color.nationality_select));
                listViewItems[1].second.setImageResource(R.drawable.check_icon_img);
                listViewItems[0] = listViewItems[1];

                nationality = nationalityListViewAdapter.getNationalityList().get(position).first;
                nationalityDrawable = nationalityListViewAdapter.getNationalityList().get(position).second;
            }
        });
    }

    private void initButton() {
        android.widget.Button nationalityDialogCancelButton = view.findViewById(R.id.nationalityDialogCancelButton);
        android.widget.Button nationalityDialogSaveButton = view.findViewById(R.id.nationalityDialogSaveButton);

        nationalityDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        nationalityDialogSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nationalityText = addView.findViewById(R.id.nationalityText);
                nationalityText.setText(nationality);
                nationalityText.setTextColor(getResources().getColor(R.color.black));

                Drawable originalDrawable = ContextCompat.getDrawable(requireContext(), nationalityDrawable);
                Drawable resizedDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) originalDrawable).getBitmap(), 30, 30, true));

                ImageView nationalityImg = addView.findViewById(R.id.nationalityImg);
                nationalityImg.setImageDrawable(resizedDrawable);

                UserInfo.getInstance().setNationality(nationality);
                dismiss();
            }
        });
    }

}
