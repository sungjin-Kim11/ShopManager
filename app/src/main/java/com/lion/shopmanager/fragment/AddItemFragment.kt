package com.lion.shopmanager.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.shopmanager.MainActivity
import com.lion.shopmanager.R
import com.lion.shopmanager.databinding.FragmentAddItemBinding
import com.lion.shopmanager.model.ItemModel
import com.lion.shopmanager.repository.ItemRepository
import com.lion.shopmanager.util.FragmentName
import com.lion.shopmanager.util.ItemSellingOrSold
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddItemFragment : Fragment() {

    lateinit var fragmentAddItemBinding: FragmentAddItemBinding
    lateinit var mainActivity: MainActivity

    private var selectedImagePath: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentAddItemBinding = FragmentAddItemBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // Toolbar를 구성하는 메서드 호출
        settingToolbar()
        // 입력 요소 초기 설정 메서드 호출
        settingTextField()
        // 이미지를 불러오는 메서드 호출
        callImage()

        return fragmentAddItemBinding.root
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbar() {
        fragmentAddItemBinding.apply {
            toolbarAddItem.title = "제품 추가"
            // 네비게이션 아이콘을 설정하고 누를 경우 NavigationView가 나타나도록 한다.
            toolbarAddItem.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarAddItem.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.ADD_ITEM_FRAGMENT)
            }

            // 메뉴
            toolbarAddItem.inflateMenu(R.menu.toolbar_add_item_menu)
            toolbarAddItem.setOnMenuItemClickListener {
                when (it.itemId) {
                    // 완료
                    R.id.toolbar_add_item_check -> {
                        processingAddItemInfo()
                    }
                }
                true
            }
        }
    }

    // 입력 요소 초기 설정 메서드
    fun settingTextField(){
        fragmentAddItemBinding.apply {
            mainActivity.showSoftInput(textInputLayoutName.editText!!)
        }
    }

    // 이미지를 불러오는 메서드
    fun callImage() {
        fragmentAddItemBinding.buttonInputSearchphoto.setOnClickListener {
            mainActivity.setTargetImageView(fragmentAddItemBinding.imageAddView)
            mainActivity.launchAlbumIntent()
        }
    }

    fun updateSelectedImagePath(path: String) {
        selectedImagePath = path
    }

    // 물건 정보 등록 완료 처리 메서드
    fun processingAddItemInfo(){
        fragmentAddItemBinding.apply {
            // 사용자가 입력한 값을 가져온다
            val itemName = textInputLayoutName.editText?.text?.toString()!!
            val itemPrice = textInputLayoutPrice.editText?.text?.toString()!!
            val itemabout = textInputLayoutAbout.editText?.text?.toString()!!

            // 제품명
            if(itemName.isEmpty()){
                mainActivity.showConfirmDialog("제품명 입력 오류", "제품의 이름을 입력해주세요"){
                    textInputLayoutName.editText?.requestFocus()
                }
                return
            }
            // 제품 가격
            if(itemPrice.isEmpty()){
                mainActivity.showConfirmDialog("제품 가격 입력 오류", "제품 가격을 입력해주세요"){
                    textInputLayoutPrice.editText?.requestFocus()
                }
                return
            }
            // 제품 정보
            if(itemabout.isEmpty()){
                mainActivity.showConfirmDialog("제품 정보 입력 오류", "제품 정보를 입력해주세요"){
                    textInputLayoutAbout.editText?.requestFocus()
                }
                return
            }

            // 판매 유무를 정수값으로 변환한다.
            val itemPriceInt = itemPrice.toInt()
            // 판매 유무
            val itemSellinOrSold = when(toggleInputSellingOrSold.checkedButtonId){
                R.id.buttonInputSelling -> ItemSellingOrSold.ITEM_SELLING
                else -> ItemSellingOrSold.ITEM_SOLD
            }

            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val itemDate = currentDateTime.format(formatter)

            // ViewModel 객체에 담는다.
            val itemModel = ItemModel(
                0, itemName, itemPriceInt, itemabout, itemSellinOrSold, itemImage = selectedImagePath, itemDate)

            // 데이터를 저장하는 메서드를 호출한다.
            CoroutineScope(Dispatchers.Main).launch{
                val work1 = async(Dispatchers.IO){
                    ItemRepository.insertItemData(mainActivity, itemModel)
                }
                work1.join()
                // 이전 화면으로 돌아간다.
                mainActivity.removeFragment(FragmentName.ADD_ITEM_FRAGMENT)
            }
        }
    }
}
