package com.example.enroute

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.view.Surface
import android.view.TextureView
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.enroute.ui.theme.EnrouteTheme
import com.example.enroute.views.IntroView
import com.example.enroute.views.StartDetectView
import com.example.enroute.views.SettingsView
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.nio.ByteBuffer
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import java.nio.ByteOrder

class MainActivity : ComponentActivity() {

    lateinit var labels: List<String>
    var colors = listOf<Int>(
        Color.BLUE, Color.GREEN, Color.RED, Color.CYAN, Color.GRAY, Color.BLACK,
        Color.DKGRAY, Color.MAGENTA, Color.YELLOW, Color.RED
    )
    val paint = Paint()

    // for object detection
    lateinit var bitmap: Bitmap
    lateinit var imageView: ImageView
    lateinit var cameraDevice: CameraDevice
    lateinit var handler: Handler
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    lateinit var interpreter: Interpreter
    lateinit var interpreter2: Interpreter
    lateinit var imageProcessor: ImageProcessor
    lateinit var previewView : PreviewView
    lateinit var drawingOverlay: DrawingOverlay

    var preview: Preview? = null
    lateinit var frameAnalyser : FrameAnalyser
    var frameAnalysis: ImageAnalysis? = null

    var isFrontCameraOn = true
    var isDepthMapDisplayed = false


    var result: String = "All Clear"
    val items = listOf(
        "person", "bicycle", "car", "motorcycle", "airplane", "bus", "train", "truck",
        "boat", "traffic light", "fire hydrant", "stop sign", "parking meter", "bench",
        "bird", "cat", "dog", "horse", "sheep", "cow", "elephant", "bear", "zebra",
        "giraffe", "backpack", "umbrella", "handbag", "tie", "suitcase", "frisbee",
        "skis", "snowboard", "sports ball", "kite", "baseball bat", "baseball glove",
        "skateboard", "surfboard", "tennis racket", "bottle", "wine glass", "cup",
        "fork", "knife", "spoon", "bowl", "banana", "apple", "sandwich", "orange",
        "broccoli", "carrot", "hot dog", "pizza", "donut", "cake", "chair", "couch",
        "potted plant", "bed", "dining table", "toilet", "tv", "laptop", "mouse",
        "remote", "keyboard", "cell phone", "microwave", "oven", "toaster", "sink",
        "refrigerator", "book", "clock", "vase", "scissors", "teddy bear", "hair drier",
        "toothbrush"
    )
    val items2 = listOf("staircase", "potholes", "person", "trees")

    // for depth perception
    private var depthImageState = mutableStateOf<Bitmap?>(null)
    private var inferenceTimeState = mutableLongStateOf(0)
    private var progressState = mutableStateOf(false)

    private var currentPhotoPath: String = ""
    private var selectedModelState = mutableStateOf("fused_model_uint8_256.onnx")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val depthEstimationModel = MiDASModel( this )
        frameAnalyser = FrameAnalyser( depthEstimationModel , drawingOverlay )

        // firebase models
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel("yolov5s", DownloadType.LOCAL_MODEL, conditions)
            .addOnSuccessListener { model: CustomModel? ->

                val modelFile = model?.file
                if (modelFile != null) {
                    interpreter = Interpreter(modelFile)
                }
            }
        val conditions2 = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel("yolov11_custom", DownloadType.LOCAL_MODEL, conditions)
            .addOnCompleteListener {
                val modelFile2 = model?.file
                if (modelFile2 != null) {
                    interpreter2 = Interpreter(modelFile2)
                }
            }

        enableEdgeToEdge()
        setContent {
            EnrouteTheme {
                // Shared mutable states
                val fontSizeState = remember { mutableStateOf(16f) } // Default font size
                val userNameState = remember { mutableStateOf("") } // Default user name
                val textToSpeechState =
                    remember { mutableStateOf(false) } // Default Text-to-Speech state
                val currentView = remember { mutableStateOf("intro") } // Track current view

                // Navigation logic based on currentView
                when (currentView.value) {
                    "intro" -> IntroView(
                        imagePath = R.drawable.custom_logo, // Replace with your image path
                        placeholderText = "Enter your name",
                        buttonText = "Next",
                        fontSizeState = fontSizeState,
                        userNameState = userNameState,
                        textToSpeechState = textToSpeechState,
                        currentView = currentView,
                        onNavigate = { destination ->
                            currentView.value = destination
                        } // Update currentView
                    )

                    "startDetect" -> StartDetectView(
                        userNameState = userNameState,
                        fontSizeState = fontSizeState,
                        textToSpeechState = textToSpeechState,
                        currentView = currentView,
                        onNavigate = { destination ->
                            currentView.value = destination
                        } // Update currentView
                    )

                    "idle" -> IdleView(
                        userNameState = userNameState,
                        fontSizeState = fontSizeState,
                        textToSpeechState = textToSpeechState,
                        currentView = currentView,
                        onNavigate = { destination ->
                            currentView.value = destination
                        } // Update currentView
                    )

                    "alert" -> AlertView(
                        detectedObject = result
                                currentView = currentView,
                        fontSizeState = fontSizeState,
                        onNavigate = { destination -> currentView.value = destination }
                    )

                    "settings" -> SettingsView(
                        fontSizeState = fontSizeState,
                        textToSpeechState = textToSpeechState,
                        userNameState = userNameState,
                        currentView = currentView,
                        onNavigate = { destination ->
                            currentView.value = destination
                        } // Update currentView
                    )
                }
            }
        }

        get_permission()

