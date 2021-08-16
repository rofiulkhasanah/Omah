package com.creartpro.smarthome.ui.perangkat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.creartpro.smarthome.databinding.ActivityPerangkatBinding
import com.creartpro.smarthome.entity.PerangkatEntity
import com.creartpro.smarthome.ui.home.HomeActivity
import java.net.HttpURLConnection
import java.net.URL


class PerangkatActivity : AppCompatActivity() {
    companion object {
        const val DEVICE = "device"
    }

    private lateinit var binding: ActivityPerangkatBinding
    private var status: String? = null
    private lateinit var endpointName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerangkatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailData = intent.getParcelableExtra<PerangkatEntity>(DEVICE)

        if (detailData != null) {
            endpointName = detailData.name
        }

        binding.apply {
            tvTitlePerangkat.text = detailData?.nama_perangkat
            if (detailData != null) {
                imgPerangkat.setImageResource(detailData.imgPerangkatOff)
            }

            imgBack.setOnClickListener {
                startActivity(Intent(this@PerangkatActivity, HomeActivity::class.java))
                finish()
            }
        }

        Log.d("nama perangkatnya", "${detailData?.name}")

        val url = "http://47.254.198.43//phpmqtt/examples/"
        var endpointReq: String?

        binding.apply {
            switchStatus.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    status = "on"
                    val request = url.plus(endpointName)
                    endpointReq = "${request.plus(status)}.php"
                    HandlerRequest().execute(endpointReq)
                    Log.d("nama perangkatnya", "$endpointReq")
                    if (detailData != null) {
                        imgPerangkat.setImageResource(detailData.imgPerangkatOn)
                    }
                    tvStatus.text = status
                }
                else {
                    status = "off"
                    val request = url.plus(endpointName)
                    endpointReq = "${request.plus(status)}.php"
                    HandlerRequest().execute(endpointReq)
                    Log.d("nama perangkatnya", "$endpointReq")
                    if (detailData != null) {
                        imgPerangkat.setImageResource(detailData.imgPerangkatOff)
                    }
                    tvStatus.text = status
                }
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    inner class HandlerRequest : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg url: String?): String {
            val text: String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text =
                    connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            } finally {
                connection.disconnect()
            }
            return text
        }

        override fun onPostExecute(result: String?) = super.onPostExecute(result)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}