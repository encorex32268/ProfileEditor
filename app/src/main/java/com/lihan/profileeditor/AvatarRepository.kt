package com.lihan.profileeditor

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

interface AvatarRepository {
    suspend fun saveToCache(bitmap: Bitmap)
    suspend fun loadFromCache(): Uri
}

class UserProfileRepository(
    private val context: Context
): AvatarRepository{

    companion object{
        private const val FILE_NAME = "user_avatar.jpg"
    }

    override suspend fun saveToCache(bitmap: Bitmap) {
        withContext(Dispatchers.IO){
            val file = File(context.cacheDir , FILE_NAME)
            file.outputStream().use {
                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    it
                )
            }
        }
    }

    override suspend fun loadFromCache(): Uri {
        return withContext(Dispatchers.IO){
             try {
                val file = File(context.cacheDir , FILE_NAME)
                if (file.exists()){
                    Uri.fromFile(file)
                }else{
                    Uri.EMPTY
                }
            }catch (e: Exception){
                e.printStackTrace()
                Uri.EMPTY
            }
        }
    }

}