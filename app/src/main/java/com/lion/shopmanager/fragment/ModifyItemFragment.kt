package com.lion.shopmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.shopmanager.MainActivity
import com.lion.shopmanager.R
import com.lion.shopmanager.databinding.FragmentModifyItemBinding
import com.lion.shopmanager.util.FragmentName

class ModifyItemFragment : Fragment() {

    lateinit var fragmentModifyItemBinding: FragmentModifyItemBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentModifyItemBinding = FragmentModifyItemBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // Toolbar를 구성하는 메서드
        settingToolbar()


        return fragmentModifyItemBinding.root
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbar() {
        fragmentModifyItemBinding.apply {
            toolbarModifyItem.title = "물건 수정"
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
                        mainActivity.replaceFragment(FragmentName.ITEM_LIST_FRAGMENT, true, true, null
                        )
                    }
                }
                true
            }
        }
    }
}