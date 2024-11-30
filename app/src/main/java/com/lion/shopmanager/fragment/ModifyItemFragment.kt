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
import com.lion.shopmanager.databinding.FragmentModifyItemBinding
import com.lion.shopmanager.model.ItemModel
import com.lion.shopmanager.repository.ItemRepository
import com.lion.shopmanager.util.FragmentName
import com.lion.shopmanager.util.ItemSellingOrSold
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ModifyItemFragment : Fragment() {

    lateinit var fragmentModifyItemBinding: FragmentModifyItemBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentModifyItemBinding = FragmentModifyItemBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // Toolbar를 구성하는 메서드
        settingToolbar()

        // 입력 요소들 초기 설정
        settingInput()


        return fragmentModifyItemBinding.root
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbar() {
        fragmentModifyItemBinding.apply {
            toolbarModifyItem.title = "제품 정보 수정"
            // 네비게이션 아이콘을 설정하고 누를 경우 NavigationView가 나타나도록 한다.
            toolbarModifyItem.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarModifyItem.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.MODIFY_ITEM_FRAGMENT)
            }

            // 메뉴
            toolbarModifyItem.inflateMenu(R.menu.toolbar_modify_item_menu)
            toolbarModifyItem.setOnMenuItemClickListener {
                when (it.itemId) {
                    // 완료
                    R.id.toolbar_modify_item_check -> {
                        modifyDone()
                    }
                }
                true
            }
        }
    }

    // 입력 요소들 초기 설정
    fun settingInput(){
        fragmentModifyItemBinding.apply {
            // 제품 번호를 가져온다.
            val itemIdx = arguments?.getInt("itemIdx")!!
            // 제품 데이터를 가져온다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    ItemRepository.selectItemDataByItemIdx(mainActivity, itemIdx)
                }
                val itemViewModel = work1.await()
                textModifyLayoutName.editText?.setText(itemViewModel.itemName)
                textModifyLayoutPrice.editText?.setText(itemViewModel.itemPrice.toString())
                textModifyLayoutAbout.editText?.setText(itemViewModel.itemAbout)

                when(itemViewModel.itemSellinOrSold){
                    ItemSellingOrSold.ITEM_SELLING -> toggleModifySellingOrSold.check(R.id.buttonModifySelling)
                    ItemSellingOrSold.ITEM_SOLD -> toggleModifySellingOrSold.check(R.id.buttonModifySold)
                }
            }
        }
    }

    // 수정 처리 메서드
    fun modifyDone(){
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(mainActivity)
        materialAlertDialogBuilder.setTitle("수정")
        materialAlertDialogBuilder.setMessage("이전 데이터로 복원할 수 없습니다")
        materialAlertDialogBuilder.setNeutralButton("취소", null)
        materialAlertDialogBuilder.setPositiveButton("수정"){ dialogInterface: DialogInterface, i: Int ->
            // 수정할 데이터
            val itemIdx = arguments?.getInt("itemIdx")!!
            val itemName = fragmentModifyItemBinding.textModifyLayoutName.editText?.text.toString()
            val itemPrice = fragmentModifyItemBinding.textModifyLayoutPrice.editText?.text.toString().toInt()
            val itemAbout = fragmentModifyItemBinding.textModifyLayoutAbout.editText?.text.toString()
            val itemSellinOrSold = when(fragmentModifyItemBinding.toggleModifySellingOrSold.checkedButtonId){
                R.id.buttonModifySelling -> ItemSellingOrSold.ITEM_SELLING
                else -> ItemSellingOrSold.ITEM_SOLD
            }

            val itemViewModel = ItemModel(itemIdx, itemName, itemPrice, itemAbout, itemSellinOrSold)

            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    ItemRepository.updateItemDataByItemIdx(mainActivity, itemViewModel)
                }
                work1.join()
                mainActivity.removeFragment(FragmentName.MODIFY_ITEM_FRAGMENT)
            }
        }
        materialAlertDialogBuilder.show()
    }
}