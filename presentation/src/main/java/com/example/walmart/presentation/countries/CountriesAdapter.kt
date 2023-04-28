package com.example.walmart.presentation.countries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.walmart.domain.model.Country
import com.example.walmart.presentation.R
import com.example.walmart.presentation.databinding.CountryItemBinding

class CountriesAdapter(
    private val onItemClickListener: (Country) -> Unit
) : ListAdapter<Country, CountryViewHolder>(diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = CountryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.root.setOnClickListener {
            onItemClickListener(getItem(holder.bindingAdapterPosition))
        }
    }
}

class CountryViewHolder(val binding: CountryItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Country) = with(item) {
        with(binding) {
            nameWithRegionView.text =
                root.context.getString(R.string.format_name_with_region, name, region)
            codeView.text = code
            capitalView.text = capital
        }
    }
}

private val diff = object : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem == newItem
    }
}