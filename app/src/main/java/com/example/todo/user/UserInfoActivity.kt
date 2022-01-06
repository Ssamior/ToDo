package com.example.todo.user

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.todo.R
import com.google.android.material.snackbar.Snackbar
import com.google.modernstorage.mediastore.FileType
import com.google.modernstorage.mediastore.MediaStoreRepository
import com.google.modernstorage.mediastore.SharedPrimary
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

class UserInfoActivity : AppCompatActivity() {

    private val userWebService = Api.userWebService;

    private var takePictureButton: Button? = null
    private var uploadImageButton: Button? = null

    private val mediaStore by lazy { MediaStoreRepository(this) }
    private lateinit var photoUri: Uri

    private var viewModel = UserInfoViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info);

        takePictureButton = findViewById(R.id.take_picture_button);
        uploadImageButton = findViewById(R.id.upload_image_button);

        takePictureButton?.setOnClickListener {
            launchCameraWithPermission()
        }
        uploadImageButton?.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        lifecycleScope.launchWhenStarted {
            photoUri = mediaStore.createMediaUri(
                filename = "picture-${UUID.randomUUID()}.jpg",
                type = FileType.IMAGE,
                location = SharedPrimary
            ).getOrThrow();
        }

        lifecycleScope.launch {
            viewModel.userInfo.collect { userInfo ->

            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenStarted {
            photoUri = mediaStore.createMediaUri(
                filename = "picture-${UUID.randomUUID()}.jpg",
                type = FileType.IMAGE,
                location = SharedPrimary
            ).getOrThrow()
        }
        viewModel.refresh()
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { accepted ->
            if (accepted) launchCamera()
            else showExplanation()
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { accepted ->
            if (accepted) handleImage(photoUri)
            else Snackbar.make(window.decorView.rootView, "Failed", Snackbar.LENGTH_LONG).show()
        }


    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if(it != null) {
            handleImage(it)
        }
    }

    private fun launchCameraWithPermission() {
        val camPermission = Manifest.permission.CAMERA
        val permissionStatus = checkSelfPermission(camPermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)
        when {
            isAlreadyAccepted -> launchCamera()
            isExplanationNeeded -> showExplanation()
            else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun showExplanation() {
        AlertDialog.Builder(this)
            .setMessage("Need camera access")
            .setPositiveButton("Accept") { _, _ -> launchAppSettings()}
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun launchAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        startActivity(intent)
    }

    private fun convert(uri: Uri): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
        )
    }

    private fun handleImage(imageUri: Uri) {
        viewModel.updateAvatar(convert(imageUri))
    }

    private fun launchCamera() {
        lifecycleScope.launchWhenStarted {
            photoUri = mediaStore.createMediaUri(
                filename = "picture-${UUID.randomUUID()}.jpg",
                type = FileType.IMAGE,
                location = SharedPrimary
            ).getOrThrow()
            cameraLauncher.launch(photoUri)
        }
    }

}