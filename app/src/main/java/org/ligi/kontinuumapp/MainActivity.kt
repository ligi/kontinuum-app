package org.ligi.kontinuumapp

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager.LayoutParams.*
import kontinuum.model.WorkPackage
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        window.addFlags(FLAG_TURN_SCREEN_ON or FLAG_DISMISS_KEYGUARD or FLAG_FULLSCREEN)

        val retrofit = Retrofit.Builder()
                .baseUrl("http://li5.ddns.net:9001")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val service = retrofit.create(KontinuumApi::class.java)

        recycler_view.layoutManager = LinearLayoutManager(this)

        val callback = object : Callback<List<WorkPackage>> {
            override fun onFailure(call: Call<List<WorkPackage>>?, throwable: Throwable?) {
                throwable?.printStackTrace()
            }

            override fun onResponse(call: Call<List<WorkPackage>>?, response: Response<List<WorkPackage>>) {
                runOnUiThread {
                    val sortedList = response.body().sortedByDescending { it.epochSeconds }
                    recycler_view.adapter = WorkPackageAdapter(sortedList)
                }
            }

        }
        handler.post(object : Runnable {
            override fun run() {
                service.getWorkPackages().enqueue(callback)
                handler.postDelayed(this, 1000)
            }

        })

    }
}
