package app.scanner.domain.manager

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import com.google.common.util.concurrent.ListenableFuture

class CameraProvider(
    private val context: Context,
    private val previewView: PreviewView
) {
    fun providerInstance(): ListenableFuture<ProcessCameraProvider> {
       return ProcessCameraProvider.getInstance(context)
    }

    fun processProvider(): ProcessCameraProvider {
        return providerInstance().get()
    }

    fun providePreview(): Preview {
        val previewBinding = previewView.surfaceProvider
        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(previewBinding)
            }
        return preview
    }

    fun selectorProvider() = CameraSelector.DEFAULT_BACK_CAMERA
}