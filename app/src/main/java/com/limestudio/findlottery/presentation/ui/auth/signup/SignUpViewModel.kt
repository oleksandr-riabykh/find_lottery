package com.limestudio.findlottery.presentation.ui.auth.signup

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.limestudio.findlottery.data.firebase.FirebaseManager
import com.limestudio.findlottery.data.models.User
import com.limestudio.findlottery.extensions.containsNumber
import com.limestudio.findlottery.extensions.validateEmailAddress
import com.limestudio.findlottery.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel : BaseViewModel() {
    val state = MutableLiveData<SignUpScreenState>()

    fun uploadImages(userId: String, files: Map<Int, ImageModel?>) {

        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            val result = hashMapOf<Int, String>()
            val instance = FirebaseManager.getInstance(null)
            files.keys.forEach { key ->
                files[key]?.let { file -> result[key] = instance.uploadImage(userId, file) }
            }
            withContext(Dispatchers.Main) {
                state.postValue(SignUpScreenState.FilesUploaded(result))
            }
        }
    }

    fun addUser(user: User) {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            FirebaseManager.getInstance(null).addUser(user)
            withContext(Dispatchers.Main) {
                state.postValue(SignUpScreenState.UserSaved)
            }
        }
    }

    fun validateEmailAddress(email: String): Boolean = email.validateEmailAddress()

    fun isPasswordContainsNumber(password: String): Boolean = password.containsNumber()
}

data class ImageModel(val file: Uri, val folderName: String)