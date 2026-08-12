package com.example.pauze.data.repository

import android.content.Context
import android.net.Uri
import com.example.pauze.R
import com.example.pauze.data.model.SoundItem
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

@Singleton
class PauzeSoundLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    okHttpClient: OkHttpClient
) {
    private val preferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )
    private val metadataLock = Any()
    private val downloadMutexes = ConcurrentHashMap<String, Mutex>()
    private val downloadClient = okHttpClient.newBuilder()
        .apply {
            interceptors().removeAll { interceptor ->
                interceptor is HttpLoggingInterceptor
            }
        }
        .build()

    suspend fun getDownloadedSounds(): List<SoundItem> = withContext(Dispatchers.IO) {
        synchronized(metadataLock) {
            downloadedIds().mapNotNull(::readDownloadedSound)
        }
    }

    suspend fun downloadSound(sound: SoundItem): String = withContext(Dispatchers.IO) {
        require(sound.audioUrl.isNotBlank()) {
            "다운로드할 오디오 주소가 없습니다."
        }

        val safeId = sound.id.replace(UNSAFE_FILE_NAME_REGEX, "_")
        val downloadMutex = downloadMutexes.getOrPut(safeId) { Mutex() }

        downloadMutex.withLock {
            synchronized(metadataLock) {
                readDownloadedSound(sound.id)?.localFilePath
            }?.let { existingPath ->
                persistDownloadedSound(sound, existingPath)
                return@withLock existingPath
            }

            val directory = audioDirectory()
            val extension = sound.audioUrl.fileExtension()
            val targetFile = File(directory, "$safeId.$extension")
            val temporaryFile = File(directory, "$safeId.download")

            temporaryFile.delete()

            try {
                val request = Request.Builder()
                    .url(sound.audioUrl)
                    .get()
                    .build()

                downloadClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        throw IOException("오디오 다운로드에 실패했습니다. (${response.code})")
                    }

                    val responseBody = response.body
                        ?: throw IOException("다운로드할 오디오 파일이 없습니다.")

                    responseBody.byteStream().use { input ->
                        temporaryFile.outputStream().buffered().use { output ->
                            input.copyTo(output)
                        }
                    }
                }

                if (targetFile.exists() && !targetFile.delete()) {
                    throw IOException("기존 오디오 파일을 교체할 수 없습니다.")
                }
                if (!temporaryFile.renameTo(targetFile)) {
                    temporaryFile.copyTo(targetFile, overwrite = true)
                    temporaryFile.delete()
                }

                persistDownloadedSound(sound, targetFile.absolutePath)
                targetFile.absolutePath
            } catch (error: Throwable) {
                temporaryFile.delete()
                throw error
            }
        }
    }

    suspend fun deleteDownloadedSound(soundId: String) = withContext(Dispatchers.IO) {
        val localPath = synchronized(metadataLock) {
            readDownloadedSound(soundId)?.localFilePath
        }

        if (localPath != null) {
            val localFile = File(localPath)
            if (localFile.exists() && !localFile.delete()) {
                throw IOException("저장된 오디오 파일을 삭제할 수 없습니다.")
            }
        }

        synchronized(metadataLock) {
            removeMetadata(soundId)
        }
    }

    fun updateDownloadedLike(soundId: String, isLiked: Boolean) {
        synchronized(metadataLock) {
            if (soundId !in downloadedIds()) return
            preferences.edit()
                .putBoolean(soundId.key(FIELD_IS_LIKED), isLiked)
                .apply()
        }
    }

    private fun audioDirectory(): File {
        val directory = File(context.filesDir, AUDIO_DIRECTORY_NAME)
        if (!directory.exists() && !directory.mkdirs()) {
            throw IOException("오디오 저장 폴더를 만들 수 없습니다.")
        }
        return directory
    }

    private fun persistDownloadedSound(sound: SoundItem, localFilePath: String) {
        synchronized(metadataLock) {
            val ids = downloadedIds().toMutableSet().apply { add(sound.id) }
            val saved = preferences.edit()
                .putStringSet(KEY_DOWNLOADED_IDS, ids)
                .putString(sound.id.key(FIELD_TITLE), sound.title)
                .putString(sound.id.key(FIELD_CATEGORY), sound.category)
                .putBoolean(sound.id.key(FIELD_IS_LIKED), sound.isLiked)
                .putString(sound.id.key(FIELD_AUDIO_URL), sound.audioUrl)
                .putString(sound.id.key(FIELD_LOCAL_FILE_PATH), localFilePath)
                .commit()

            if (!saved) {
                File(localFilePath).delete()
                throw IOException("다운로드 정보를 저장할 수 없습니다.")
            }
        }
    }

    private fun readDownloadedSound(soundId: String): SoundItem? {
        val localFilePath = preferences.getString(
            soundId.key(FIELD_LOCAL_FILE_PATH),
            null
        ) ?: return null

        if (!File(localFilePath).isFile) {
            removeMetadata(soundId)
            return null
        }

        val title = preferences.getString(soundId.key(FIELD_TITLE), null)
            ?: return null
        val category = preferences.getString(soundId.key(FIELD_CATEGORY), null)
            ?: return null

        return SoundItem(
            id = soundId,
            title = title,
            category = category,
            isLiked = preferences.getBoolean(soundId.key(FIELD_IS_LIKED), false),
            isBookmarked = true,
            imageResId = if (title.contains("비", ignoreCase = true)) {
                R.drawable.ic_rain
            } else {
                R.drawable.ic_empty_image
            },
            audioUrl = preferences.getString(soundId.key(FIELD_AUDIO_URL), "").orEmpty(),
            localFilePath = localFilePath
        )
    }

    private fun removeMetadata(soundId: String) {
        val ids = downloadedIds().toMutableSet().apply { remove(soundId) }
        preferences.edit()
            .putStringSet(KEY_DOWNLOADED_IDS, ids)
            .remove(soundId.key(FIELD_TITLE))
            .remove(soundId.key(FIELD_CATEGORY))
            .remove(soundId.key(FIELD_IS_LIKED))
            .remove(soundId.key(FIELD_AUDIO_URL))
            .remove(soundId.key(FIELD_LOCAL_FILE_PATH))
            .apply()
    }

    private fun downloadedIds(): Set<String> =
        preferences.getStringSet(KEY_DOWNLOADED_IDS, emptySet())?.toSet().orEmpty()

    private fun String.key(field: String): String = "$this.$field"

    private fun String.fileExtension(): String {
        val extension = Uri.parse(this)
            .lastPathSegment
            .orEmpty()
            .substringAfterLast('.', missingDelimiterValue = "")
            .lowercase()

        return extension.takeIf { it.matches(SAFE_EXTENSION_REGEX) } ?: DEFAULT_EXTENSION
    }

    private companion object {
        const val PREFERENCES_NAME = "pauze_sound_downloads"
        const val AUDIO_DIRECTORY_NAME = "pauze_audio"
        const val KEY_DOWNLOADED_IDS = "downloaded_ids"
        const val FIELD_TITLE = "title"
        const val FIELD_CATEGORY = "category"
        const val FIELD_IS_LIKED = "is_liked"
        const val FIELD_AUDIO_URL = "audio_url"
        const val FIELD_LOCAL_FILE_PATH = "local_file_path"
        const val DEFAULT_EXTENSION = "mp3"

        val UNSAFE_FILE_NAME_REGEX = Regex("[^A-Za-z0-9_-]")
        val SAFE_EXTENSION_REGEX = Regex("[a-z0-9]{1,5}")
    }
}
