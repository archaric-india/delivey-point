package com.archaric.deliverypoint.Search

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archaric.deliverypoint.EndPoint
import com.archaric.deliverypoint.Fragments.AllCategoriesModel
import com.archaric.deliverypoint.Fragments.FiftyPercentOfferModel
import com.archaric.deliverypoint.Fragments.RestaurantsAroundYouLargeAdapter
import com.archaric.deliverypoint.Mapset
import com.archaric.deliverypoint.R
import com.archaric.deliverypoint.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

var showFilter = false;

class SearchFragment: Fragment() {
    private var back: ImageButton? = null
    var s = ""
    var searchBar: EditText? = null
    var close_search: ImageView? = null
    var backtohome: TextView? = null
    var recycler: RecyclerView ?= null
    var filter: ImageView? = null
    var empty: FrameLayout? = null
    var adapter: RestaurantsAroundYouLargeAdapter ?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_sep, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchBar = view.findViewById(R.id.search_bar)
        close_search = view.findViewById(R.id.close_search)
        filter = view.findViewById(R.id.filter_btn)
        back = view.findViewById(R.id.close)
        empty = view.findViewById(R.id.empty)
        backtohome = view.findViewById(R.id.backToHomePageFromOrderHistory)
        recycler = view.findViewById(R.id.recycler)

        adapter = RestaurantsAroundYouLargeAdapter()
        recycler?.layoutManager = LinearLayoutManager(requireContext())
        recycler?.adapter = adapter
        close_search?.setOnClickListener(View.OnClickListener { searchBar?.setText("") })

        back?.setOnClickListener(View.OnClickListener {
            Utils.hideKeyboard(activity)
            searchBar?.clearFocus()
        })

        searchBar?.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                back?.setVisibility(View.VISIBLE)
            } else {
                back?.setVisibility(View.GONE)
            }
        })

        backtohome?.setOnClickListener {
            findNavController().popBackStack()
        }

        if(showFilter){
            showFilter = false;
            showFilterA()
        } else {
            searchBar?.requestFocus()
            Utils.showKeyboard(requireActivity())
        }

        filter?.setOnClickListener {
            showFilterA()
        }

        searchBar?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s.toString())) {
                    close_search?.setVisibility(View.VISIBLE)

                    search()
                }
                if (TextUtils.isEmpty(s.toString())) {
                    close_search?.setVisibility(View.GONE)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    fun search() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://delivery-8a843.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val endPoint = retrofit.create(EndPoint::class.java)

        val zoneId = Utils.getStoredData(activity, Mapset.ZONE_ID)
        println(zoneId + " Zone Id at Special Offers")
        if (!TextUtils.isEmpty(zoneId)) {
            endPoint.searchHome(searchBar?.text.toString().toLowerCase(),zoneId)
                .enqueue(object : Callback<List<FiftyPercentOfferModel?>?> {
                    override fun onResponse(
                        call: Call<List<FiftyPercentOfferModel?>?>,
                        response: Response<List<FiftyPercentOfferModel?>?>
                    ) {
                        val results = response.body() as ArrayList<FiftyPercentOfferModel?>?
                        if (results != null) {
                            adapter?.setModels(results)
                            if(results.size == 0){
                                empty?.visibility = View.VISIBLE
                            } else{
                                empty?.visibility = View.GONE
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<FiftyPercentOfferModel?>?>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }
    }

    override fun onPause() {
        super.onPause()
        Utils.hideKeyboard(requireActivity())
    }
    fun showFilterA(){
        Utils.hideKeyboard(requireActivity())
        FilterBottomSheet().show(requireActivity().supportFragmentManager, null)
    }


}