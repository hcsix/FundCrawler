package com.supcoder.fundcrawler

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.supcoder.fundcrawler.adapter.BottomSheetAdapter
import com.supcoder.fundcrawler.adapter.HistoryAdapter
import com.supcoder.fundcrawler.adapter.ImageAdapter
import com.supcoder.fundcrawler.entity.HistoryEntity
import com.supcoder.fundcrawler.http.HtmlParserUtil
import com.supcoder.fundcrawler.http.ProcessMessege
import com.supcoder.fundcrawler.utils.ListDataSave
import com.supcoder.fundcrawler.widget.ItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import me.foji.realmhelper.RealmHelper
import kotlin.properties.Delegates


open class MainActivity : AppCompatActivity() {

    private var mData: MutableList<String>? = null
    private lateinit var mAdapter: ImageAdapter

    private var mHisData: MutableList<HistoryEntity>? = null
    private lateinit var mHisAdapter: HistoryAdapter

    private var mPopupWindow: PopupWindow? = null

    private var mLastClickBack: Long = 0

    private var mRealmHelper: RealmHelper by Delegates.notNull()

    private var hisRecyclerView: RecyclerView? = null


    private var dataSave: ListDataSave? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initData()
        initRecyclerView()
        initPopupWindow()
        initSwipeRefreshLayout()
        bindEvent()
    }


    /**
     * 初始化数据
     */
    private fun initData() {
        mRealmHelper = RealmHelper.get(this)
        dataSave = ListDataSave(applicationContext, "fund")
        mData = dataSave!!.getDataList<String>("fundList") as MutableList<String>
    }


    /**
     * 初始化RecyclerView
     */
    private fun initRecyclerView() {

        mAdapter = ImageAdapter(mData, ImageAdapter.OnFundIdListener { fundId ->
            Log.e("12", fundId)
            Thread(Runnable {
                val mList = HtmlParserUtil.getInstance().queryProcessInfo2(fundId)

                for (item in mList) {
                    Log.e("12", item.toString())
                }

                runOnUiThread {
                    if (!isFinishing) {
                        openBottom(mList)
                    }
                }
            }).start()
        })
        mAdapter.openLoadAnimation()

        val itemDragAndSwipeCallback = ItemDragAndSwipeCallback(mAdapter)
        val itemTouchHelper = ItemTouchHelper(itemDragAndSwipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        mAdapter.enableDragItem(itemTouchHelper, R.id.fundImg, true)
        mAdapter.enableSwipeItem()

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(ItemDecoration())


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                hideKeyboard(editText)
            }
        })


    }


    private fun initPopupWindow() {
        mPopupWindow = PopupWindow(this)
        mPopupWindow!!.width = ViewGroup.LayoutParams.MATCH_PARENT
        mPopupWindow!!.height = ViewGroup.LayoutParams.WRAP_CONTENT
        mPopupWindow!!.contentView = LayoutInflater.from(this).inflate(R.layout.pop_history, null)
        mPopupWindow!!.setBackgroundDrawable(ColorDrawable(0x00000000))
        mPopupWindow!!.isOutsideTouchable = false
        mPopupWindow!!.isFocusable = true
        hisRecyclerView = mPopupWindow!!.contentView.findViewById(R.id.hisRecyclerView)


        val listener = object : HistoryAdapter.OnHistoryListener {
            override fun onDelete(fund: HistoryEntity?) {
                mRealmHelper.use {
                    val hisRecord = where(HistoryEntity::class.java)
                            .equalTo(
                                    "fundId",
                                    fund!!.fundId
                            ).findAll()
                    executeTransaction {
                        hisRecord!!.deleteAllFromRealm()
                        refreshHis()
                    }
                }
            }

            override fun onFund(fundId: String?) {
                editText.setText(fundId)
                editText.setSelection(fundId!!.length)
                hideKeyboard(editText)
                mPopupWindow!!.dismiss()
            }
        }

        mHisAdapter = HistoryAdapter(mHisData, listener)

        hisRecyclerView!!.adapter = mHisAdapter
        hisRecyclerView!!.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.addItemDecoration(ItemDecoration())
        refreshHis()

        mPopupWindow!!.setOnDismissListener { hideKeyboard(editText) }
    }


    /**
     *  初始化SwipeRefreshLayout
     */
    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.red)
        swipeRefreshLayout.isEnabled = false
    }

    private fun refreshHis() {
        mRealmHelper.use {
            val hisRecords = where(HistoryEntity::class.java).findAll()
            Log.e("KOTLIN", hisRecords.size.toString())
            if (null != hisRecords) {
                mHisAdapter.refresh(hisRecords)
                mHisAdapter.notifyDataSetChanged()

            }
        }
    }


    private fun bindEvent() {

        addImg.setOnClickListener {
            val editTextStr = editText.text.toString().trim()
            if ("" != editTextStr) {
                if (mAdapter.data.contains(editText.text.toString().trim())) {
                    showSnackBar(editTextStr + " 号基金已存在")
                } else {
                    mAdapter.addData(0, editTextStr)
                    recyclerView.scrollToPosition(0)
                    mRealmHelper.use {
                        executeTransactionAsync({ realm ->
                            val hisEntity = HistoryEntity()
                            hisEntity.fundId = editTextStr
                            realm.copyToRealmOrUpdate(hisEntity)
                        }, {
                            refresh()
                        }, { error ->
                            showSnackBar("Insert fail : $error")
                        })
                    }
                }
                editText.text.clear()
            } else {
                showSnackBar("基金号不能为空")
            }
        }


        historyImg.setOnClickListener {
            if (mPopupWindow != null) {
                hideKeyboard(editText)
                mPopupWindow!!.showAsDropDown(toolbar)
            }
        }

        cleanImg.setOnClickListener {
            swipeRefreshLayout.isRefreshing = true
            Thread(Runnable {
                Glide.get(application).clearDiskCache()
                runOnUiThread {
                    Glide.get(application).clearMemory()
                    showSnackBar("缓存清理成功")
                    swipeRefreshLayout.isRefreshing = false
                }
            }).start()
        }


    }


    override fun onBackPressed() {
        val current = System.currentTimeMillis()
        if (current - mLastClickBack > 3 * 1000) {
            mLastClickBack = System.currentTimeMillis()
            showSnackBar("再按一次返回键退出程序")
            if (dataSave != null) {
                dataSave!!.setDataList("fundList", mAdapter.data)
            }
            return
        }
        finish()
        super.onBackPressed()
    }


    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }


    private fun showSnackBar(hint: String) {
        val snackBar = Snackbar.make(recyclerView, hint, 3000)
        snackBar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.iron))
        snackBar.show()
    }


    private fun openBottom(list: List<ProcessMessege>) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_bottom, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.bottomRecycler)
        recyclerView.itemAnimator = DefaultItemAnimator()
//        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = BottomSheetAdapter(list)
        recyclerView.adapter = adapter
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        dialog.show()
    }
}
