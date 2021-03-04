package com.mobatia.bisad.pdfviewer

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
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

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class PdfViewer : AppCompatActivity() {
    lateinit var pdfviewer: PDFView
    lateinit var urltoshow: String
    lateinit var progressBar: RelativeLayout
    lateinit var btn_left: ImageView
    lateinit var sharepdf: ImageView
    lateinit var downloadpdf: ImageView
    private val STORAGE_PERMISSION_CODE: Int = 1000
    private var title = " ";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)
        pdfviewer = findViewById(R.id.pdfView)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        //progressBar = findViewById(R.id.progressDialog)
        progressBar = findViewById(R.id.progressbar)
        downloadpdf = findViewById(R.id.downloadpdf)
        sharepdf = findViewById(R.id.sharepdf)

        btn_left = findViewById(R.id.btn_left)
        urltoshow = intent.getStringExtra("Url")
        title = intent.getStringExtra("title")
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
        downloadpdf.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_CODE
                    )
                }

                    val fileWithinMyDir = File(getFilepath("$title.pdf"))

                if (fileWithinMyDir.exists()) {
                    fileWithinMyDir.delete()
                    startdownloading()
                    onDownloadComplete()
                } else {
                    startdownloading()
                    onDownloadComplete()
                }
            }
        }
        sharepdf.setOnClickListener {
            val intentShareFile = Intent(Intent.ACTION_SEND)
            val fileWithinMyDir = File(getFilepath(title + "docs.pdf"))
            if (fileWithinMyDir.exists()) {
                intentShareFile.type = "application/pdf"
                intentShareFile.putExtra(
                    Intent.EXTRA_STREAM,
                    Uri.parse("file://" + getFilepath(title + "docs.pdf"))
                )
                startActivity(Intent.createChooser(intentShareFile, "Share File"))
            } else {
                startdownloadingforshare()

                intentShareFile.type = "application/pdf"
                intentShareFile.putExtra(
                    Intent.EXTRA_STREAM,
                    Uri.parse("file://" + getFilepath(title + "docs.pdf"))
                )
                startActivity(Intent.createChooser(intentShareFile, "Share File"))

            }
        }

    }

    private fun startdownloadingforshare() {
        val request = DownloadManager.Request(Uri.parse(urltoshow))   //URL = URL to download
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS, title + "docs.pdf"

        )
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

     fun onDownloadComplete() {
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show()

            }

        }
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun startdownloading() {
        val request = DownloadManager.Request(Uri.parse(urltoshow))   //URL = URL to download
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("The file is downloading...")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$title.pdf")
        progressBar.visibility = View.VISIBLE
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    private fun getFilepath(filename: String): String? {
        return File(
            Environment.getExternalStorageDirectory().absolutePath,
            "/Download/$filename"
        ).path
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