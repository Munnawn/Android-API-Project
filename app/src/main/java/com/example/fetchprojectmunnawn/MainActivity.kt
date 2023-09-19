package com.example.fetchprojectmunnawn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.ArrayAdapter
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.Callback
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    private val dataList: MutableList<UserData> = mutableListOf()
    private val tag = "MainActivity"

    interface RequestData {
        @GET("/hiring.json")
        fun getData(): Call<List<UserData>>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://fetch-hiring.s3.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val requestData = retrofit.create(RequestData::class.java)
        requestData.getData().enqueue(object : Callback<List<UserData>> {
            override fun onResponse(call: Call<List<UserData>>, response: Response<List<UserData>>) {
                if (response.isSuccessful) {
                    val userDataList = response.body()
                    if (userDataList != null) {
                        // Filter blank/null names
                        val filteredData = filterBlankNames(userDataList)

                        // Sort by listId and then by Name
                        dataList.addAll(filteredData.sortedWith(compareBy({ it.listId }, { it.name })))
                        setupListView()
                    }
                } else {
                    // Handle error
                    val errorBody = response.errorBody()
                    if (errorBody != null){
                        Log.e(tag, errorBody.string())
                    } else {
                        Log.e(tag, "Unknown Error")
                    }
                }
            }

            override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                // Handle failure
                Log.e(tag, "API call failed.", t)
            }
        })
    }

    fun filterBlankNames(userDataList: List<UserData>): List<UserData> {
        return userDataList.filter { it.name != null && it.name.isNotBlank() }
    }

    private fun setupListView() {
        adapter = ArrayAdapter(this, R.layout.list_item, R.id.textName, dataList.map {
            "List ID: ${it.listId}\nName: ${it.name}\nID: ${it.id}"
        })
        listView.adapter = adapter
    }
}