package com.suraj.employee

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var employeeAdapter: EmployeeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)

        dbHelper = DatabaseHelper(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )

        employeeAdapter = EmployeeAdapter(
            dbHelper.getAllEmployees(),
            object : EmployeeAdapter.OnItemClickListener {
                override fun onItemClick(employeeId: Long) {
                    val intent = Intent(this@MainActivity, SecondActivity::class.java)
                    intent.putExtra("employeeId", employeeId)
                    startActivity(intent)
                }
            },
            object : EmployeeAdapter.OnEditClickListener {
                override fun onEditClick(employeeId: Long) {
                    val intent = Intent(this@MainActivity, DetailsEnterAcitivity::class.java)
                    intent.putExtra("edit_employee_id", employeeId)
                    startActivity(intent)
                }
            },
            object : EmployeeAdapter.OnDeleteClickListener {
                override fun onDeleteClick(employeeId: Long) {
                    dbHelper.removeEmployee(employeeId)
                    employeeAdapter.updateData(dbHelper.getAllEmployees())
                }
            }
        )

        recyclerView.adapter = employeeAdapter

        fab.setOnClickListener {
            val intent = Intent(this, DetailsEnterAcitivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        employeeAdapter.updateData(dbHelper.getAllEmployees())
    }
}
