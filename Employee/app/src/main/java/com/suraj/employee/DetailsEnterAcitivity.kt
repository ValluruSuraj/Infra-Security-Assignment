package com.suraj.employee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText

class DetailsEnterAcitivity : AppCompatActivity() {

    lateinit var btnSubmit: Button
    lateinit var nameField: EditText
    lateinit var dobField: EditText
    lateinit var designationField: EditText
    lateinit var yoeField: EditText
    lateinit var addrField: EditText
    lateinit var dbHelper: DatabaseHelper

    var isEditing = false
    var employeeId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_enter_acitivity)

        btnSubmit = findViewById(R.id.btn_sub)
        nameField = findViewById(R.id.name)
        dobField = findViewById(R.id.dob)
        designationField = findViewById(R.id.design)
        yoeField = findViewById(R.id.yoe)
        addrField = findViewById(R.id.addr)

        yoeField.inputType = InputType.TYPE_CLASS_NUMBER

        dbHelper = DatabaseHelper(this)

        if (intent.hasExtra("edit_employee_id")) {
            isEditing = true
            employeeId = intent.getLongExtra("edit_employee_id", -1)

            val existingEmployee = dbHelper.getEmployeeById(employeeId)

            nameField.setText(existingEmployee?.name)
            dobField.setText(existingEmployee?.dob)
            designationField.setText(existingEmployee?.designation)
            yoeField.setText(existingEmployee?.yoe.toString())
            addrField.setText(existingEmployee?.address)
        }

        btnSubmit.setOnClickListener {
            if (isEditing) {
                val updatedEmployee = Employee(
                    id = employeeId,
                    name = nameField.text.toString(),
                    dob = dobField.text.toString(),
                    designation = designationField.text.toString(),
                    yoe = yoeField.text.toString().toInt(),
                    address = addrField.text.toString()
                )

                dbHelper.updateEmployee(updatedEmployee)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                val newEmployee = Employee(
                    name = nameField.text.toString(),
                    dob = dobField.text.toString(),
                    designation = designationField.text.toString(),
                    yoe = yoeField.text.toString().toInt(),
                    address = addrField.text.toString()
                )

                employeeId = dbHelper.addEmployee(newEmployee)


                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("employeeId", employeeId)
                startActivity(intent)
            }
        }
    }
}