        imageView = findViewById(R.id.imageView)
        textureView = findViewById(R.id.textureView)

        imageProcessor =
            ImageProcessor.Builder().add(ResizeOp(640, 640, ResizeOp.ResizeMethod.BILINEAR)).build()

        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                open_camera()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                bitmap = textureView.bitmap!!
                var image = TensorImage.fromBitmap(bitmap)

                image = imageProcessor.process(image)
                val timage = image.tensorBuffer

//                 Runs model inference and gets result.


                val input = ByteBuffer.allocateDirect(1*640*640*3).order(ByteOrder.nativeOrder())
                val input2 = ByteBuffer.allocateDirect(1*640*640*3).order(ByteOrder.nativeOrder())
                val byteBuffer =
                    ByteBuffer.allocateDirect(1 * 640 * 640 * 3) // Batch x Width x Height x Channels x SizeOf(Float)


                val bufferSize = 1000 * java.lang.Float.SIZE / java.lang.Byte.SIZE
                val modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
                interpreter?.run(input, modelOutput)

                val modelOutput2 = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
                interpreter2?.run(input, modelOutput)

                val outputArray = modelOutput.asFloatBuffer() // Output tensor as a flat float array
                val outputArray2 = modelOutput2.asFloatBuffer()

                val numDetections = 25200

                val detections = mutableListOf<Detection>() // To store final detections

                for (i in 0 until numDetections) {

                    // Each detection has 85 values
                    val offset = i * 85

                    // Extract bounding box coordinates
                    val centerX = outputArray[offset]
                    val centerY = outputArray[offset + 1]
                    val width = outputArray[offset + 2]
                    val height = outputArray[offset + 3]

                    val objectt = outputArray[offset + 4]

                    // Extract class probabilities
                    val classScores = outputArray.copyOfRange(offset + 5, offset + 85)
                    val maxClassIndex = classScores.indices.maxByOrNull { classScores[it] } ?: -1
                    val maxClassScore = classScores[maxClassIndex]

                    // Calculate final confidence score
                    val confidence = objectt * maxClassScore

                    // Filter out low-confidence detections
                    if (confidence > 0.5) { // Threshold, adjust as needed
                        detections.add(
                            Detection(
                                x = centerX,
                                y = centerY,
                                width = width,
                                height = height,
                                classIndex = maxClassIndex,
                                confidence = confidence
                            )
                        )
                    }
                }
                val finalDetections = applyNMS(detections, 0.6)

                result = items[finalDetections[0].classindex]

                var mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                val canvas = Canvas(mutable)

                imageView.setImageBitmap(mutable)

            }
        }
    }
    fun open_camera() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        cameraManager.openCamera(
            cameraManager.cameraIdList[0],
            object : CameraDevice.StateCallback() {
                override fun onOpened(p0: CameraDevice) {
                    cameraDevice = p0

                    var surfaceTexture = textureView.surfaceTexture
                    var surface = Surface(surfaceTexture)

                    var captureRequest =
                        cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    captureRequest.addTarget(surface)

                    cameraDevice.createCaptureSession(
                        listOf(surface),
                        object : CameraCaptureSession.StateCallback() {
                            override fun onConfigured(p0: CameraCaptureSession) {
                                p0.setRepeatingRequest(captureRequest.build(), null, null)
                            }

                            override fun onConfigureFailed(p0: CameraCaptureSession) {
                            }
                        },
                        handler
                    )
                }
                override fun onDisconnected(p0: CameraDevice) {

                }
                override fun onError(p0: CameraDevice, p1: Int) {

                }
            },
            handler
        )
    }

    //class for detection
    data class Detection(
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float,
        val classIndex: Int,
        val confidence: Float
    )

    //class for
    fun applyNMS(detections: List<Detection>, iouThreshold: Double): List<Detection> {
        val finalDetections = mutableListOf<Detection>()

        // Group detections by class
        val groupedByClass = detections.groupBy { it.classIndex }

        for ((_, classDetections) in groupedByClass) {
            // Sort detections by confidence score (highest first)
            val sortedDetections = classDetections.sortedByDescending { it.confidence }

            val selectedDetections = mutableListOf<Detection>()
            val toRemove = mutableSetOf<Int>()

            for (i in sortedDetections.indices) {
                if (i in toRemove) continue

                val currentDetection = sortedDetections[i]
                selectedDetections.add(currentDetection)

                for (j in (i + 1) until sortedDetections.size) {
                    if (j in toRemove) continue

                    val otherDetection = sortedDetections[j]
                    val iou = calculateIoU(currentDetection, otherDetection)

                    if (iou > iouThreshold) {
                        toRemove.add(j)
                    }
                }
            }

            finalDetections.addAll(selectedDetections)
        }

        return finalDetections
    }

    fun calculateIoU(box1: Detection, box2: Detection): Float {
        val x1 = maxOf(box1.x - box1.width / 2, box2.x - box2.width / 2)
        val y1 = maxOf(box1.y - box1.height / 2, box2.y - box2.height / 2)
        val x2 = minOf(box1.x + box1.width / 2, box2.x + box2.width / 2)
        val y2 = minOf(box1.y + box1.height / 2, box2.y + box2.height / 2)

        val intersection = maxOf(0f, x2 - x1) * maxOf(0f, y2 - y1)
        val area1 = box1.width * box1.height
        val area2 = box2.width * box2.height
        val union = area1 + area2 - intersection

        return if (union == 0f) 0f else intersection / union
    }

    fun get_permission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            get_permission()
        }
    }




    }