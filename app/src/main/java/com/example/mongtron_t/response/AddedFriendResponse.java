package com.example.mongtron_t.response;

import com.example.mongtron_t.model.AddedFriendVO;

import java.util.List;

public class AddedFriendResponse {
    private final List<AddedFriendVO> addedFriendVOList;

    public AddedFriendResponse(List<AddedFriendVO> addedFriendVOList) {
        this.addedFriendVOList = addedFriendVOList;
    }

    public List<AddedFriendVO> getAddedFriendVOList() {
        return addedFriendVOList;
    }
}
