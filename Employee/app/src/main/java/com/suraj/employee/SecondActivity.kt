package com.suraj.employee

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.suraj.employee.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    lateinit var nameField: TextView
    lateinit var idField: TextView
    lateinit var dobField: TextView
    lateinit var desigField: TextView
    lateinit var yoeField: TextView
    lateinit var addrField: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        nameField = findViewById(R.id.name)
        idField = findViewById(R.id.eid)
        dobField = findViewById(R.id.dob)
        desigField = findViewById(R.id.design)
        yoeField = findViewById(R.id.yoe)
        addrField = findViewById(R.id.addr)

        // Retrieve employee_id from the intent
        val employeeId = intent.getLongExtra("employeeId", -1)

        // Fetch employee details from the database using employee_id
        val dbHelper = DatabaseHelper(this)
        val receivedEmployee = dbHelper.getEmployeeById(employeeId)

        // Display employee details
        nameField.text = receivedEmployee?.name
        idField.text = receivedEmployee?.id.toString()
        dobField.text = receivedEmployee?.dob
        desigField.text = receivedEmployee?.designation
        yoeField.text = receivedEmployee?.yoe.toString()
        addrField.text = receivedEmployee?.address
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Handle back press
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
