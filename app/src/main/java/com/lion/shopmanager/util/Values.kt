package com.lion.shopmanager.util

// Fragment들의 이름
enum class FragmentName(val number:Int, var str:String) {
    ITEM_LIST_FRAGMENT(1, "ItemListFragment"),
    ADD_ITEM_FRAGMENT(2, "AddItemFragment"),
    READ_ITEM_FRAGMENT(3, "ReadItemFragment"),
    MODIFY_ITEM_FRAGMENT(4, "ModifyItemFragment"),
    FILTER_SELLING_ITEM_FRAGMENT(5, "FilterSellingItemFragment"),
    FILTER_SOLD_ITEM_FRAGMENT(6, "FilterSoldItemFragment")
}

// 판매 유무를 나타내는 값
enum class ItemSellingOrSold(var number:Int, var str:String){
    ITEM_SELLING(1, "판매중"),
    ITEM_SOLD(2, "판매완료")
}

fun numberToItemSellingOrSold(itemSellingOrSold: Int) = when(itemSellingOrSold){
    1 -> ItemSellingOrSold.ITEM_SELLING
    else -> ItemSellingOrSold.ITEM_SOLD
}


class ValueClass{
    companion object{
        // 전체를 의미하는 값
        val VALUE_ALL = 0
    }
}

