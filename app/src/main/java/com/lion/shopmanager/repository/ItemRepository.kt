package com.lion.shopmanager.repository

import android.content.Context
import android.util.Log
import com.lion.shopmanager.VO.ItemVO
import com.lion.shopmanager.database.ItemDatabase
import com.lion.shopmanager.model.ItemModel
import com.lion.shopmanager.util.numberToItemSellingOrSold

class ItemRepository {
    companion object {

        // 제품 정보를 저장하는 메서드
        fun insertItemData(context: Context, itemModel: ItemModel) {
            // 데이터를 VO 객체에 담는다.
            val itemVO = ItemVO(
                itemName = itemModel.itemName,
                itemPrice = itemModel.itemPrice,
                itemAbout = itemModel.itemAbout,
                itemSellinOrSold = itemModel.itemSellinOrSold.number,
                itemImage = itemModel.itemImage,
                itemDate = itemModel.itemDate
            )
            // 저장한다.
            val itemDatabase = ItemDatabase.getInstance(context)
            itemDatabase?.itemDao()?.insertItemData(itemVO)

            Log.d("ItemRepository", "Data: $itemVO")
        }

        // 제품 데이터 전체를 가져오는 메서드
        fun selectItemDataAll(context: Context): MutableList<ItemModel> {
            // 데이터를 가져온다.
            val itemDatabase = ItemDatabase.getInstance(context)
            val itemList = itemDatabase?.itemDao()?.selectItemAll()

            // 제품 데이터를 담을 리스트
            val tempList = mutableListOf<ItemModel>()

            // 제품의 수 만큼 반복한다.
            itemList?.forEach {
                val itemModel = ItemModel(
                    it.itemIdx, it.itemName, it.itemPrice, it.itemAbout,
                    numberToItemSellingOrSold(it.itemSellinOrSold),
                    it.itemImage, it.itemDate
                )
                // 리스트에 담는다.
                tempList.add(itemModel)
                Log.d("ItemRepository", "Data: $itemModel")
            }
            return tempList
        }

        // 제품 한 개의 데이터를 가져오는 메서드
        fun selectItemDataByItemIdx(context: Context, itemIdx: Int): ItemModel {
            val itemDatabase = ItemDatabase.getInstance(context)
            // 제품 데이터를 가져온다.
            val itemVo = itemDatabase?.itemDao()?.selectItemDataByItemIdx(itemIdx)

            Log.d("ItemRepository", "Data: $itemVo")

            // Model 객체에 담는다.
            val itemModel = ItemModel(
                itemVo!!.itemIdx, itemVo.itemName, itemVo.itemPrice, itemVo.itemAbout,
                numberToItemSellingOrSold(itemVo.itemSellinOrSold),
                itemVo.itemImage, itemVo.itemDate
            )
            return itemModel
        }

        // 제품 정보를 삭제하는 메서드
        fun deleteItemDataByItemIdx(context: Context, itemIdx: Int) {
            // 삭제한다.
            val itemDatabase = ItemDatabase.getInstance(context)
            val itemVO = ItemVO(itemIdx = itemIdx)
            itemDatabase?.itemDao()?.deleteItemData(itemVO)
        }

        // 제품 정보를 수정하는 메서드
        fun updateItemDataByItemIdx(context: Context, itemModel: ItemModel) {
            // VO에 데이터를 담는다.
            val itemVO = ItemVO(
                itemModel.itemIdx, itemModel.itemName, itemModel.itemPrice, itemModel.itemAbout,
                itemModel.itemSellinOrSold.number,
                itemModel.itemImage, itemModel.itemDate
            )
            // 수정하는 메서드를 호출한다.
            val itemDatabase = ItemDatabase.getInstance(context)
            itemDatabase?.itemDao()?.updateItemData(itemVO)
        }
    }
}