package com.example.makebitmap

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.makebitmap.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 998
        const val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            btnSave.setOnClickListener {
//                viewSave(binding.main)
//                viewSave(binding.scrollview)
                viewSave(binding.streamingImage)
//                viewSave(binding.card)
//                viewSave(it)
            }
            btnPermission.setOnClickListener {
                checkPermission()
            }
        }

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            request_WRITE_EXTERNAL_STORAGE_Permission()
        } else if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            request_READ_EXTERNAL_STORAGE_Permission()
        }
    }

    private fun request_WRITE_EXTERNAL_STORAGE_Permission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE_PERMISSION_CODE)
    }

    private fun request_READ_EXTERNAL_STORAGE_Permission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_EXTERNAL_STORAGE_PERMISSION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "WRITE_EXTERNAL_STORAGE_PERMISSION_CODE 수신")
                } else {
                    // 권한 요청 거부했을 경우의 logic 처리
                    // ex) permission 재요청 or 무시 등등
                }
            }
            READ_EXTERNAL_STORAGE_PERMISSION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "READ_EXTERNAL_STORAGE_PERMISSION_CODE 수신")
                } else {
                    // 권한 요청 거부했을 경우의 logic 처리
                    // ex) permission 재요청 or 무시 등등
                }
            }
        }
    }

    private fun getViewBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun getSaveFilePathName(): String {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
//        val dir = File((Environment.getExternalStorageDirectory().absolutePath + File.pathSeparator + Environment.DIRECTORY_PICTURES))
//        Log.d(TAG, "dir: ${dir.path}")
//        Log.d(TAG, "!dir.exists(): ${!dir.exists()}")
//        if (!dir.exists()) {
//            dir.mkdir()
//        }
//        Log.d(TAG, "!dir.exists(): ${!dir.exists()}")
        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        return "$folder/$fileName.jpg"
    }

    private fun bitmapFileSave(bitmap: Bitmap, path: String) {
        Log.d(TAG, "path: ${path}")
        val fos: FileOutputStream
        try{
            fos = FileOutputStream(File(path))
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos)
            fos.close()
        }catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun viewSave(view: View) {
        val bitmap = getViewBitmap(view)
        val filePath = getSaveFilePathName()
        bitmapFileSave(bitmap, filePath)
    }
}