package com.nexorai.soundwallpaper

import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.io.File
import java.io.FileOutputStream

class MainActivity : Activity() {
    private lateinit var statusText: TextView
    private val videoFile: File by lazy { File(filesDir, VIDEO_FILE_NAME) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUi()
        updateStatus()
    }

    private fun buildUi() {
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(24), dp(32), dp(24), dp(28))
            setBackgroundColor(Color.rgb(245, 244, 250))
        }
        val title = TextView(this).apply {
            text = "خلفية فيديو بصوت"
            textSize = 27f
            setTextColor(Color.rgb(30, 30, 40))
            gravity = Gravity.CENTER
        }
        val subtitle = TextView(this).apply {
            text = "اختَر الفيديو من موبايلك، ثم عيّنه كخلفية حية. يحاول التطبيق تشغيل الصوت فقط أثناء ظهور الخلفية."
            textSize = 16f
            setTextColor(Color.rgb(70, 70, 85))
            gravity = Gravity.CENTER
            setPadding(0, dp(12), 0, dp(24))
        }
        statusText = TextView(this).apply {
            textSize = 15f
            setTextColor(Color.rgb(40, 40, 55))
            gravity = Gravity.CENTER
            setPadding(dp(12), dp(12), dp(12), dp(16))
        }
        val chooseButton = Button(this).apply {
            text = "١. اختيار الفيديو"
            isAllCaps = false
            setOnClickListener { chooseVideo() }
        }
        val setButton = Button(this).apply {
            text = "٢. تعيين كخلفية"
            isAllCaps = false
            setOnClickListener { setLiveWallpaper() }
        }
        val note = TextView(this).apply {
            text = "ملاحظة: تشغيل الصوت يعتمد على طريقة إدارة الخلفيات في جهازك. قد يوقف النظام الصوت أو الخلفية لتوفير البطارية."
            textSize = 13f
            setTextColor(Color.rgb(100, 100, 115))
            gravity = Gravity.CENTER
            setPadding(0, dp(22), 0, 0)
        }
        page.addView(title, matchWrap())
        page.addView(subtitle, matchWrap())
        page.addView(statusText, matchWrap())
        page.addView(chooseButton, matchWrap())
        page.addView(setButton, matchWrap())
        page.addView(note, matchWrap())
        setContentView(page)
    }

    private fun chooseVideo() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "video/*"
        }
        try {
            startActivityForResult(intent, REQUEST_VIDEO)
        } catch (_: Exception) {
            statusText.text = "لم أستطع فتح منتقي الملفات على هذا الجهاز."
        }
    }

    @Deprecated("Uses the platform file picker for broad Android compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_VIDEO || resultCode != RESULT_OK) return
        val uri: Uri = data?.data ?: return
        try {
            val input = contentResolver.openInputStream(uri)
                ?: throw IllegalStateException("تعذّر فتح ملف الفيديو")
            input.use { stream ->
                FileOutputStream(videoFile).use { output -> stream.copyTo(output) }
            }
            statusText.text = "تم استيراد الفيديو بنجاح ✅\nالحجم: ${formatSize(videoFile.length())}"
        } catch (e: Exception) {
            statusText.text = "فشل استيراد الفيديو: ${e.localizedMessage ?: "خطأ غير معروف"}"
        }
    }

    private fun setLiveWallpaper() {
        if (!videoFile.exists() || videoFile.length() == 0L) {
            statusText.text = "اختَر ملف الفيديو أولًا."
            return
        }
        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
            putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this@MainActivity, VideoWallpaperService::class.java)
            )
        }
        try {
            startActivity(intent)
        } catch (_: Exception) {
            statusText.text = "تعذّر فتح شاشة تعيين الخلفية الحية على جهازك."
        }
    }

    private fun updateStatus() {
        statusText.text = if (videoFile.exists() && videoFile.length() > 0L) {
            "الفيديو جاهز ✅\nالحجم: ${formatSize(videoFile.length())}"
        } else {
            "لم يتم اختيار فيديو بعد."
        }
    }

    private fun formatSize(bytes: Long): String = when {
        bytes >= 1024L * 1024L -> String.format("%.1f MB", bytes / (1024.0 * 1024.0))
        bytes >= 1024L -> String.format("%.0f KB", bytes / 1024.0)
        else -> "$bytes B"
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
    private fun matchWrap() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply { bottomMargin = dp(8) }

    companion object {
        private const val REQUEST_VIDEO = 1001
        private const val VIDEO_FILE_NAME = "wallpaper.mp4"
    }
}
