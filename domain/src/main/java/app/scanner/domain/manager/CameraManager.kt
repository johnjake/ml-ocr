package app.scanner.domain.manager

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat

class CameraManager(
    private val context: Context,
    private val previewView: PreviewView,
 ) {
    private val provider: CameraProvider by lazy { CameraProvider(context, previewView) }

    fun builderInit(cameraBinding: (cameraProvider: ProcessCameraProvider,
                                    cameraSelector: CameraSelector,
                                    preview: Preview
    ) -> Unit) {
        provider.providerInstance().addListener({
        val process = provider.processProvider()
        val preview = provider.providePreview()
        val selector = provider.selectorProvider()
        cameraBinding(process, selector, preview)
        }, ContextCompat.getMainExecutor(context))
    }

    fun cameraPreview() {

    }
}