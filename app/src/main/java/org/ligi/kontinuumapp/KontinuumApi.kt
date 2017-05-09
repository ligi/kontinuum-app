package org.ligi.kontinuumapp

import kontinuum.model.WorkPackage
import retrofit2.Call
import retrofit2.http.GET

interface KontinuumApi {
    @GET("/api")
    fun getWorkPackages(): Call<List<WorkPackage>>
}