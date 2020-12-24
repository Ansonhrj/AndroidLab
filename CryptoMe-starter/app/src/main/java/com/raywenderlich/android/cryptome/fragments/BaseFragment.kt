package com.raywenderlich.android.cryptome.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.raywenderlich.android.cryptome.App
import com.raywenderlich.android.cryptome.DetailActivity
import com.raywenderlich.android.cryptome.R
import com.raywenderlich.android.cryptome.adapters.CryptoDataAdapter
import com.raywenderlich.android.cryptome.models.CryptoData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


//TODO 5: Constant Values for Initial Delay and Interval

open class BaseFragment : Fragment(), CryptoDataAdapter.Listener, SwipeRefreshLayout.OnRefreshListener {

  private var cryptoDataAdapter: CryptoDataAdapter? = null

  private val viewModel = App.injectCryptoDataViewModel()
  //TODO 11: Declare Disposables

  private lateinit var cryptocurrencyList: RecyclerView
  private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

  private var currencies: String = ""

  private fun readBundle(bundle: Bundle?) {
    if (bundle != null) {
      currencies = bundle.getString("currencies")
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_home, container, false)

    readBundle(getArguments())

    initRecyclerView(view)

    return view
  }

  override fun onStart() {
    super.onStart()

    loadData()

    Log.d("OnStart", "LoadData")
  }

  override fun onResume() {
    super.onResume()

    loadData()

    Log.d("onResume", "LoadData")
  }

  override fun onRefresh() {
    loadData()

    Log.d("onRefresh", "LoadData")
  }

  override fun onPause() {
    super.onPause()

    //TODO 13: Clear Disposables
  }

  override fun onStop() {
    super.onStop()

    //TODO 14: Clear Disposables
  }

  private fun initRecyclerView(view: View) {
    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
    cryptocurrencyList = view.findViewById(R.id.cryptocurrency_list)
    cryptocurrencyList.layoutManager = layoutManager

    mSwipeRefreshLayout = view.findViewById(R.id.swipe_container)
    mSwipeRefreshLayout.setOnRefreshListener(this)
    mSwipeRefreshLayout.setColorSchemeResources(
        R.color.colorPrimary,
        android.R.color.holo_green_dark,
        android.R.color.holo_orange_dark,
        android.R.color.holo_blue_dark)
  }

  private fun loadData() {
    //TODO 6: Call API using Observable
    //TODO 12: Add Disposables
  }

  //TODO 7: Add Update Crypto Data

  //TODO 8: Add onError

  //TODO 9: Handle API Response & Error

  override fun onItemClick(cryptoData: CryptoData) {
    //TODO 10: Handle Item Click
  }

}