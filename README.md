# Enroute

### Overview
This Kotlin and Java project is designed to integrate object detection creating an application that detects dangerous impediments for disabled users on their daily routes. The project leverages a custom YoloV11 machine-learning model and a standard YoloV5 object detection model to identify objects. With real-time data processing, the application ensures responsiveness and accuracy during operation.
Key features include an intuitive user interface tailored for accessibility, robust backend algorithms for handling complex detection tasks, and seamless integration of multiple models to enhance the user experience. The project also incorporates advanced deployment strategies such as model conversion to TernsorflowLite to optimize performance on Android devices, making it suitable for applications such as navigation assistance or interactive object recognition.

### Code

We used multiple different sources for our backend code. Our intial version of the project uses [Real-Time Object Detection code](https://www.youtube.com/watch?v=zs43IrWTzB0) to obtain video permissions and input from the camera on a user's device. We integrated this with our YoloV5 and YolovV11 FireBase models that we coded in, and with our UI pipeline to display the correct outputs. 

For Depth Perception, we started to implement [Depth Perception](https://github.com/shubham0204/Realtime_MiDaS_Depth_Estimation_Android/tree/master) but ran into multiple and consistent dependency issues which we were not able to resolve. Our progress on this front can be seen in the Enroute 2 file. 

We used [an example](https://github.com/google-ai-edge/mediapipe-samples/tree/main/examples/object_detection/android) application which had a built-in ML model, and an interface for live object-detection and object detection on input video. We changed this app to run with our user interface, integrating the outputs of the model with our UI. This code can be found [here](https://github.com/barbicgem/android.git).

The new code created across our files includes all of the front-end, user interface files, functions for drawing bounding-boxes and filtering out redundant bounding-boxes, as well as the pipelines for navigation in the app. 

A link to our custom dataset used to train the YoloV11 model can be found [here](https://universe.roboflow.com/t-buiyt/haii). 
