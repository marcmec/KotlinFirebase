package com.example.marcoseng.kotlin_firebase

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.marcoseng.kotlin_firebase.helper.GraphicOverlay
import com.example.marcoseng.kotlin_firebase.helper.RectOverlay
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.wonderkiln.camerakit.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

lateinit var waitingDialog : android.app.AlertDialog
    override fun onResume() {
        cameraMl.start()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        cameraMl.stop()
    }

    var count=0

    var database = FirebaseDatabase.getInstance()
    var myref= database.getReference("LED")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        waitingDialog = SpotsDialog.Builder().setContext(this)
                .setMessage("espera mano...")
                .setCancelable(false)
                .build()

        btn_camera.setOnClickListener{
            cameraMl.start()
            cameraMl.captureImage()
            overlay_id.clear()
        }



        cameraMl.addCameraKitListener(object : CameraKitEventListener{
            override fun onVideo(p0: CameraKitVideo?) {
            }

            override fun onEvent(p0: CameraKitEvent?) {
            }

            override fun onImage(p0: CameraKitImage?) {
                    var bitmap = p0!!.bitmap
                        bitmap = Bitmap.createScaledBitmap(bitmap, cameraMl.width,cameraMl.height, false)
                        cameraMl.stop()

                runDetectorFaces(bitmap)
            }

            override fun onError(p0: CameraKitError?) {
            }


        })


       /* var editText = findViewById(R.id.edt_text1) as EditText
        var btn= findViewById(R.id.btn_inserir) as Button
        var datatxt= findViewById(R.id.txt_data) as TextView

        btn.setOnClickListener {

            var dado = editText.getText().toString()
            myref.setValue(dado)
        }*/



        myref.addValueEventListener( object : ValueEventListener{

            override fun onDataChange(data: DataSnapshot) {


                val value = data.getValue(String::class.java)

                //datatxt.setText(value)

                }

            override fun onCancelled(p0: DatabaseError) {
                println("dont work")
            }


        })
    }

    private fun runDetectorFaces(bitmap: Bitmap?) {

        val img = FirebaseVisionImage.fromBitmap(bitmap!!)
        val options = FirebaseVisionFaceDetectorOptions.Builder().build()
        val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)


        detector.detectInImage(img)
                .addOnSuccessListener { result -> processFaceResult(result) }
                .addOnFailureListener {er -> Toast.makeText(this, er.message,Toast.LENGTH_LONG).show()}

    }

    private fun processFaceResult(result: List<FirebaseVisionFace>) {

        //var count =0

        for (face in result){
            val bounds = face.boundingBox

            val rectOverlay =  RectOverlay(overlay_id,bounds)
            overlay_id.add(rectOverlay)

            count++
            myref.setValue(count.toString())

        }


        waitingDialog.dismiss()
                Toast.makeText(this,String.format("Voce achou %d rostos",count),Toast.LENGTH_LONG).show()

    }


}

