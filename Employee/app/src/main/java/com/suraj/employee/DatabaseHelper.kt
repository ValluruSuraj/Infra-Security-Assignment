package com.suraj.employee

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "EmployeeDatabase"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "employees"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DOB = "dob"
        private const val COLUMN_DESIGNATION = "designation"
        private const val COLUMN_YOE = "yoe"
        private const val COLUMN_ADDRESS = "address"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_DOB TEXT,"
                + "$COLUMN_DESIGNATION TEXT,"
                + "$COLUMN_YOE INTEGER,"
                + "$COLUMN_ADDRESS TEXT)")
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addEmployee(employee: Employee): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, employee.name)
        values.put(COLUMN_DOB, employee.dob)
        values.put(COLUMN_DESIGNATION, employee.designation)
        values.put(COLUMN_YOE, employee.yoe)
        values.put(COLUMN_ADDRESS, employee.address)

        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllEmployees(): ArrayList<Employee> {
        val employees = ArrayList<Employee>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val employee = Employee(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOB)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESIGNATION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YOE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
                )
                employees.add(employee)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return employees
    }

    fun updateEmployee(employee: Employee): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, employee.name)
        values.put(COLUMN_DOB, employee.dob)
        values.put(COLUMN_DESIGNATION, employee.designation)
        values.put(COLUMN_YOE, employee.yoe)
        values.put(COLUMN_ADDRESS, employee.address)

        return db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID = ?",
            arrayOf(employee.id.toString())
        )
    }

    fun removeEmployee(employeeId: Long): Int {
        val db = this.writableDatabase
        return db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ?",
            arrayOf(employeeId.toString())
        )
    }

    // Add this method to your DatabaseHelper class
    fun getEmployeeById(employeeId: Long): Employee? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_DOB,
                COLUMN_DESIGNATION,
                COLUMN_YOE,
                COLUMN_ADDRESS
            ),
            "$COLUMN_ID = ?",
            arrayOf(employeeId.toString()),
            null,
            null,
            null,
            null
        )

        var employee: Employee? = null

        if (cursor.moveToFirst()) {
            employee = Employee(
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOB)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESIGNATION)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YOE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
            )
        }

        cursor.close()
        return employee
    }

}
