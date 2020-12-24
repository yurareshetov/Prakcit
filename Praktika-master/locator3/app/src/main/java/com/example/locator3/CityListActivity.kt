package com.example.locator3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_city_list.*

class CityListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)

        cityList.adapter = ArrayAdapter(
            this,
            R.layout.city_list_item, names
        )
        cityList.setOnItemClickListener { parent, view, position, id ->
            val mainIntent = Intent(this, MainActivity::class.java)
            val cityName = names[id.toInt()]

            // запоминаем выбранное название города
            mainIntent.putExtra("city_name", cityName)

            // возвращаемся на основной экран (Activity)
            startActivity( mainIntent )
        }
    }
    private var names = arrayOf(
        "Moscow",
        "Yoshkar-Ola",
        "Kazan"
    )

}