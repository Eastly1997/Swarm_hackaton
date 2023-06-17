package com.lakbay.pamayanan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lakbay.pamayanan.R
import com.lakbay.pamayanan.databinding.ItemAddOnsListBinding
import com.lakbay.pamayanan.databinding.ItemVariationListBinding
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.viewModels.Variation

class VariationAdapter(private val context: Context, private val variationList: ArrayList<Variation>, val btnListener: BtnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_VARIATION = 0
        private const val TYPE_ADD_ONS = 1
        var mClickListener: BtnClickListener? = null
    }



    interface BtnClickListener {
        fun updateTotal(price: Double)
    }

    inner class ItemVariationHolder(private val binding: ItemVariationListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(variation: Variation) {
            mClickListener = btnListener
            var lastPrice = 0.0
            binding.title = variation.name
            for(i in 1..variation.value.size) {
                val radioButton = LayoutInflater.from(context).inflate(R.layout.item_variation, null)
                radioButton.findViewById<TextView>(R.id.name).text = variation.key[i - 1]
                radioButton.findViewById<TextView>(R.id.value).text = "+ " + CommonUtils.convertToAmount(variation.value[i - 1])
                radioButton.setOnClickListener {
                    for(x in 0 until binding.variation.childCount) {
                        binding.variation.getChildAt(x).findViewById<RadioButton>(R.id.radioButton).isChecked = false
                    }
                    radioButton.findViewById<RadioButton>(R.id.radioButton).isChecked = true
                    if(variation.value[i - 1] == 0.0)
                        mClickListener?.updateTotal(-lastPrice)
                    else
                        mClickListener?.updateTotal(variation.value[i - 1] - lastPrice)

                    lastPrice = variation.value[i - 1]

                }
                binding.variation.addView(radioButton)
            }
        }
    }

    inner class ItemAddOnsHolder(private val binding: ItemAddOnsListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(variation: Variation) {
            mClickListener = btnListener
            binding.title = variation.name
            binding.subTitle.text = "Optional, max ${variation.limit}"
            val limit = variation.limit
            var selected = 0
            for(i in 1..variation.value.size) {
                val checkBox = LayoutInflater.from(context).inflate(R.layout.item_add_ons, null)
                checkBox.findViewById<TextView>(R.id.name).text = variation.key[i - 1]
                checkBox.findViewById<TextView>(R.id.value).text = CommonUtils.convertToAmount(variation.value[i - 1])
                checkBox.setOnClickListener {

                    if(checkBox.findViewById<CheckBox>(R.id.checkBox).isChecked) {
                        selected--
                        checkBox.findViewById<CheckBox>(R.id.checkBox).isChecked = false
                        mClickListener?.updateTotal(-variation.value[i - 1])

                    } else {

                        if(limit == selected)
                            return@setOnClickListener

                        selected++
                        checkBox.findViewById<CheckBox>(R.id.checkBox).isChecked = true
                        mClickListener?.updateTotal(variation.value[i - 1])

                    }

                }
                binding.addons.addView(checkBox)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_VARIATION -> {
                val binding = ItemVariationListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false)
                ItemVariationHolder(binding)
            }
            else -> {
                val binding = ItemAddOnsListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false)
                ItemAddOnsHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VariationAdapter.ItemVariationHolder -> {
                holder.bind(variationList[position])
            }
            is VariationAdapter.ItemAddOnsHolder -> {
                holder.bind(variationList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return variationList.size
    }

    override fun getItemViewType(position: Int): Int {
        // Determine the view type based on the position
        return if (variationList[position].limit == 1) {
            TYPE_VARIATION
        } else {
            TYPE_ADD_ONS
        }
    }

}