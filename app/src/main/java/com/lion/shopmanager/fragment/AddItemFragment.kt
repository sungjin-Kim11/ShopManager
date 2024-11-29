package com.lion.shopmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.shopmanager.MainActivity
import com.lion.shopmanager.R
import com.lion.shopmanager.databinding.FragmentAddItemBinding
import com.lion.shopmanager.databinding.FragmentItemListBinding
import com.lion.shopmanager.util.FragmentName

class AddItemFragment : Fragment() {

    lateinit var fragmentAddItemBinding: FragmentAddItemBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentAddItemBinding = FragmentAddItemBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // Toolbar를 구성하는 메서드
        settingToolbar()

        return fragmentAddItemBinding.root
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbar() {
        fragmentAddItemBinding.apply {
            toolbarAddItem.title = "물건 추가"
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
                        mainActivity.replaceFragment(FragmentName.ITEM_LIST_FRAGMENT, true, true, null)
                    }
                }
                true
            }
        }
    }
}