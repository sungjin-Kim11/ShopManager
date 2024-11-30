package com.lion.shopmanager.VO

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ItemTable")
data class ItemVO(
    // 제품 번호
    @PrimaryKey(autoGenerate = true)
    var itemIdx:Int = 0,
    // 제품 이름
    var itemName:String = "",
    // 제품 가격
    var itemPrice:Int = 0,
    // 제품 정보
    var itemAbout:String = "",
    // 제품 유무
    var itemSellinOrSold: Int = 0
    // 제품 사진
    // var itemImage:String = ""
)