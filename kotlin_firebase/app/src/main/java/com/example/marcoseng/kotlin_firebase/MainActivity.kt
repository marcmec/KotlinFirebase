package com.example.marcoseng.kotlin_firebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var database = FirebaseDatabase.getInstance()
                var myref= database.getReference("LED")


        var editText = findViewById(R.id.edt_text1) as EditText
        var btn= findViewById(R.id.btn_inserir) as Button
        var datatxt= findViewById(R.id.txt_data) as TextView

        btn.setOnClickListener {

            var dado = editText.getText().toString()
            myref.setValue(dado)


        }

        myref.addValueEventListener( object : ValueEventListener{

            override fun onDataChange(data: DataSnapshot) {


                val value = data.getValue(String::class.java)

                datatxt.setText(value)


                }

            override fun onCancelled(p0: DatabaseError) {
                println("dont work")
            }


        })
    }


}