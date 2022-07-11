package app.scanner.calc.features.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.scanner.calc.databinding.ItemExpressionBinding
import app.scanner.domain.model.MathData

class CalculatedAdapter : ListAdapter<MathData, CalculatedAdapter.RecognizerViewHolder>(diffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecognizerViewHolder {
        val binding = ItemExpressionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecognizerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecognizerViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let { holder.bind(it) }
    }

    inner class RecognizerViewHolder(
        private val binding: ItemExpressionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: MathData) {
            binding.apply {
                textViewInput.text = "Input: ${data.expression}"
                textViewResult.text = "Result: ${data.result}"
            }
        }
    }
    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<MathData>() {
            override fun areItemsTheSame(oldItem: MathData, newItem: MathData): Boolean =
                oldItem.id == newItem.id
            override fun areContentsTheSame(
                oldItem: MathData,
                newItem: MathData
            ): Boolean =
                oldItem == newItem
        }
    }
}