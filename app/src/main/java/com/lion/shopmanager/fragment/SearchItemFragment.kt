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
import com.lion.shopmanager.databinding.FragmentSearchItemBinding
import com.lion.shopmanager.databinding.RowText2Binding
import com.lion.shopmanager.model.ItemModel
import com.lion.shopmanager.repository.ItemRepository
import com.lion.shopmanager.util.FragmentName
import com.lion.shopmanager.util.ItemSellingOrSold
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

class SearchItemFragment : Fragment() {

    lateinit var fragmentSearchItemFragment: FragmentSearchItemBinding
    lateinit var mainActivity: MainActivity

    var itemList = mutableListOf<ItemModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentSearchItemFragment = FragmentSearchItemBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // 툴바를 구성하는 메서드
        settingToolbarSearchItem()
        // recyclerView를 구성하는 메서드
        settingRecyclerViewSearchItem()
        // 입력 요소 설정 메서드를 호출한다.
        settingTextField()

        return fragmentSearchItemFragment.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbarSearchItem(){
        fragmentSearchItemFragment.apply {
            toolbarSearchItem.title = "제품 정보 검색"

            toolbarSearchItem.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarSearchItem.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.SEARCH_ITEM_FRAGMENT)
            }
        }
    }

    // 입력 요소 설정
    fun settingTextField(){
        fragmentSearchItemFragment.apply {
            // 검색창에 포커스를 준다.
            mainActivity.showSoftInput(textFieldSearchItemName.editText!!)
            // 키보드의 엔터를 누르면 동작하는 리스너
            textFieldSearchItemName.editText?.setOnEditorActionListener { v, actionId, event ->
                // 검색 데이터를 가져와 보여준다.
                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO){
                        val keyword = textFieldSearchItemName.editText?.text.toString()
                        ItemRepository.selectItemDataAllByItemName(mainActivity, keyword)
                    }
                    itemList = work1.await()
                    recyclerViewSearchItem.adapter?.notifyDataSetChanged()
                }
                mainActivity.hideSoftInput()
                true
            }
        }
    }

    // recyclerView를 구성하는 메서드
    fun settingRecyclerViewSearchItem(){
        fragmentSearchItemFragment.apply {
            recyclerViewSearchItem.adapter = RecyclerViewMainAdapter()
            recyclerViewSearchItem.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewSearchItem.addItemDecoration(deco)
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

            holder.rowText2Binding.textViewRowName.text = item.itemName
            holder.rowText2Binding.textViewDate.text = item.itemDate

            // 이미지 설정
            if (item.itemImage.isNotEmpty()) {
                val imageFile = File(item.itemImage)
                if (imageFile.exists() && imageFile.isFile) {
                    // 유효한 이미지 파일이 있는 경우 URI 설정
                    holder.rowText2Binding.imageListItemView.setImageURI(item.itemImage.toUri())
                } else {
                    // 이미지 파일이 없거나 유효하지 않으면 기본 이미지 설정
                    holder.rowText2Binding.imageListItemView.setImageResource(R.drawable.image_24px)
                }
            } else {
                // 이미지 경로가 비어 있으면 기본 이미지 설정
                holder.rowText2Binding.imageListItemView.setImageResource(R.drawable.image_24px)
            }


            // 판매 상태에 따라 텍스트 색상 및 배경 색상 설정
            if (item.itemSellinOrSold == ItemSellingOrSold.ITEM_SOLD) {
                // 판매완료 상태
                holder.rowText2Binding.root.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.sold_color_background)
                )
            }
        }
    }
}