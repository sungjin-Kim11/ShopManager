package com.lion.shopmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lion.shopmanager.VO.ItemVO
import com.lion.shopmanager.dao.ItemDao

@Database(entities = [ItemVO::class], version = 1, exportSchema = true)
abstract class ItemDatabase : RoomDatabase(){
    // dao
    abstract fun itemDao() : ItemDao

    companion object{
        // 데이터 베이스 객체를 담을 변수
        var itemDatabase:ItemDatabase? = null
        @Synchronized
        fun getInstance(context: Context) : ItemDatabase?{
            synchronized(ItemDatabase::class){
                itemDatabase = Room.databaseBuilder(
                    context.applicationContext, ItemDatabase::class.java,
                    "Item.db"
                ).build()
            }
            return itemDatabase
        }

        // 데이터 베이스 객체가 소멸될 때 호출되는 메서드
        fun destroyInstance(){
            itemDatabase = null
        }
    }
}