package com.example.database;

import com.example.camera.R;

public enum LayoutName {
    LATTICE(Integer.valueOf(0), R.mipmap.lattice_land, R.mipmap.lattice_small),
    R_PERSON(Integer.valueOf(1), R.mipmap.right_person_land, R.mipmap.right_person_small),
    L_PERSON(Integer.valueOf(2), R.mipmap.left_person_land, R.mipmap.left_person_small);

    private Integer num;
    private Integer layoutID;
    private Integer thumbnailID;

    private LayoutName(Integer num, Integer layoutID, Integer thumbnailID){
        this.num = num;
        this.layoutID = layoutID;
        this.thumbnailID = thumbnailID;
    }

    public Integer getNumber() {
        return this.num;
    }

    public Integer getLayoutID() {
        return this.layoutID;
    }

    public Integer getThumbnailID() {
        return this.thumbnailID;
    }
}
