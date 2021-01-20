package com.mobatia.bisad.fragment.contact_us

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.contact_us.model.ContactsListDetailModel
import java.security.AccessController.getContext

internal class ContactusAdapter(
    private var aboutuslist: List<ContactsListDetailModel>
) : RecyclerView.Adapter<ContactusAdapter.MyviewHolder>() {
    lateinit var context: Context

    internal inner class MyviewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.contactName)
        var number: TextView = view.findViewById(R.id.contactNumber)
        var email: TextView = view.findViewById(R.id.contactEmail)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactusAdapter.MyviewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_aboutus_list, parent, false)
        context = parent.context
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkpermission()
        }
        return MyviewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return aboutuslist.size
    }

    override fun onBindViewHolder(holder: ContactusAdapter.MyviewHolder, position: Int) {
        val data = aboutuslist[position]

        holder.name.text = data.name
        holder.email.text = data.email
        holder.number.text = data.phone

        holder.number.setOnClickListener {

            val dialIntent = Intent(Intent.ACTION_CALL)
            dialIntent.data = Uri.parse("tel:" + data.phone)
            context.startActivity(dialIntent)

        }

        holder.email.setOnClickListener {
            val emailIntent = Intent(
                Intent.ACTION_SEND_MULTIPLE
            )
            val deliveryAddress =
                arrayOf(holder.email.getText().toString())
            emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress)
            emailIntent.type = "text/plain"
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            val pm: PackageManager = it.context.packageManager
            val activityList = pm.queryIntentActivities(
                emailIntent, 0
            )
            for (app in activityList) {
                if (app.activityInfo.name.contains("com.google.android.gm")) {
                    val activity = app.activityInfo
                    val name = ComponentName(
                        activity.applicationInfo.packageName, activity.name
                    )
                    emailIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                    emailIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                            or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    emailIntent.component = name
                    it.context.startActivity(emailIntent)
                    break
                }
            }
        }
//            val intent = Intent(Intent.ACTION_SEND)
//            intent.type = ClipDescription.MIMETYPE_TEXT_PLAIN
//            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(data.email))
//            context.startActivity(Intent.createChooser(intent,""))
    }


    private fun checkpermission() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.CALL_PHONE
                ),
                123
            )
        }
    }

}
