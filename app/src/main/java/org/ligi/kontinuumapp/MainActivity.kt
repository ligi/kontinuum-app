package org.ligi.kontinuumapp

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager.LayoutParams.*
import kontinuum.model.WorkPackage
import kotlinx.android.synthetic.main.activity_main.*
import org.ligi.kaxt.dismissIfNotNullAndShowing
import org.ligi.kaxt.getStackTraceString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    val handler = Handler()
    var alert: AlertDialog? = null

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
                throwable?.let {
                    it.printStackTrace()
                    runOnUiThread {
                        if (!isFinishing) {
                            alert.dismissIfNotNullAndShowing()
                            alert = AlertDialog.Builder(this@MainActivity)
                                    .setMessage(it.getStackTraceString())
                                    .show()
                        }
                    }
                }

            }

            override fun onResponse(call: Call<List<WorkPackage>>?, response: Response<List<WorkPackage>>) {
                runOnUiThread {
                    if (!isFinishing) {
                        alert.dismissIfNotNullAndShowing()
                        alert = null
                        val body = response.body()
                        if (body != null) {
                            val sortedList = body.sortedByDescending { it.epochSeconds }
                            recycler_view.adapter = WorkPackageAdapter(sortedList)
                        }
                    }
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

    override fun onPause() {
        alert.dismissIfNotNullAndShowing()
        alert = null

        super.onPause()
    }
}
