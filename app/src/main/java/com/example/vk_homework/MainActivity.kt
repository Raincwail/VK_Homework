package com.example.vk_homework

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Log.println
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.vk_homework.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val client = OkHttpClient()
    var myBundle = Bundle()

    val TOKEN = "vk1.a.ksVFwZkH4N5QUxjBWHg3jmByx14mcQzVySWmEqqInX2X5Eb48yCrc6TH9X4sHehcw3UUy-bCUdQmOy8tRicngPlFuSyIsOx7bypFQW9Pup1bVa62KBQnqEuyiLc2tFMhfNAVYsoVrKuitcyKRjAlpUFe93TjCbD9lqieUTyNBQeG1CEQrl_fkWK10DDk8wBANguLcTAH-CU7d9nACV3rZQ"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }



    fun run(url: String) : String {
        val request = Request.Builder()
            .url(url)
            .build()

        var res = ""

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            for ((name, value) in response.headers) {
                println(Log.VERBOSE, "","$name: $value")
            }

            res = response.body!!.string()
        }

        return res
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun asyncRun(url: String) : Future<String> {
        val request = Request.Builder()
            .url(url)
            .build()

        val f = CompletableFuture<String>()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                f.completeExceptionally(e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    f.complete(response.body!!.string())
                }
            }
        })
        return f
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}