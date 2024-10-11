package com.pinankh.loadmorerecycler_master.ui.grid

/**
 * @Author by Pinankh Patel
 * Created on Date = 11-10-2024  00:45
 * Github = https://github.com/Pinankh
 * LinkdIN = https://www.linkedin.com/in/pinankh-patel-19400350/
 * Stack Overflow = https://stackoverflow.com/users/4564376/pinankh
 * Medium = https://medium.com/@pinankhpatel
 * Email = pinankhpatel@gmail.com
 */
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pinankh.loadmorerecycler_master.Constant
import com.pinankh.loadmorerecycler_master.R
import com.pinankh.loadmorerecycler_master.databinding.GridItemRowBinding
import com.pinankh.loadmorerecycler_master.databinding.ProgressLoadingBinding

class Items_GridRVAdapter(private var itemsCells: ArrayList<Int?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addData(dataViews: ArrayList<Int?>) {
        this.itemsCells.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun getItemAtPosition(position: Int): Int? {
        return itemsCells[position]
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            itemsCells.add(null)
            notifyItemInserted(itemsCells.size - 1)
        }
    }

    fun removeLoadingView() {
        //remove loading item
        if (itemsCells.size != 0) {
            itemsCells.removeAt(itemsCells.size - 1)
            notifyItemRemoved(itemsCells.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.grid_item_row, parent, false)
            ItemViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.progress_loading, parent, false)
            val binding = ProgressLoadingBinding.bind(view)

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                binding.progressbar.indeterminateDrawable.colorFilter =
//                    BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
//            } else {
//                binding.progressbar.indeterminateDrawable.setColorFilter(
//                    Color.WHITE,
//                    PorterDuff.Mode.MULTIPLY
//                )
//            }
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return itemsCells.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemsCells[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM) {
            val binding = GridItemRowBinding.bind(holder.itemView)

            binding.gridItemImageview.setBackgroundColor(itemsCells[position]!!)
        }
    }
}