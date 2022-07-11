package app.scanner.domain.filesystem

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import timber.log.Timber

class FileGallery(
    private val registry : ActivityResultRegistry
) : DefaultLifecycleObserver {
    lateinit var getContent : ActivityResultLauncher<String>
    private lateinit var uri: Uri
    var init: Boolean = false
    override fun onCreate(owner: LifecycleOwner) {
        getContent = registry.register("key", owner, ActivityResultContracts.GetContent()) {
            uri = it
            init = true
        }
    }
    fun selectImage() {
        getContent.launch("image/*")
    }

    fun getUri(): Uri? {
        return when {
            uri.path?.isNotEmpty() == true -> uri
            else -> null
        }
    }
}