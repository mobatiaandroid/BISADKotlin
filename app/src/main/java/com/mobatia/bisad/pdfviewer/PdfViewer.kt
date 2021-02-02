package com.mobatia.bisad.pdfviewer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.github.barteksc.pdfviewer.PDFView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.home.mContext
import java.io.File

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PdfViewer : AppCompatActivity() {
    lateinit var pdfviewer: PDFView
    lateinit var urltoshow:String
    lateinit var progressBar: RelativeLayout
    lateinit var btn_left: ImageView
    //lateinit var progressBar:ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)
        pdfviewer = findViewById(R.id.pdfView)
        //progressBar = findViewById(R.id.progressDialog)
        progressBar = findViewById(R.id.progressbar)

        btn_left = findViewById(R.id.btn_left)
        urltoshow = intent.getStringExtra("Url")
        progressBar.visibility = View.VISIBLE

        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressBar.startAnimation(aniRotate)

        btn_left.setOnClickListener {
            finish()
        }


        PRDownloader.initialize(applicationContext)
        val fileName = "myFile.pdf"

        downloadPdfFromInternet(
            urltoshow,
            getRootDirPath(this),
            fileName
        )
        

    }
    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {
        PRDownloader.download(
            url,
            dirPath,
            fileName
        ).build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    val downloadedFile = File(dirPath, fileName)

                    showPdfFromFile(downloadedFile)
                }

                override fun onError(error: com.downloader.Error?) {

                }
                
            })
    }

    private fun showPdfFromFile(file: File) {
        pdfviewer.fromFile(file)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .onPageError { page, _ ->
            }
            .load()
        progressBar.visibility = View.GONE
    }

    fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

    override fun onBackPressed() {
        finish()
    }
}