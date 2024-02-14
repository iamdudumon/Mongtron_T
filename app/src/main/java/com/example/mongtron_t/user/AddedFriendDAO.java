package com.example.mongtron_t.user;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


import com.example.mongtron_t.http.RetrofitClient;
import com.example.mongtron_t.http.RetrofitRxClient;
import com.example.mongtron_t.model.AddedFriendVO;
import com.example.mongtron_t.response.AddedFriendResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class AddedFriendDAO extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Mongtron_t.db";
    private static final String TABLE_NAME = "friend";
    private static final int DATABASE_VERSION = 1;

    public AddedFriendDAO(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME + "(\n" +
                "\tfriendId         INT NOT NULL,\n" +
                "\tfriendNickName   INT,\n" +
                "\tfriendSex        CHAR(1),\n" +
                "\tfriendGpsState   CHAR(1),\n" +
                "\tdistance         DOUBLE,\n" +
                "\tPRIMARY KEY      (friendId)\n" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists " + TABLE_NAME;

        db.execSQL(sql);
        onCreate(db);
    }


    public void deleteAddedFriend(int friendId){
        //서버측 db 에서 친구 목록 삭제
        RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.friendDeletePatch(friendId);

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "delete from " + TABLE_NAME + " where friendId = " + String.valueOf(friendId);
        db.execSQL(sql);
        db.close();
    }

    public void insertAddedFriend(AddedFriendVO friendVO){
        //서버측으로 친구를 추가했다고 알려줌
        RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.friendAddPost(friendVO.getFriendId());
        //내부 db에 친구 추가
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "insert into " + TABLE_NAME + " values(?, ?, ?, ?, ?)";
        db.execSQL(sql, new Object[]{friendVO.getFriendId(), friendVO.getFriendNickName(), friendVO.getFriendSex(),friendVO.isFriendGpsState(), friendVO.getDistance()});
        db.close();
    }

    public boolean isAddedFriendVO(int friendId){                                              //이미 친구 테이블에 존재하는지 검사
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select friendId from " + TABLE_NAME +" where friendId = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(friendId)});

        boolean result = cursor.moveToFirst();
        cursor.close();
        db.close();
        return result;
    }

    public List<AddedFriendVO> getAddedFriendList(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<AddedFriendVO> addedFriendVOList = new ArrayList<>();
        String sql = "select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            AddedFriendVO temp = new AddedFriendVO(cursor.getInt(0), cursor.getString(1), cursor.getString(2).charAt(0),(Objects.equals(cursor.getString(3), "1")), cursor.getDouble(4));
            addedFriendVOList.add(temp);
        }
        cursor.close();
        db.close();

        return addedFriendVOList;
    }

    public void autoLogin(){
        if(getAddedFriendList().size() == 0)
            AddedFriendVO.friendsList = new ArrayList<>();
        else
            AddedFriendVO.friendsList = getAddedFriendList();
    }

    public void deleteAllAddedFriend(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "delete from " + TABLE_NAME;
        db.execSQL(sql);
        db.close();
    }

    public void insertAddedFriendList(){                        //메모리에 올라간 친구 리스트 모두를 내부 db에 저장
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i = 0; i < AddedFriendVO.friendsList.size(); i++) {
            AddedFriendVO friendVO = AddedFriendVO.friendsList.get(i);
            String sql = "insert into " + TABLE_NAME + " values(?, ?, ?, ?, ?)";
            db.execSQL(sql, new Object[]{friendVO.getFriendId(), friendVO.getFriendNickName(), friendVO.getFriendSex(), friendVO.isFriendGpsState(), friendVO.getDistance()});
        }
        db.close();
    }

    public void getServerFriendList(){
        RetrofitRxClient retrofitRxClient = new RetrofitRxClient();

        RetrofitRxClient.disposable.add(retrofitRxClient.friendListGet()
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(new DisposableSingleObserver<AddedFriendResponse>(){
                    @Override
                    public void onSuccess(@NonNull AddedFriendResponse addedFriendResponse) {
                        AddedFriendVO.friendsList = addedFriendResponse.getAddedFriendVOList();             //메모리에 서버로부터 받은 친구 리스트를 저장
                        insertAddedFriendList();                                                            //내부 db에 서버로부터 받은 친구 리스트를 table 에 저장
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TAG", "Server 와 Disconnected!");
                    }
                })
        );
    }
}
