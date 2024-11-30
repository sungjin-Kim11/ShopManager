package com.lion.shopmanager.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lion.shopmanager.MainActivity
import com.lion.shopmanager.R
import com.lion.shopmanager.databinding.FragmentItemListBinding
import com.lion.shopmanager.databinding.RowText1Binding
import com.lion.shopmanager.databinding.RowText2Binding
import com.lion.shopmanager.model.ItemModel
import com.lion.shopmanager.repository.ItemRepository
import com.lion.shopmanager.util.FragmentName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ItemListFragment : Fragment() {

    lateinit var fragmentItemListFragment: FragmentItemListBinding
    lateinit var mainActivity: MainActivity

//    // RecyclerView 구성을 위한 임시데이터
//    val tempData1 = Array(100){
//        "제품 ${it+1}"
//    }
    var itemList = mutableListOf<ItemModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentItemListFragment = FragmentItemListBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity

        // Toolbar를 구성하는 메서드를 호출한다.
        settingToolbar()
        // RecyclerView를 구성하는 메서드를 호출한다.
        settingRecyclerView()
        // 버튼을 설정하는 메서드를 호출한다.
        settingButton()
        // 제품 정보를 가져와 RecyclerView를 갱신하는 메서드를 호출한다.
        refreshRecyclerView()


        return fragmentItemListFragment.root
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbar(){
        fragmentItemListFragment.apply {
            toolbarItemList.title = "전체 제품 목록"
            toolbarItemList.setNavigationIcon(R.drawable.menu_24px)
            toolbarItemList.setNavigationOnClickListener {
                mainActivity.activityMainBinding.drawerLayoutMain.open()
            }

            // 메뉴
            toolbarItemList.inflateMenu(R.menu.toolbar_item_list_menu)
            toolbarItemList.setOnMenuItemClickListener {
                when (it.itemId) {
                    // 검색
                    R.id.toolbar_item_list_search -> {

                    }
                }
                true
            }
        }
    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView(){
        fragmentItemListFragment.apply {
            recyclerViewItemList.adapter = RecyclerViewMainAdapter()
            recyclerViewItemList.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewItemList.addItemDecoration(deco)

        }
    }

    // 버튼을 설정하는 메서드
    fun settingButton(){
        fragmentItemListFragment.apply {
            // fab를 누를 때
            fabItemListAdd.setOnClickListener {
                mainActivity.replaceFragment(FragmentName.ADD_ITEM_FRAGMENT, true, true, null)
            }
        }
    }

    // 데이터 베이스에서 데이터를 읽어와 RecyclerView를 갱신한다.
    fun refreshRecyclerView(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                // 데이터를 읽어온다.
                ItemRepository.selectItemDataAll(mainActivity)
            }
            itemList = work1.await()
            // RecyclerView를 갱신한다.
            fragmentItemListFragment.recyclerViewItemList.adapter?.notifyDataSetChanged()
        }
    }

    // RecyclerView의 어뎁터
    inner class RecyclerViewMainAdapter : RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolderMain>() {
        // ViewHolder
        inner class ViewHolderMain(val rowText2Binding: RowText2Binding) : RecyclerView.ViewHolder(rowText2Binding.root), OnClickListener {
            override fun onClick(v: View?) {
                // 사용자가 누른 제품의 제품 번호를 담아준다.
                val dataBundle = Bundle()
                dataBundle.putInt("itemIdx", itemList[adapterPosition].itemIdx)
                // ShowFragment로 이동한다.
                mainActivity.replaceFragment(FragmentName.READ_ITEM_FRAGMENT, true, true, dataBundle)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {
            val rowText2Binding = RowText2Binding.inflate(layoutInflater, parent, false)
            val viewHolderMain = ViewHolderMain(rowText2Binding)
            rowText2Binding.root.setOnClickListener(viewHolderMain)
            return viewHolderMain
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
            holder.rowText2Binding.textViewRowName.text = itemList[position].itemName
        }
    }
}