package com.creartpro.smarthome.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.creartpro.smarthome.R
import com.creartpro.smarthome.databinding.ActivityHomeBinding
import com.creartpro.smarthome.entity.PerangkatEntity
import com.creartpro.smarthome.entity.WeatherEntity
import com.google.gson.Gson
import id.co.telkom.iot.AntaresHTTPAPI
import id.co.telkom.iot.AntaresResponse
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class HomeActivity : AppCompatActivity(), AntaresHTTPAPI.OnResponseListener {

    companion object {
        private const val ACCESS_KEY = "908f6fa09a1dc492:9092afa23cd0d278"
        private const val DEVICE_NAME = "esp"
        private const val APP_NAME = "smarthomeaku"
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var deviceAdapter: PerangkatAdapter
    private val requestCode = 100
    private lateinit var antaresApi: AntaresHTTPAPI
    private val antaresAPI = "ANTARES-API"
    private var dataDevice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDevice()
        startRepeatingJob(5000)
    }

    private fun getDevice() {
        deviceAdapter = PerangkatAdapter(
            listOf(
                PerangkatEntity(
                    imgPerangkatOn = R.drawable.ic_lamp,
                    imgPerangkatOff = R.drawable.ic_lampu_off,
                    nama_perangkat = getString(R.string.lamp),
                    name = "lampu"
                ),
                PerangkatEntity(
                    imgPerangkatOn = R.drawable.ic_tv,
                    imgPerangkatOff = R.drawable.ic_tv_off,
                    nama_perangkat = getString(R.string.television),
                    name = "tv"
                ),
                PerangkatEntity(
                    imgPerangkatOn = R.drawable.ic_kipas,
                    imgPerangkatOff = R.drawable.ic_kipas_off,
                    nama_perangkat = getString(R.string.fan),
                    name = "kipas"
                ),
                PerangkatEntity(
                    imgPerangkatOn = R.drawable.ic_pompa,
                    imgPerangkatOff = R.drawable.ic_pompa_off,
                    nama_perangkat = getString(R.string.pump),
                    name = "pompa"
                ),
                PerangkatEntity(
                    imgPerangkatOn = R.drawable.ic_pagar,
                    imgPerangkatOff = R.drawable.ic_pagar_off,
                    nama_perangkat = getString(R.string.fence),
                    name = "pagar"
                )
            )
        )
        binding.apply {
            // Set Recycler View
            val layoutManager = GridLayoutManager(applicationContext, 2)
            rvPerangkat.adapter = deviceAdapter
            rvPerangkat.layoutManager = layoutManager

            // Voice
            btnVoice.setOnClickListener {
                speak()
            }

        }
    }

    private fun speak() {
        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        mIntent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            "Hi, Silahkan ucap perintah pada perangkat"
        )

        try {
            startActivityForResult(mIntent, requestCode)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            this.requestCode -> {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                    val kata = result?.get(0).toString()
                    val url = "http://47.254.198.43/index.php?sentence="
                    val request = url.plus(kata)
                    HandlerRequest().execute(request)
                    Log.d("nama perangkatnya", request)

                    binding.apply {
                        resultVoice.text = kata
                        resultVoice.visibility = View.VISIBLE

                        // Set Visible setelah di tampilkan dengan delay
                        resultVoice.postDelayed(
                            Runnable { resultVoice.visibility = View.GONE },
                            3000
                        )
                    }
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

    private fun getSuhu() {
        antaresApi = AntaresHTTPAPI()
        antaresApi.addListener(this)
        antaresApi.getLatestDataofDevice(ACCESS_KEY, APP_NAME, DEVICE_NAME)
    }

    override fun onResponse(antaresResp: AntaresResponse) {
        Log.d(antaresAPI, antaresResp.requestCode.toString())
        if (antaresResp.requestCode == 0) {
            try {
                val body = JSONObject(antaresResp.body)
                dataDevice = body.getJSONObject("m2m:cin").getString("con")

                val gson = Gson()
                val getData = gson.fromJson(dataDevice, WeatherEntity::class.java)

                val hum = getData.humidity
                val temp = getData.temperature
                runOnUiThread {
                    binding.apply {
                        tvHumidity.text = getString(R.string.humidity, hum.toString())
                        tvTemp.text = getString(R.string.temperature, temp.toString())
                    }
                }
                Log.d(
                    "data", "Humidity : ${hum.toString()}" +
                            ", Temperatue: ${temp.toString()}"
                )
            } catch (e: Exception) {
                Log.d("temperaturnya", e.message.toString())
            }
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    private fun startRepeatingJob(timeInterval: Long): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (NonCancellable.isActive) {
                getSuhu()
                delay(timeInterval)
            }
        }
    }

}