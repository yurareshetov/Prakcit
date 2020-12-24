package com.example.locator3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.view.isVisible
import com.birjuvachhani.locus.Locus
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {



    private var ready = false

    // и счетчик
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cityName = intent.getStringExtra("city_name")

        //val mainIntent = Intent(this, MainActivity::class.java)

        //val cityName = "Moscow"

        //mainIntent.putExtra("city_name", cityName)

        //startActivity( mainIntent )

        //val newCityName = intent.getStringExtra("city_name")


selectCity.setOnClickListener {
    // при клике переходим на форму выбора города
    startActivity( Intent(this, CityListActivity::class.java) )
}


        Locus.getCurrentLocation(this) { result ->
            result.location?.let {
                tv.text = "${it.latitude}, ${it.longitude}"

                getWheather(it.longitude, it.latitude)
            } ?: run {
                tv.text = result.error?.message
            }
        }


        // в конструктор добавляем счечик (маскимальное время ожидания, периодические события)
        object : CountDownTimer(5000,1000){
            override fun onTick(millisUntilFinished: Long) {
                // если данные получены и прошло 3 сек, то скрываем splash screen и останавливаем счетчик
                counter++
                if(counter>3 && ready){
                    splash_screen.isVisible = false
                    this.cancel()
                }
            }

            override fun onFinish(){
                splash_screen.isVisible = false
            }
        }.start()

    }


    private val client = OkHttpClient()

    fun getWheather(lon: Double, lat: Double) {
        val token = "d4c9eea0d00fb43230b479793d6aa78f"
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&units=metric&appid=${token}"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                //setText( e.toString() )
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    // так можно достать заголовки http-ответа
                    //for ((name, value) in response.headers) {
                    //  println("$name: $value")
                    //}

                    //строку преобразуем в JSON-объект
                    var jsonObj = JSONObject(response.body!!.string())


                    // обращение к визуальному объекту из потока может вызвать исключение
                    // нужно присвоение делать в UI-потоке
                    setText( jsonObj )
                }
            }
        })
    }


    fun setText(t: JSONObject) {
        runOnUiThread {
            // достаем из ответа сервера название иконки погоды
                //var list = t.getJSONArray("list")
                val wheather = t.getJSONArray("weather")
                val icoName = wheather.getJSONObject(0).getString("icon")
                val icoUrl = "https://openweathermap.org/img/w/${icoName}.png"
                //      for(i in 0..list.length()-1) {
                //var item = list.getJSONObject(i)
                // аналогично достаньте значение температуры и выведите на экран

                // загружаем иконку и выводим ее на icon (ImageView)
                Glide.with(this).load(icoUrl).into(icon)

                ready = true
                //}
            }
        }

}