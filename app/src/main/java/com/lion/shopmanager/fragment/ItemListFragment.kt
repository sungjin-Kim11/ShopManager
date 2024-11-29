package com.lion.shopmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lion.shopmanager.MainActivity
import com.lion.shopmanager.R
import com.lion.shopmanager.databinding.FragmentItemListBinding
import com.lion.shopmanager.databinding.RowText1Binding
import com.lion.shopmanager.util.FragmentName

class ItemListFragment : Fragment() {

    lateinit var fragmentItemListFragment: FragmentItemListBinding
    lateinit var mainActivity: MainActivity

    // RecyclerView 구성을 위한 임시데이터
    val tempData1 = Array(100){
        "물건 ${it+1}"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentItemListFragment = FragmentItemListBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // Toolbar를 구성하는 메서드를 호출한다.
        settingToolbar()

        // RecyclerView를 구성하는 메서드를 호출한다.
        settingRecyclerView()

        // 버튼을 설정하는 메서드를 호출한다.
        settingButton()

        return fragmentItemListFragment.root
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbar(){
        fragmentItemListFragment.apply {
            toolbarItemList.title = "전체 물건"
            // 네비게이션 아이콘을 설정하고 누를 경우 NavigationView가 나타나도록 한다.
            toolbarItemList.setNavigationIcon(R.drawable.menu_24px)
            toolbarItemList.setNavigationOnClickListener {
                mainActivity.activityMainBinding.drawerLayoutMain.open()
            }
        }
    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView(){
        fragmentItemListFragment.apply {
            recyclerViewItemList.adapter = RecyclerShowMemoAdapter()
            recyclerViewItemList.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewItemList.addItemDecoration(deco)
        }
    }

    // 버튼을 설정하는 메서드
    fun settingButton(){
        fragmentItemListFragment.apply {
            // fab를 누를 때
            fabItemListAdd.setOnClickListener {
                // AddMemoFragment가 나타나게 한다.
                mainActivity.replaceFragment(FragmentName.ADD_ITEM_FRAGMENT, true, true, null)
            }
        }
    }

    // RecyclerView의 어뎁터
    inner class RecyclerShowMemoAdapter : RecyclerView.Adapter<RecyclerShowMemoAdapter.ViewHolderMemoAdapter>(){
        // ViewHolder
        inner class ViewHolderMemoAdapter(val rowMemoBinding: RowText1Binding) : RecyclerView.ViewHolder(rowMemoBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMemoAdapter {
            val rowMemoBinding = RowText1Binding.inflate(layoutInflater, parent, false)
            val viewHolderMemoAdapter = ViewHolderMemoAdapter(rowMemoBinding)

            rowMemoBinding.root.setOnClickListener {
                mainActivity.replaceFragment(FragmentName.READ_ITEM_FRAGMENT, true, true, null)
            }

            return viewHolderMemoAdapter
        }

        override fun getItemCount(): Int {
            return tempData1.size
        }

        override fun onBindViewHolder(holder: ViewHolderMemoAdapter, position: Int) {
            holder.rowMemoBinding.textViewRow.text = tempData1[position]
        }
    }

}