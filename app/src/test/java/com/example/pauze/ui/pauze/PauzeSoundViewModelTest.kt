package com.example.pauze.ui.pauze

import com.example.pauze.data.model.SoundItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PauzeSoundViewModelTest {
    @Test
    fun `원격에 없는 다운로드 항목은 병합 결과에 유지한다`() {
        val remoteSound = sound(id = "remote")
        val downloadedLocalSound = sound(
            id = "downloaded",
            isBookmarked = true,
            localFilePath = "/files/pauze_audio/downloaded.mp3"
        )

        val result = mergeRemoteWithLocal(
            remoteSounds = listOf(remoteSound),
            localSounds = listOf(downloadedLocalSound)
        )

        assertEquals(listOf("remote", "downloaded"), result.map(SoundItem::id))
        assertEquals(downloadedLocalSound, result.last())
    }

    @Test
    fun `원격에 없고 다운로드하지 않은 로컬 항목은 병합 결과에서 제외한다`() {
        val result = mergeRemoteWithLocal(
            remoteSounds = listOf(sound(id = "remote")),
            localSounds = listOf(sound(id = "local-only"))
        )

        assertEquals(listOf("remote"), result.map(SoundItem::id))
    }

    @Test
    fun `같은 항목은 원격 정보를 기준으로 로컬 상태와 경로를 반영한다`() {
        val remoteSound = sound(
            id = "same",
            title = "서버 제목",
            audioUrl = "https://pauze.cloud/same.mp3"
        )
        val localSound = sound(
            id = "same",
            title = "로컬 제목",
            isLiked = true,
            isBookmarked = true,
            localFilePath = "/files/pauze_audio/same.mp3"
        )

        val result = mergeRemoteWithLocal(
            remoteSounds = listOf(remoteSound),
            localSounds = listOf(localSound)
        ).single()

        assertEquals("서버 제목", result.title)
        assertEquals("https://pauze.cloud/same.mp3", result.audioUrl)
        assertTrue(result.isLiked)
        assertTrue(result.isBookmarked)
        assertEquals("/files/pauze_audio/same.mp3", result.localFilePath)
        assertFalse(result.title == localSound.title)
    }
}

private fun sound(
    id: String,
    title: String = id,
    isLiked: Boolean = false,
    isBookmarked: Boolean = false,
    audioUrl: String = "",
    localFilePath: String? = null
) = SoundItem(
    id = id,
    title = title,
    category = "자연소리",
    isLiked = isLiked,
    isBookmarked = isBookmarked,
    imageResId = 0,
    audioUrl = audioUrl,
    localFilePath = localFilePath
)
