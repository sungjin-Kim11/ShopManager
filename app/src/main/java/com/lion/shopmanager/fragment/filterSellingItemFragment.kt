package com.lion.shopmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lion.shopmanager.MainActivity
import com.lion.shopmanager.R
import com.lion.shopmanager.databinding.FragmentFilterSellingItemBinding
import com.lion.shopmanager.databinding.RowText2Binding
import com.lion.shopmanager.model.ItemModel
import com.lion.shopmanager.repository.ItemRepository
import com.lion.shopmanager.util.FragmentName
import com.lion.shopmanager.util.ItemSellingOrSold
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class filterSellingItemFragment : Fragment() {

    lateinit var fragmentFilterSellingItemBinding: FragmentFilterSellingItemBinding
    lateinit var mainActivity: MainActivity

    var itemList = mutableListOf<ItemModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentFilterSellingItemBinding = FragmentFilterSellingItemBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity

        // Toolbar를 구성하는 메서드를 호출한다.
        settingToolbar()
        // RecyclerView를 구성하는 메서드를 호출한다.
        settingRecyclerView()
        // 제품 정보를 가져와 RecyclerView를 갱신하는 메서드를 호출한다.
        refreshRecyclerView()

        return fragmentFilterSellingItemBinding.root
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbar(){
        fragmentFilterSellingItemBinding.apply {
            toolbarSellingItemList.title = "판매중인 제품 목록"
            toolbarSellingItemList.setNavigationIcon(R.drawable.menu_24px)
            toolbarSellingItemList.setNavigationOnClickListener {
                mainActivity.activityMainBinding.drawerLayoutMain.open()
            }
        }
    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView(){
        fragmentFilterSellingItemBinding.apply {
            recyclerViewSellingItemList.adapter = RecyclerViewMainAdapter()
            recyclerViewSellingItemList.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewSellingItemList.addItemDecoration(deco)

        }
    }

    fun refreshRecyclerView() {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                // 모든 데이터를 가져옵니다.
                ItemRepository.selectItemDataAll(mainActivity)
            }

            // 전체 데이터를 가져온 후 판매중인 데이터만 필터링
            itemList = work1.await().filter { it.itemSellinOrSold == ItemSellingOrSold.ITEM_SELLING }.toMutableList()

            // RecyclerView 갱신
            fragmentFilterSellingItemBinding.recyclerViewSellingItemList.adapter?.notifyDataSetChanged()
        }
    }


    // RecyclerView의 어뎁터
    inner class RecyclerViewMainAdapter : RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolderMain>() {
        // ViewHolder
        inner class ViewHolderMain(val rowText2Binding: RowText2Binding) : RecyclerView.ViewHolder(rowText2Binding.root),
            OnClickListener {
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
            val item = itemList[position]

            // 텍스트 및 이미지 설정
            holder.rowText2Binding.textViewRowName.text = item.itemName
            holder.rowText2Binding.imageListItemView.setImageURI(item.itemImage.toUri())
            holder.rowText2Binding.textViewDate.text = item.itemDate
        }

    }
}