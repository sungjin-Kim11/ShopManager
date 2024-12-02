package com.lion.shopmanager.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lion.shopmanager.VO.ItemVO

@Dao
interface ItemDao {

    // 제품 정보 저장
    @Insert
    fun insertItemData(itemVO: ItemVO)

    // 모든 제품 정보를 가져오는 메서드
    @Query("""
        select * from ItemTable
        order by itemIdx
    """)
    fun selectItemAll() : List<ItemVO>

    // 제품 이름으로 제품 정보를 가져오는 메서드
    @Query("""
        select * from ItemTable
        where itemName = :itemName
        order by itemIdx desc
    """)
    fun selectItemDataAllByItemName(itemName:String):List<ItemVO>

    // 제품 하나의 정보를 가져오는 메서드
    @Query("""
        select * from ItemTable
        where itemIdx = :itemIdx
    """)
    fun selectItemDataByItemIdx(itemIdx:Int) : ItemVO

    // 제품 하나의 정보를 삭제하는 메서드
    @Delete
    fun deleteItemData(itemVO: ItemVO)

    // 제품 한 개의 정보를 수정하는 메서드
    @Update
    fun updateItemData(itemVO: ItemVO)
}