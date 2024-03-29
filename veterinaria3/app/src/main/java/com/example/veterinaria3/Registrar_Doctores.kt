package com.example.veterinaria3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.log

class Registrar_Doctores : AppCompatActivity() {

    private lateinit var edName: EditText
    private lateinit var edpuesto: EditText
    private  lateinit var edarea: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnview: Button
    private lateinit var btnUpdate: Button

    private lateinit var sqlitehelper: SQLitehelper2
    private lateinit var recyclerView: RecyclerView
    private  var adapter: Doctoradapter?=null
    private var std:DoctorModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_doctores)

        initview()
        initRecyclerview()
        sqlitehelper = SQLitehelper2(this)

        btnAdd.setOnClickListener { adddoctor() }
        btnview.setOnClickListener { getdoctor() }
        btnUpdate.setOnClickListener { updateDoctor() }
        adapter?.setOnclickItem {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()

            edName.setText(it.name)
            edpuesto.setText(it.puesto)
            edarea.setText(it.area)
            std = it

        }
        adapter?.setOnclickDeleteItem {
            deleteDoctor(it.id)
        }

    }

    private fun getdoctor(){

        val stdlist = sqlitehelper.getAllDoctores()
        Log.e("pppp", "${stdlist.size}")

        adapter?.addItems(stdlist)
    }

    private fun adddoctor(){

        val name = edName.text.toString()
        val puesto = edpuesto.text.toString()
        val area= edarea.text.toString()

        if (name.isEmpty() || puesto.isEmpty() || area.isEmpty()){
            Toast.makeText(this, "Please enter required field", Toast.LENGTH_SHORT).show()
        }
        else{
            val std = DoctorModel(name=name, puesto = puesto, area = area)
            val status = sqlitehelper.insertDoctor(std)

            if (status > -1){
                Toast.makeText(this, "doctor added", Toast.LENGTH_SHORT ).show()
                clearEditText()
                getdoctor()
            }
            else{
                Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, status.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateDoctor(){

        val name = edName.text.toString()
        val puesto = edpuesto.text.toString()
        val area= edarea.text.toString()

        if (name == std?.name && puesto ==std?.puesto && area == std?.area){
            Toast.makeText(this, "record not changed", Toast.LENGTH_SHORT).show()
            return
        }
        if (std == null)return


        val std = DoctorModel(id = std!!.id, name = name, puesto = puesto, area = area)
        val status = sqlitehelper.UpdateDoctor(std)

        if (status > -1){
            clearEditText()
        }
        else{
            Toast.makeText(this,    "Update failed", Toast.LENGTH_SHORT ).show()
        }

        getdoctor()

    }

    private fun deleteDoctor(id:Int){


        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete item")
        builder.setCancelable(true)
        builder.setPositiveButton("yes"){ dialog, _ ->
            sqlitehelper.deletedoctorById(id)
            getdoctor()

            dialog.dismiss()

        }
        builder.setNegativeButton("No"){ dialog, _ ->

            dialog.dismiss()

        }

        val alert = builder.create()
        alert.show()
    }

    private fun clearEditText() {
        edName.setText("")
        edpuesto.setText("")
        edarea.setText("")
        edName.requestFocus()
    }

    private  fun initRecyclerview(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Doctoradapter()
        recyclerView.adapter = adapter
    }

    private fun initview(){

        edName = findViewById(R.id.editTextText7)
        edpuesto = findViewById(R.id.editTextText8)
        edarea = findViewById(R.id.editTextText9)
        btnAdd = findViewById(R.id.button18)
        btnview = findViewById(R.id.button19)
        btnUpdate =findViewById(R.id.button20)
        recyclerView = findViewById(R.id.recycleview2)
    }


}