package com.ecosnap.fragments

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.ecosnap.Controller.fbDatabase.insertHistoryItem
import com.ecosnap.Model.dbHistoryItem
import com.ecosnap.R
import com.ecosnap.classifier.*
import com.ecosnap.utils.getCroppedBitmap
import kotlinx.android.synthetic.main.fragment_camera.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {
    private lateinit var classifier: Classifier
    private val MAX_PREVIEW_WIDTH = 1280
    private val MAX_PREVIEW_HEIGHT = 720
    private lateinit var userID: String
    private lateinit var timeStamp: String
    private lateinit var filePath: String
    private lateinit var captureSession: CameraCaptureSession
    private lateinit var captureRequestBuilder: CaptureRequest.Builder
    private lateinit var cameraDevice: CameraDevice
    private val handler: Handler = Handler()
    private val deviceStateCallback = object: CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice?) {
            Log.d(TAG, "camera device opened")
            if (camera !== null) {
                cameraDevice = camera
                previewSession()
            }
        }
        override fun onDisconnected(camera: CameraDevice?) {
            Log.d(TAG, "camera device disconnected")
            camera?.close()
        }
        override fun onError(camera: CameraDevice?, error: Int) {
            Log.d(TAG, "camera device error")
            this@CameraFragment.activity?.finish()
        }

    }
    private lateinit var galleryFolder: File
    private lateinit var backgroundThread: HandlerThread
    private lateinit var backgroundHandler: Handler
    private val cameraManager by lazy {
        activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("Camera Capture").also { it.start() }
        backgroundHandler = Handler(backgroundThread.looper)
    }
    private fun stopBackgroundThread() {
        backgroundThread.quitSafely()
        try {
            backgroundThread.join()
        } catch (e: InterruptedException) {
            Log.e(TAG, e.toString())
        }
    }

    private fun previewSession() {
        val surfaceTexture = camera_texture_view.surfaceTexture
        surfaceTexture.setDefaultBufferSize(MAX_PREVIEW_WIDTH, MAX_PREVIEW_HEIGHT)
        val surface = Surface(surfaceTexture)
        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequestBuilder.addTarget(surface)
        cameraDevice.createCaptureSession(Arrays.asList(surface),
                object: CameraCaptureSession.StateCallback() {
                    override fun onConfigureFailed(session: CameraCaptureSession?) {
                        Log.e(TAG, "creating capture session failed")
                    }

                    override fun onConfigured(session: CameraCaptureSession?) {
                        if (session != null) {
                            captureSession = session
                            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                            captureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null)
                        }
                    }

                }, null)
    }

    private fun closeCamera() {
        if (this::captureSession.isInitialized) {
            captureSession.close()
        }
        if (this::cameraDevice.isInitialized) {
            cameraDevice.close()
        }
    }

    private fun <T> cameraCharacteristics(cameraID: String, key: CameraCharacteristics.Key<T>) :T {
        val characteristics = cameraManager.getCameraCharacteristics(cameraID)
        return when (key) {
            CameraCharacteristics.LENS_FACING -> characteristics.get(key)
            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP -> characteristics.get(key)
            else -> throw IllegalArgumentException("Ket not recognized")
        }
    }

    private fun cameraID(lens: Int) :String {
        var deviceID = listOf<String>()
        try {
            val cameraIDList = cameraManager.cameraIdList
            deviceID = cameraIDList.filter { lens == cameraCharacteristics(it, CameraCharacteristics.LENS_FACING) }
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        }
        return deviceID[0]
    }

    private fun connectCamera() {
        val deviceID = cameraID(CameraCharacteristics.LENS_FACING_BACK)
        val streamConfigurationMap: StreamConfigurationMap = cameraManager.getCameraCharacteristics(deviceID).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        val previewSize = streamConfigurationMap.getOutputSizes(SurfaceTexture::class.java)[0]
        Log.d(TAG, "deviceID: $deviceID")
        try {
            cameraManager.openCamera(deviceID, deviceStateCallback, backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        } catch (e: InterruptedException) {
            Log.e(TAG, "Open camera device interrupted while opened")
        }
    }
    private var listener: OnCameraFragmentInteractionListener? = null

    companion object {
        const val REQUEST_CAMERA_PERMISSION = 100
        private val TAG = CameraFragment::class.qualifiedName
        @kotlin.jvm.JvmStatic fun newInstance() = CameraFragment()
    }

    private val surfaceListener = object: TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        }
        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) = Unit
        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?) = true
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @AfterPermissionGranted(REQUEST_CAMERA_PERMISSION)
    private fun checkCameraPermission() {
        if (EasyPermissions.hasPermissions(activity!!, android.Manifest.permission.CAMERA)) {
            connectCamera()
        } else {
            EasyPermissions.requestPermissions(activity!!,"App Requires Camera Permission",
                    REQUEST_CAMERA_PERMISSION, android.Manifest.permission.CAMERA)
        }
    }

   override fun onResume() {
       super.onResume()
       startBackgroundThread()
       if (camera_texture_view.isAvailable) {
           openCamera()
       } else {
           camera_texture_view.surfaceTextureListener = surfaceListener
       }
    }

    override fun onPause() {
        closeCamera()
        stopBackgroundThread()
        super.onPause()
    }

    private fun openCamera() {
        this.checkCameraPermission()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.userID = arguments?.getString("userID") as String
        }
        classifier = ImageClassifierFactory.create(
                context?.assets as AssetManager,
                GRAPH_FILE_PATH,
                LABELS_FILE_PATH,
                IMAGE_SIZE,
                GRAPH_INPUT_NAME,
                GRAPH_OUTPUT_NAME
        )
        startBackgroundThread()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onStart() {
        super.onStart()

        capture_image_button.setOnClickListener{
            this.takePicture()
        }

        close_image_button.setOnClickListener{
            this.retakePicture()
        }

        camera_label_report.setOnClickListener{
            this.displayReportOption()
        }

        hideLabels()
    }

    private fun displayReportOption() {
        val dialog = MaterialDialog.Builder(context as Context)
                .title("Reporting Misclassification")
                .content("Our model is not perfect, therefore we would love feedback from you. Would you like to provide feedback?")
                .positiveText("Yes")
                .negativeText("No")
                .positiveColor(resources.getColor(R.color.colorPrimaryDark))
                .onPositive{ materialDialog: MaterialDialog, dialogAction: DialogAction ->
                    this.displayLabelFeedback()
                }

        dialog.show()
    }


    private fun displayLabelFeedback() {
        val dialog = MaterialDialog.Builder(context as Context)
                .title("Was this item not classified correctly?")
                .positiveText("Yes")
                .negativeText("No")
                .positiveColor(resources.getColor(R.color.colorPrimaryDark))
                .onPositive{ materialDialog: MaterialDialog, dialogAction: DialogAction ->
                    // Write to db and adjust model
                }
                .onNegative{
                    materialDialog: MaterialDialog, dialogAction: DialogAction ->
                    // Write to db and adjust model
                }

        dialog.show()
    }

    private fun retakePicture() {
        hideLabels()
        unlock()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCameraFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }
    override fun onDetach() {
        stopBackgroundThread()
        closeCamera()
        super.onDetach()
        listener = null
    }

    override fun onStop() {
        stopBackgroundThread()
        closeCamera()
        super.onStop()
    }

    override fun onDestroy() {
        stopBackgroundThread()
        closeCamera()
        super.onDestroy()
    }

    private fun takePicture() {
        createImageGallery()
        lock()
        var outputPhoto: FileOutputStream? = null
        var outputFile: File? = null
        try {
            outputFile = createImageFile(galleryFolder)
            this.filePath = outputFile.toString()
            outputPhoto = FileOutputStream(outputFile)
            camera_texture_view.getBitmap()
                    .compress(Bitmap.CompressFormat.PNG, 100, outputPhoto)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        } finally {
//            unlock()
            // Show new fragment with overlay
            captureSession.stopRepeating()
            classifyImage(outputFile as File)
            try {
                if (outputPhoto != null) {
                    outputPhoto.close()
                }
            } catch (e: IOException) {
                Log.e(TAG, e.toString())
            }
        }
    }

    private fun lock() {
        try {
            captureSession.capture(captureRequestBuilder.build(), null, backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        }
    }

    private fun unlock() {
        try {
            captureSession.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler)
        } catch(e: CameraAccessException) {
            Log.e(TAG, e.toString())
        }
    }

    private fun createImageGallery() {
        val storageDirectory: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        galleryFolder = File(storageDirectory, resources.getString(R.string.app_name))
        Log.d(TAG, galleryFolder.absolutePath)
        if (!galleryFolder.exists()) {
            val wasCreated: Boolean = galleryFolder.mkdir()
            if (!wasCreated) {
                Log.e(TAG, "Failed to create directory")
            }
        }
    }

    private fun createImageFile(galleryFolder: File) :File {
        this.timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName: String = "image_" + this.timeStamp + "_"
        return File.createTempFile(imageFileName, ".jpg", galleryFolder)

    }
    interface OnCameraFragmentInteractionListener {
        fun onCaptureButton()
    }

    private fun classifyImage(file: File) {
        val photoBitmap = BitmapFactory.decodeFile(file.absolutePath)
        val croppedBitmap = getCroppedBitmap(photoBitmap)
        classifyAndShowResult(croppedBitmap)
    }

    private fun classifyAndShowResult(croppedBitmap: Bitmap) {
        runInBackground(
                Runnable {
                    // Handle Firebase Storage Here
                    val result = classifier.labelImage(croppedBitmap)
                    val confidenceRounded = "%.4f".format(result.confidence).toFloat() * 100
                    if (result.result == "recyclable") {
                        camera_label_icon.setImageResource(R.drawable.ic_pass)
                        image_camera_label.setTextColor(Color.GREEN)
                        image_camera_label.text = resources.getString(R.string.recyclable)
                    } else {
                        camera_label_icon.setImageResource(R.drawable.ic_reject)
                        image_camera_label.setTextColor(Color.RED)
                        image_camera_label.text = resources.getString(R.string.nonrecyclable)
                    }
                    image_camera_label_confidence.text = "Confidence: " + confidenceRounded + "%"
                    val historyItem = dbHistoryItem(result.result, confidenceRounded, this.timeStamp, this.filePath)
                    insertHistoryItem(this.userID, historyItem)
                    showLabels()
                })
    }

    private fun hideLabels() {
        image_camera_label.visibility = View.INVISIBLE
        close_image_button.visibility = View.INVISIBLE
        image_camera_label_confidence.visibility = View.INVISIBLE
        camera_label_icon.visibility = View.INVISIBLE
        camera_label_report.visibility = View.INVISIBLE
    }

    private fun showLabels() {
        image_camera_label.visibility = View.VISIBLE
        close_image_button.visibility = View.VISIBLE
        image_camera_label_confidence.visibility = View.VISIBLE
        camera_label_icon.visibility = View.VISIBLE
        camera_label_report.visibility = View.VISIBLE
    }

    @Synchronized
    private fun runInBackground(runnable: Runnable) {
        handler.post(runnable)
    }


}
