package com.example.reto1.ui.users

import android.view.ViewGroup
import android.widget.ListAdapter
import com.example.reto1.data.User

//class UserAdapter(
//    private val onClickListener: (User) -> Unit
//): ListAdapter<User, UserAdapter.EmployeeViewHolder>(EmployeeDiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
//        val binding = ItemEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return EmployeeViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
//        val employee = getItem(position)
//        holder.bind(employee)
//        holder.itemView.setOnClickListener {
//            onClickListener(employee)
//        }
//    }
//
//    inner class EmployeeViewHolder(private val binding: ItemEmployeeBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(employee: Employee) {
//            binding.textViewTitle.text = employee.name
//            binding.textViewSubtitle1.text = employee.position
//            binding.textViewSubtitle2.text = employee.salary.toString()
//        }
//    }
//
//    class EmployeeDiffCallback : DiffUtil.ItemCallback<Employee>() {
//
//        override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
//            return oldItem.name == newItem.name
//        }
//
//        override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
//            return oldItem == newItem
//        }
//
//    }
//}