package com.lion.shopmanager.fragment

import android.app.Activity
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ModifyItemFragment : Fragment() {

    lateinit var fragmentModifyItemBinding: FragmentModifyItemBinding
    lateinit var mainActivity: MainActivity

    // 이미지 경로 저장 변수
    private var selectedImagePath: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentModifyItemBinding = FragmentModifyItemBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // Toolbar 구성
        settingToolbar()
        // 기존 데이터 로드
        settingInput()
        // 이미지 선택 버튼 이벤트 설정
        setupImageSelection()

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

    // 기존 데이터 로드 및 설정
    fun settingInput() {
        fragmentModifyItemBinding.apply {
            val itemIdx = arguments?.getInt("itemIdx")!!
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    ItemRepository.selectItemDataByItemIdx(mainActivity, itemIdx)
                }
                val itemViewModel = work1.await()

                // 기존 데이터 설정
                textModifyLayoutName.editText?.setText(itemViewModel.itemName)
                textModifyLayoutPrice.editText?.setText(itemViewModel.itemPrice.toString())
                textModifyLayoutAbout.editText?.setText(itemViewModel.itemAbout)

                when (itemViewModel.itemSellinOrSold) {
                    ItemSellingOrSold.ITEM_SELLING -> toggleModifySellingOrSold.check(R.id.buttonModifySelling)
                    ItemSellingOrSold.ITEM_SOLD -> toggleModifySellingOrSold.check(R.id.buttonModifySold)
                }

                // 기존 이미지 로드
                if (!itemViewModel.itemImage.isNullOrEmpty()) {
                    val bitmap = BitmapFactory.decodeFile(itemViewModel.itemImage)
                    fragmentModifyItemBinding.imageModifyView.setImageBitmap(bitmap)
                    selectedImagePath = itemViewModel.itemImage
                }
            }
        }
    }

    // 이미지 선택 버튼 이벤트 설정
    fun setupImageSelection() {
        fragmentModifyItemBinding.buttonModifySearchphoto.setOnClickListener {
            // MainActivity에 현재 ImageView 설정
            mainActivity.setTargetImageView(fragmentModifyItemBinding.imageModifyView)
            // 앨범 선택 런처 호출
            mainActivity.launchAlbumIntent()
        }
    }

    // 데이터 수정 처리
    fun modifyDone() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(mainActivity)
        materialAlertDialogBuilder.setTitle("수정")
        materialAlertDialogBuilder.setMessage("이전 데이터로 복원할 수 없습니다")
        materialAlertDialogBuilder.setNeutralButton("취소", null)
        materialAlertDialogBuilder.setPositiveButton("수정") { dialogInterface: DialogInterface, i: Int ->
            val itemIdx = arguments?.getInt("itemIdx")!!
            val itemName = fragmentModifyItemBinding.textModifyLayoutName.editText?.text.toString()
            val itemPrice = fragmentModifyItemBinding.textModifyLayoutPrice.editText?.text.toString().toInt()
            val itemAbout = fragmentModifyItemBinding.textModifyLayoutAbout.editText?.text.toString()
            val itemSellinOrSold = when (fragmentModifyItemBinding.toggleModifySellingOrSold.checkedButtonId) {
                R.id.buttonModifySelling -> ItemSellingOrSold.ITEM_SELLING
                else -> ItemSellingOrSold.ITEM_SOLD
            }
            val itemImage = selectedImagePath

            val currentDateTime = LocalDateTime.now() // 현재 날짜와 시간
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // 원하는 형식
            val itemDate = currentDateTime.format(formatter)

            // 수정된 모델 생성
            val itemViewModel = ItemModel(itemIdx, itemName, itemPrice, itemAbout, itemSellinOrSold, itemImage, itemDate)

            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    ItemRepository.updateItemDataByItemIdx(mainActivity, itemViewModel)
                }
                work1.join()
                mainActivity.removeFragment(FragmentName.MODIFY_ITEM_FRAGMENT)
            }
        }
        materialAlertDialogBuilder.show()
    }

}
