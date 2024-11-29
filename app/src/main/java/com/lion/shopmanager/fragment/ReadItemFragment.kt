package com.lion.shopmanager.fragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.shopmanager.MainActivity
import com.lion.shopmanager.R
import com.lion.shopmanager.databinding.FragmentReadItemBinding
import com.lion.shopmanager.util.FragmentName

class ReadItemFragment : Fragment() {

    lateinit var fragmentReadItemBinding: FragmentReadItemBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentReadItemBinding = FragmentReadItemBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // Toolbar를 구성하는 메서드
        settingToolbar()

        return fragmentReadItemBinding.root
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbar() {
        fragmentReadItemBinding.apply {
            toolbarReadItem.title = "물건 보기"
            // 네비게이션 아이콘을 설정하고 누를 경우 NavigationView가 나타나도록 한다.
            toolbarReadItem.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarReadItem.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.READ_ITEM_FRAGMENT)
            }

            // 메뉴
            toolbarReadItem.inflateMenu(R.menu.toolbar_read_item_menu)
            toolbarReadItem.setOnMenuItemClickListener {
                when (it.itemId) {
                    // 수정
                    R.id.toolbar_read_menu_item_modify -> {
                        mainActivity.replaceFragment(FragmentName.MODIFY_ITEM_FRAGMENT, true, true, null)
                    }
                    // 삭제
                    R.id.toolbar_read_menu_item_delete -> {

                        val builder = MaterialAlertDialogBuilder(mainActivity)
                        builder.setTitle("물건 정보 삭제")
                        builder.setMessage("""
                            물건 정보를 삭제합니다.
                            삭제된 정보는 복원할 수 없습니다.
                        """.trimIndent())

                        builder.setNegativeButton("취소", null)
                        builder.setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
                            mainActivity.removeFragment(FragmentName.READ_ITEM_FRAGMENT)
                        }
                        builder.show()
                    }
                }
                true
            }
        }
    }
}