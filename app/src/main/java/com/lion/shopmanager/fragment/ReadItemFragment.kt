package com.lion.shopmanager.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.shopmanager.MainActivity
import com.lion.shopmanager.R
import com.lion.shopmanager.databinding.FragmentReadItemBinding
import com.lion.shopmanager.repository.ItemRepository
import com.lion.shopmanager.util.FragmentName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ReadItemFragment : Fragment() {

    lateinit var fragmentReadItemBinding: FragmentReadItemBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentReadItemBinding = FragmentReadItemBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // 툴바를 구성하는 메서드
        settingToolbarShowItem()
        // 제품 데이터를 가져와 보여주는 메서드
        settingTextField()

        return fragmentReadItemBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbarShowItem(){
        fragmentReadItemBinding.apply {
            toolbarReadItem.title = "제품 정보 보기"

            toolbarReadItem.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarReadItem.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.READ_ITEM_FRAGMENT)
            }

            toolbarReadItem.inflateMenu(R.menu.toolbar_read_item_menu)
            toolbarReadItem.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.toolbar_read_menu_item_modify -> {
                        // 정보 수정 화면으로 이동한다.
                        // 제품 번호를 추출하여 전달해준다.
                        val itemIdx = arguments?.getInt("itemIdx")
                        val dataBundle = Bundle()
                        dataBundle.putInt("itemIdx", itemIdx!!)
                        mainActivity.replaceFragment(FragmentName.MODIFY_ITEM_FRAGMENT, true, true, dataBundle)
                    }
                    R.id.toolbar_read_menu_item_delete -> {
                        // 다이얼로를 띄운다.
                        val builder = MaterialAlertDialogBuilder(mainActivity)
                        builder.setTitle("제품 정보 삭제")
                        builder.setMessage("제품 정보 삭제시 복구가 불가능합니다")
                        builder.setNegativeButton("취소", null)
                        builder.setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
                            // 삭제 메서드를 호출한다.
                            deleteItemData()
                        }
                        builder.show()
                    }
                }
                true
            }
        }
    }

    // 제품 데이터를 가져와 보여주는 메서드
    fun settingTextField(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                // 제품 번호를 가져온다.
                val itemIdx = arguments?.getInt("itemIdx")
                // 제품 데이터를 가져온다.
                ItemRepository.selectItemDataByItemIdx(mainActivity, itemIdx!!)
            }
            val itemModel = work1.await()

            fragmentReadItemBinding.apply {
                textFieldReadItemName.editText?.setText(itemModel.itemName)
                textFieldReadItemPrice.editText?.setText(itemModel.itemPrice.toString())
                textFieldReadItemAbout.editText?.setText(itemModel.itemAbout)
                textFieldReadItemSellingOrSold.editText?.setText(itemModel.itemSellinOrSold.str)
            }
        }
    }

    // 제품 정보를 삭제하는 메서드
    fun deleteItemData(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                // 삭제한다.
                val itemIdx = arguments?.getInt("itemIdx")!!
                ItemRepository.deleteItemDataByItemIdx(mainActivity, itemIdx)
            }
            work1.join()
            // 제품 목록을 보는 화면으로 돌아간다.
            mainActivity.removeFragment(FragmentName.READ_ITEM_FRAGMENT)
        }
    }
}