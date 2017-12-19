package org.firstinspires.ftc.teamcode.base;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Camera for taking pictures.
 * Manages the Android camera lifecycle and returns OpenCV images.
 */
public class SimpleCamera implements Camera.PreviewCallback, Camera.PictureCallback {
    /**
     * Initializes the phone's camera.
     * Attempts to get the first camera, which should be the back camera.
     */
    public SimpleCamera(final Context context) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        this.context = context;
        takingPicture = false;

        // Open the camera.
        camera = Camera.open();
        camera.setPreviewCallback(this);
        camera.enableShutterSound(true);

        // Create surface on UI thread.
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                synchronized(camera) {
                    // Create a surface to hold images.
                    surfaceTexture = new SurfaceTexture(10);
                    surfaceTexture.setDefaultBufferSize(
                            camera.getParameters().getPictureSize().width,
                            camera.getParameters().getPictureSize().height);
                    try {
                        camera.setPreviewTexture(surfaceTexture);
                    } catch (Exception e) {
                        Log.wtf(TAG, e);
                    }
                }
            }
        });
    }

    /**
     * Starts the process for capturing an image.
     * The image will be available from takeImage().
     * @return Whether the capture was started.
     */
    public boolean startCapture() {
        synchronized(camera) {
            if (surfaceTexture == null) {
                return false;
            }

            if (takingPicture) {
                return false;
            } else {
                camera.startPreview();
                takingPicture = true;
                return true;
            }
        }
    }

    /**
     * Gets the previously taken image.
     * @return The taken image, or null if the image is not available yet.
     */
    public Mat takeImage() {
        synchronized(this) {
            Mat taken = img;
            img = null;
            return taken;
        }
    }

    /**
     * Releases the camera.
     * Should be called when done with the camera to release it for future use.
     */
    public void stop() {
        synchronized(camera) {
            camera.release();
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.i(TAG, "Preview frame received.");
        try {
            camera.takePicture(null, null, this);
        } catch (Exception e) {
            Log.e(TAG, "Take picture failed: " + e.toString());
            synchronized(camera) {
                takingPicture = false;
            }
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        if (data == null) {
            Log.i(TAG, "No data.");
            return;
        }
        Log.i(TAG, "Picture taken: " + data.length);

        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "SimpleCamera.jpg");
            FileOutputStream output = new FileOutputStream(file);
            output.write(data);
            output.close();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        Mat jpeg = new MatOfByte(data);
        Mat decodedImg = Imgcodecs.imdecode(jpeg, Imgcodecs.CV_LOAD_IMAGE_COLOR);

        synchronized(this) {
            img = decodedImg;
        }
        synchronized(camera) {
            takingPicture = false;
        }
    }

    // Tag used for logging.
    private static final String TAG = "pmtischler.SimpleCamera";

    // The app context.
    private Context context;
    // Camera to take pictures with.
    private Camera camera;
    // Surface which holds pictures taken.
    private SurfaceTexture surfaceTexture;
    // Whether the camera is already taking a picture.
    private boolean takingPicture;
    // The latest image.
    private Mat img;
}
