package com.flowersofk.imagesearchtool

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.flowersofk.imagesearchtool.adapter.ImageSearchAdapter
import com.flowersofk.imagesearchtool.data.ImageDoc
import com.flowersofk.imagesearchtool.data.ImageSearchRequestData
import com.flowersofk.imagesearchtool.model.ImageSearchViewModel
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 메인화면
 */
class MainActivity : AppCompatActivity() {

    companion object {
        const val PAGE_SIZE = 20                // 한 페이지 당 문서 카운트
    }

    private val SORT_ACCURACY = "accuracy"      // 정확도순
    private val SORT_RECENCY = "recency"        // 최신순
    private var sort = SORT_ACCURACY            // 정렬기준(SORT_ACCURACY : 정확도순, SORT_RECENCY : 최신순)

    private lateinit var filter:String          // 필터값(Collection)

    private var adapter: ImageSearchAdapter = ImageSearchAdapter(this)
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    private lateinit var viewModel: ImageSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAdapter()
        initUI()

        requestImage()

    }

    private fun initAdapter() {

        filter = "All"
        spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)

        viewModel = ViewModelProvider(this).get(ImageSearchViewModel::class.java)
        viewModel.repos.observe(this, Observer<PagedList<ImageDoc>> {
            adapter.submitList(it)
        })

        viewModel.networkErrors.observe(this, Observer<String> {
            makeText(this, "${it}", Toast.LENGTH_LONG).show()
        })

        viewModel.collectionResponse.observe(this, Observer<List<String>> {
            spinnerAdapter.clear()
            spinnerAdapter.addAll(it)
            spinnerAdapter.notifyDataSetChanged()
        })

    }

    private fun initUI() {

        // 에디트텍스트 Search Action 설정
        et_keyword.setOnEditorActionListener { v, actionId, event ->
            when(actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    requestImage()
                    true
                }
                else -> false
            }

        }

        rv_imgList.layoutManager = LinearLayoutManager(this)
        rv_imgList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        rv_imgList.adapter = adapter
        rv_imgList.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // 스크롤 상태에 따라 '최상단 버튼' Show/Hide 처리
                when(newState) {
                    SCROLL_STATE_IDLE -> fab_top.show()
                    else -> {
                        fab_top.hide()
                        HideKeyboard(recyclerView)
                    }
                }
            }
        })

        // 정렬기준 선택
        rg_filter.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.rb_accuracy -> sort = SORT_ACCURACY  // 정확도순
                R.id.rb_recency -> sort = SORT_RECENCY    // 최신순
            }

            requestImage()

        }

        // 필터링 선택 Spinner
        spinner_cat_filter.adapter = spinnerAdapter
        spinner_cat_filter.setSelection(0, false)
        spinner_cat_filter.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val selectedItem = parent?.getItemAtPosition(position).toString()

                // 중복호출 방지
                if(filter.equals(selectedItem)) {
                    return
                }

                when(position) {
                    0 -> filter = "All"                                   // 전체검색
                    else -> filter = selectedItem                      // collection 필터값

                }

                requestImage()
            }
        }

        // 최상단 이동 FAB
        fab_top.setOnClickListener {
            rv_imgList.scrollToPosition(0)
        }

    }

    /**
     * 이미지 검색 요청
     */
    private fun requestImage() {

        Log.i("test", "Call requestImage")

        // it:입력값, sort:정렬방법, filter:필터링값
        et_keyword.text.toString().let {
            viewModel.searchKeywordRepo(ImageSearchRequestData(it, sort, filter))
        }

    }

    /**
     * 키보드 숨김처리 (스크롤 시)
     */
    private fun HideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
