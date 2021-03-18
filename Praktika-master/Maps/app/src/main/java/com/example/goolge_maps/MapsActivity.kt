
package com.example.goolge_maps


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.birjuvachhani.locus.Locus
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso.get
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.custom_infowindow.view.*
import org.json.JSONObject


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var marker_list = arrayListOf<Marker>()

    private var lat: Double = 0.0
    private var lon: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker): Boolean {
                if(marker.tag==0) {
                    marker.tag = 1
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                } else {
                    marker.tag = 0
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                }

                // в этой функции показываем/скрываем кнопку Расчитать маршрут
                checkMarkers()

                return false
            }
        })


         Locus.getCurrentLocation(this) { result ->
            result.location?.let {
                lat = it.latitude
                lon = it.longitude

                // тут можно вызвать функцию отображения текущей геолокации

            } ?: run {
                //error = "${error}${result.error?.message}\n"
            }
        }
    }

    private fun checkMarkers(){
        for (marker in marker_list){
            if (marker.tag==1){
                btn_route.isVisible = true
                return
            }
        }
        btn_route.isVisible = false
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        mMap.setInfoWindowAdapter( CustomInfoWindowAdapter() )


        // координаты техникума
        val yotc = LatLng(56.639439, 47.892384)

        // к метке добавлена подпись
        mMap.addMarker(MarkerOptions().position(yotc).title("Метка ЙОТК")
                .snippet("Йошкар-Олинский Технологический Коледж"))

        // используем камеру с масштабированием
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yotc, 16F))

        Fuel.post("http://192.168.0.166:8080/points",
                listOf("login" to "KNovoselov", "password" to "13579"))
                .responseString { request, response, result ->

                    // тут разбираем ответ сервера

                }
    }


    private var mymarker_list =  arrayListOf<MyMarker>()

    internal inner class MyMarker {
        var marker: Marker
        var url: String
        var description: String
        var downloaded = false

        constructor(_marker: Marker, _url: String, _desc: String){
            marker = _marker
            url = _url
            description = _desc
        }
    }


    private fun getPoints() {
        Fuel.post("http://192.168.0.166:8080/points",
                listOf("login" to "KNovoselov", "password" to "13579"))
                .responseString { request, response, result ->
                    when (result) {
                        is Result.Failure ->
                            Toast.makeText(applicationContext,
                                    "Get points failure: ${result.getException()}",
                                    Toast.LENGTH_LONG).show()
                        is Result.Success ->
                            try {
                                val jsonResp = JSONObject(result.get())
                                if (jsonResp.has("status") && jsonResp.getString("status") == "OK") {
                                    // добавляем точки на карту
                                    val points = jsonResp.getJSONArray("points")
                                    for (i in 0 until points.length()) {
                                        val point = points.getJSONObject(i)

                                        val coord = LatLng(point.getDouble("lat"), point.getDouble("lon"))

                                        val marker = MarkerOptions()
                                                .position(coord)
                                                .title(point.getString("short"))
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

                                        marker_list.add(mMap.addMarker(marker))


                                        val mymarker = MyMarker(mMap.addMarker(marker) ,
                                                point.getString("img"),
                                                point.getString("description"))

                                        mymarker_list.add( mymarker )

                                    }
                                } else
                                    throw Exception("Не верный формат ответа сервера или ошибка")
                            } catch (e: Exception) {
                                Toast.makeText(applicationContext,
                                        "Get points failure: ${e.message}",
                                        Toast.LENGTH_LONG).show()
                            }
                    }
                }
        }


    internal inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {
        fun getImgUrl4Marker(marker: Marker): Triple<String?,String?,Boolean?>{
            for (i in 0 until mymarker_list.size){
                if(mymarker_list[i].marker==marker)
                    return Triple("$/img/${mymarker_list[i].url}",
                            mymarker_list[i].description,
                            mymarker_list[i].downloaded)
            }
            return Triple(null,null,null)
        }

        // абстрактная функция класса InfoWindowAdapter - должна быть реализована в потомках
        override fun getInfoWindow(marker: Marker): View? {
            //тут менять только если меняется форма окна (круглое или облачко...)
            return null
        }

        // в этой функции реализуется отрисовка контента, т.е. нашей формы
        override fun getInfoContents(marker: Marker): View? {
            // извлекаем кастомный layout
            val inflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.custom_infowindow, null)

            // получаем урл и описание маркера
            //val (imgUrl, desc) = getImgUrl4Marker(marker)

            val (imgUrl, desc, downloaded) = getImgUrl4Marker(marker)

            if(imgUrl!=null) {
                // Picasso кэширует картинки, если картинка уже была загружена, то callback не нужен
                if (downloaded!!) {
                    get().load(imgUrl).into(view.image)
                } else {
                    // если новая картинка, то загружаем картинку с callback функцией
                    get().load(imgUrl).into(view.image, InfoWindowRefresher(marker))
                }
            }
            return view
        }


        internal inner class InfoWindowRefresher : Callback {
            private lateinit var markerToRefresh: Marker

            // в конструкторе запоминаем маркер
            constructor(marker: Marker) {
                markerToRefresh = marker
            }

            override fun onError(e: Exception?) {}

            // по готовности ресурса перерисовываем информационное окно маркера
            override fun onSuccess() {
                for (i in 0 until mymarker_list.size){
                    if(mymarker_list[i].marker==markerToRefresh){
                        mymarker_list[i].downloaded = true
                        break
                    }
                }
                markerToRefresh.showInfoWindow()
            }
        }


    }


    }