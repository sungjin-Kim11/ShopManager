package com.lion.shopmanager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialSharedAxis
import com.lion.shopmanager.databinding.ActivityMainBinding
import com.lion.shopmanager.databinding.NavigationHeaderLayoutBinding
import com.lion.shopmanager.fragment.AddItemFragment
import com.lion.shopmanager.fragment.ItemListFragment
import com.lion.shopmanager.util.FragmentName

class MainActivity : AppCompatActivity() {

    val activityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(activityMainBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 네비게이션 뷰를 구성하는 메서드를 호출한다.
        settingNavigationView()

        // 첫 화면을 설정해준다.
        replaceFragment(FragmentName.ITEM_LIST_FRAGMENT, false, false, null)

    }

    // 네비게이션 뷰를 구성하는 메서드
    fun settingNavigationView(){
        activityMainBinding.apply {
            // 네비게이션 뷰의 해더
            val navigationHeaderLayoutBinding = NavigationHeaderLayoutBinding.inflate(layoutInflater)
            navigationHeaderLayoutBinding.textViewNavigationHeaderTitle1.text = "ShopManager"
            navigationHeaderLayoutBinding.textViewNavigationHeaderTitle2.text = "판매자용 어플입니다."
            navigationViewMain.addHeaderView(navigationHeaderLayoutBinding.root)

            // 메뉴
            navigationViewMain.inflateMenu(R.menu.navigation_main_menu)

            // 네비게이션 뷰의 메뉴 중 전체 메모가 선택되어 있는 상태로 설정한다.
            navigationViewMain.setCheckedItem(R.id.navigation_menu_item_all)

            // 새로운 메뉴 추가
//            val menuItem = navigationViewMain.menu.findItem(R.id.navigation_menu_item_memo_category)
//            menuItem.subMenu?.add(Menu.NONE, 100, Menu.NONE, "새로추가한 메뉴")
//            val subMenuItem = menuItem.subMenu?.findItem(100)
//            subMenuItem?.setIcon(R.drawable.lock_24px)

            // 네비게이션의 메뉴를 눌렀을 때
            navigationViewMain.setNavigationItemSelectedListener {
                when(it.itemId){
//                    R.id.navigation_menu_item_all -> {
//                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, null)
//                    }
//                    R.id.navigation_menu_item_favorite -> {
//                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, null)
//                    }
//                    R.id.navigation_menu_item_secret -> {
//                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, null)
//                    }
//                    R.id.navigation_menu_item_management_category -> {
//                        replaceFragment(FragmentName.CATEGORY_MANAGEMENT_FRAGMENT, false, false, null)
//                    }
//                    R.id.navigation_menu_item_category_basic -> {
//                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, null)
//                    }
//                    else -> {
//                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, null)
//                    }
                }

                // 닫아준다.
                drawerLayoutMain.close()
                true
            }
        }
    }

    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: FragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
        // 프래그먼트 객체
        val newFragment = when(fragmentName){
            // 전체 메모 화면
            FragmentName.ITEM_LIST_FRAGMENT -> ItemListFragment()
            FragmentName.ADD_ITEM_FRAGMENT -> AddItemFragment()
            FragmentName.READ_ITEM_FRAGMENT -> AddItemFragment()
        }

        // bundle 객체가 null이 아니라면
        if(dataBundle != null){
            newFragment.arguments = dataBundle
        }

        // 프래그먼트 교체
        supportFragmentManager.commit {

            if(animate) {
                newFragment.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                newFragment.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
            }

            replace(R.id.fragmentContainerView, newFragment)
            if(isAddToBackStack){
                addToBackStack(fragmentName.str)
            }
        }
    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: FragmentName){
        supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}