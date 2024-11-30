package com.lion.shopmanager.model

import com.lion.shopmanager.util.ItemSellingOrSold

data class ItemModel (
    // 제품 번호
    var itemIdx:Int,
    // 제품 이름
    var itemName:String,
    // 제품 가격
    var itemPrice:Int,
    // 제품 정보
    var itemAbout:String,
    // 판매 유무
    var itemSellinOrSold:ItemSellingOrSold
    // 제품 사진
    // var itemImage:String
)