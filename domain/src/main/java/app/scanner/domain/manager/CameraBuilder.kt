package app.scanner.domain.manager

import android.content.Context
import androidx.annotation.NonNull
import androidx.annotation.RestrictTo
import androidx.camera.core.Camera
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import app.scanner.domain.R
import app.scanner.domain.extension.getActivity
import app.scanner.domain.extension.toast
import timber.log.Timber

@RestrictTo(RestrictTo.Scope.LIBRARY)
class CameraBuilder(
    @NonNull private val context: Context,
    @NonNull private val previewView: PreviewView,
    @NonNull private val lifecycleOwner: LifecycleOwner
) {
    private val provider: CameraProvider by lazy { CameraProvider(context, previewView) }
    private lateinit var camera: Camera
    private lateinit var cameraController: CameraControl
    private lateinit var cameraDetails: CameraInfo
    private lateinit var process: ProcessCameraProvider

    fun build() {
        provider.providerInstance().addListener({
            process = provider.processProvider()
            val preview = provider.providePreview()
            val selector = provider.selectorProvider()
            cameraPreview(process, selector, preview)
        }, ContextCompat.getMainExecutor(context))
    }

    private fun cameraPreview(
        cameraProvider: ProcessCameraProvider,
        cameraSelector: CameraSelector,
        preview: Preview
    ) {
        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
            camera.apply {
                when {
                    cameraInfo.hasFlashUnit() -> {
                        cameraController = cameraControl
                        cameraDetails = cameraInfo
                    }
                    else -> {
                        getActivity(context)?.toast(context.getString(R.string.no_flash))
                    }
                }
            }
        } catch (exc: Exception) {
            Timber.e("Error -> ${exc.localizedMessage}")
        }
    }

    private fun flashOn(flash: Boolean) {
        when (flash) {
            true -> {
                cameraController.enableTorch(cameraDetails.torchState.value == TorchState.OFF)
                cameraDetails.torchState.observe(lifecycleOwner) { torchState ->
                    when (torchState) {
                        TorchState.OFF -> getActivity(context)?.toast(context.getString(R.string.flash_off))
                        else -> getActivity(context)?.toast(context.getString(R.string.flash_on))
                    }
                }
            }
            else -> getActivity(context)?.toast(context.getString(R.string.flash_off))
        }
    }
}
