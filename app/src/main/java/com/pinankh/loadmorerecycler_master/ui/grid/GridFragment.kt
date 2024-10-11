package com.pinankh.loadmorerecycler_master.ui.grid

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pinankh.loadmorerecycler_master.Constant.VIEW_TYPE_ITEM
import com.pinankh.loadmorerecycler_master.Constant.VIEW_TYPE_LOADING
import com.pinankh.loadmorerecycler_master.OnLoadMoreListener
import com.pinankh.loadmorerecycler_master.RecyclerViewLoadMoreScroll
import com.pinankh.loadmorerecycler_master.databinding.FragmentDashboardBinding
import java.util.Random

class GridFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    lateinit var itemsCells: ArrayList<Int?>
    lateinit var loadMoreItemsCells: ArrayList<Int?>
    lateinit var adapterGrid: Items_GridRVAdapter
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(GridViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //** Set the data for our ArrayList
        setItemsData()

        //** Set the adapterLinear of the RecyclerView
        setAdapter()

        //** Set the Layout Manager of the RecyclerView
        setRVLayoutManager()

        //** Set the scrollListener of the RecyclerView
        setRVScrollListener()
//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setItemsData() {
        itemsCells = ArrayList()
        for (i in 0..41) {
            val rnd = Random()
            val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            itemsCells.add(color)
        }
    }

    private fun setAdapter() {
        adapterGrid = Items_GridRVAdapter(itemsCells)
        adapterGrid.notifyDataSetChanged()
        binding.itemsGridRv.adapter = adapterGrid
    }

    private fun setRVLayoutManager() {
        mLayoutManager = GridLayoutManager(requireActivity(), 3)
        binding.itemsGridRv.layoutManager = mLayoutManager
        binding.itemsGridRv.setHasFixedSize(true)
        binding.itemsGridRv.adapter = adapterGrid
        (mLayoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapterGrid.getItemViewType(position)) {
                    VIEW_TYPE_ITEM -> 1
                    VIEW_TYPE_LOADING -> 3 //number of columns of the grid
                    else -> -1
                }
            }
        }
    }

    private fun setRVScrollListener() {
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as GridLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                loadMoreData()
            }
        })

        binding.itemsGridRv.addOnScrollListener(scrollListener)
    }

    private fun loadMoreData() {
        //Add the Loading View
        adapterGrid.addLoadingView()
        //Create the loadMoreItemsCells Arraylist
        loadMoreItemsCells = ArrayList()
        //Get the number of the current Items of the main Arraylist
        val start = adapterGrid.itemCount
        //Load 16 more items
        val end = start + 16
        //Use Handler if the items are loading too fast.
        //If you remove it, the data will load so fast that you can't even see the LoadingView
        Handler().postDelayed({
            for (i in start..end) {
                val rnd = Random()
                val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                //Get data and add them to loadMoreItemsCells ArrayList
                loadMoreItemsCells.add(color)
            }
            //Remove the Loading View
            adapterGrid.removeLoadingView()
            //We adding the data to our main ArrayList
            adapterGrid.addData(loadMoreItemsCells)
            //Change the boolean isLoading to false
            scrollListener.setLoaded()
            //Update the recyclerView in the main thread
            binding.itemsGridRv.post {
                adapterGrid.notifyDataSetChanged()
            }
        }, 3000)
    }
}