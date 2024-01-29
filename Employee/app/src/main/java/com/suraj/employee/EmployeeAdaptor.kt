package com.suraj.employee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmployeeAdapter(
    private var employees: List<Employee>,
    private val itemClickListener: OnItemClickListener,
    private val editClickListener: OnEditClickListener,
    private val deleteClickListener: OnDeleteClickListener
) : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    interface OnEditClickListener {
        fun onEditClick(employeeId: Long)
    }
    interface OnItemClickListener {
        fun onItemClick(employeeId: Long)
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(employeeId: Long)
    }

    class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val idTextView: TextView = itemView.findViewById(R.id.idTextView)
        val editBtn: Button = itemView.findViewById(R.id.editButton)
        val deleteBtn: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_employee, parent, false)

        return EmployeeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val currentEmployee = employees[position]

        holder.idTextView.text = "ID: ${currentEmployee.id}"
        holder.nameTextView.text = currentEmployee.name

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(currentEmployee.id)
        }

        holder.editBtn.setOnClickListener {
            editClickListener.onEditClick(currentEmployee.id)
        }

        holder.deleteBtn.setOnClickListener {
            deleteClickListener.onDeleteClick(currentEmployee.id)
        }
    }

    override fun getItemCount(): Int {
        return employees.size
    }

    fun updateData(newEmployees: List<Employee>) {
        employees = newEmployees
        notifyDataSetChanged()
    }
}
