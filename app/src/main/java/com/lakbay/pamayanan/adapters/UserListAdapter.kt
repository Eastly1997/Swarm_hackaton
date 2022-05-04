package com.lakbay.pamayanan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.internal.service.Common
import com.lakbay.pamayanan.R
import com.lakbay.pamayanan.databinding.ItemFriendBinding
import com.lakbay.pamayanan.databinding.ItemJobBinding
import com.lakbay.pamayanan.databinding.ItemJobExploreBinding
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.viewModels.User

class UserListAdapter(private val userList: ArrayList<User>, private val context: Context): RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFriendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(userList[position], position)


    override fun getItemCount() = userList.size

    inner class ViewHolder(private val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, position: Int) {
            binding.user = user
            binding.convertedAmount = CommonUtils.convertToAmount(user.donatedAmount)
            CommonUtils.loadCurvedImage(context,
                AppCompatResources.getDrawable(context, R.drawable.temp_profile)!!,
                binding.userImage,
                99
            )
        }
    }
}