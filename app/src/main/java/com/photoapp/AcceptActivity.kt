package com.photoapp

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.photoapp.api.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AcceptActivity : AppCompatActivity(), UploadRequestBody.UploadCallBack{
    private lateinit var instanceOfRootLayout: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var instanceOfImageView: ImageView
    private lateinit var instanceOfCaptureButton:Button
    private lateinit var instanceOfExtractButton: Button
    private lateinit var instanceOfBitMap: Bitmap
    private lateinit var instanceOfUploadButton: Button
    private var selectedImage:Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept)

        instanceOfImageView = findViewById(R.id.imageViewId)
        instanceOfCaptureButton = findViewById(R.id.captureButtonId)
        instanceOfExtractButton = findViewById(R.id.extractButtonId)
        instanceOfUploadButton = findViewById(R.id.uploadButtonId)


        instanceOfCaptureButton.setOnClickListener {
            captureFunction()
        }

        instanceOfExtractButton.setOnClickListener {
            extractFunction()
        }

        instanceOfUploadButton.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        if(selectedImage == null){
            instanceOfRootLayout.snackbar("Image not selected")
            return
        }
        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImage!!, "r", null)?: return
        val file = File(cacheDir, contentResolver.getFileName(selectedImage!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file,"image", this)

        MyApi().uploadImage(
            MultipartBody.Part.createFormData("image", file.name, body),
            RequestBody.create(MediaType.parse("multipart/form-data"), "Dummy file")
        ).enqueue(object: Callback<UploadResponse>{
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                Toast.makeText(this@AcceptActivity, "Image uploaded successfully", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Toast.makeText(this@AcceptActivity, "${t.message}!!", Toast.LENGTH_LONG).show()
            }

        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            instanceOfBitMap = data?.extras?.get("data") as Bitmap
            instanceOfImageView.setImageBitmap(instanceOfBitMap)
        }
        else if (requestCode == 456){
            instanceOfImageView.setImageURI(data?.data)
            selectedImage = data?.data
        }
    }

    private fun extractFunction() {
        val intentExtract = Intent(Intent.ACTION_PICK)
        intentExtract.type = "image/*"
        startActivityForResult(intentExtract, 456)
    }

    private fun captureFunction() {
        val intentCapture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intentCapture, 123)
    }

    override fun onProgressUpdate(percentage: Int) {
//        Toast.makeText(this@AcceptActivity, "Image uploaded successfully", Toast.LENGTH_LONG).show()

    }


}