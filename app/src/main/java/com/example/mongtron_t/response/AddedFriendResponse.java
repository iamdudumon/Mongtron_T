package com.example.mongtron_t.response;

import com.example.mongtron_t.model.AddedFriendVO;

import java.util.List;

public class AddedFriendResponse {
    private final int code;
    private final List<AddedFriendVO> addedFriendVOList;

    public AddedFriendResponse(int code, List<AddedFriendVO> addedFriendVOList) {
        this.code = code;
        this.addedFriendVOList = addedFriendVOList;
    }

    public int getCode() {
        return code;
    }

    public List<AddedFriendVO> getAddedFriendVOList() {
        return addedFriendVOList;
    }
}
