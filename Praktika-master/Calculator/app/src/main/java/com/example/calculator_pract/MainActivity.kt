package com.example.calculator_pract

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    var value1: Float = 0.0f
    var value2: Float = 0.0f
    var val_proc: Int=0
    var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_one.setOnClickListener {
            if (textView.text == "0"){
                textView.setText("")
                var tmp = textView.text.toString()
                textView.text = tmp+'1'
            }
            else{
                var tmp = textView.text.toString()
                textView.text = tmp+'1'
            }
        }

        bnt_two.setOnClickListener {
            if (textView.text == "0"){
                textView.setText("")
                var tmp = textView.text.toString()
                textView.text = tmp+'2'
            }
            else {
                var tmp = textView.text.toString()
                textView.text = tmp + '2'
            }
        }
        btn_three.setOnClickListener {
            if (textView.text == "0"){
                textView.setText("")
                var tmp = textView.text.toString()
                textView.text = tmp+'2'
            }
            else {
                var tmp = textView.text.toString()
                textView.text = tmp + '3'
            }
        }
        btn_four.setOnClickListener {
            if (textView.text == "0"){
                textView.setText("")
                var tmp = textView.text.toString()
                textView.text = tmp+'4'
            }
            else {
                var tmp = textView.text.toString()
                textView.text = tmp + '4'
            }
        }
        btn_five.setOnClickListener {
            if (textView.text == "0"){
                textView.setText("")
                var tmp = textView.text.toString()
                textView.text = tmp+'5'
            }
            else {
                var tmp = textView.text.toString()
                textView.text = tmp + '5'
            }
        }
        bnt_six.setOnClickListener {
            if (textView.text == "0"){
                textView.setText("")
                var tmp = textView.text.toString()
                textView.text = tmp+'6'
            }
            else {
                var tmp = textView.text.toString()
                textView.text = tmp + '6'
            }
        }
        btn_seven.setOnClickListener {
            if (textView.text == "0"){
                textView.setText("")
                var tmp = textView.text.toString()
                textView.text = tmp+'7'
            }
            else {
                var tmp = textView.text.toString()
                textView.text = tmp + '7'
            }
        }
        btn_eight.setOnClickListener {
            if (textView.text == "0"){
                textView.setText("")
                var tmp = textView.text.toString()
                textView.text = tmp+'8'
            }
            else {
                var tmp = textView.text.toString()
                textView.text = tmp + '8'
            }
        }
        btn_nine.setOnClickListener {
            if (textView.text == "0"){
                textView.setText("")
                var tmp = textView.text.toString()
                textView.text = tmp+'9'
            }
            else {
                var tmp = textView.text.toString()
                textView.text = tmp + '9'
            }
        }
        btn_zero.setOnClickListener {
            var tmp = textView.text.toString()
            textView.text = tmp+'0'
        }
        btn_point.setOnClickListener {
            var tmp = textView.text.toString()
            textView.text = tmp+'.'
        }

        btn_AC.setOnClickListener {
            if(textView.text.length>0)
                textView.text = textView.text.take(textView.text.length - 1)
            if (textView.text == "")
                textView.setText("0")
        }

        btn_procent.setOnClickListener{
            val_proc = (value1/100).toInt()
        }

        btn_bool.setOnClickListener{
            var i:Int = 0
            var j:Int = 0
            for (char in textView.text){
                i+=1
            }
            j=i+1
            textView.text = textView.text.padStart(j,'-')
        }

        btn_plus.setOnClickListener {
            var tmp = textView.text.toString()
            value1 = tmp.toFloat()
            textView.setText("0")
            count = 1
        }
        btn_minus.setOnClickListener {
            var tmp = textView.text.toString()
            value1 = tmp.toFloat()
            textView.setText("0")
            count = 2
        }
        btn_umn.setOnClickListener {
            var tmp = textView.text.toString()
            value1 = tmp.toFloat()
            textView.setText("0")
            count = 3
        }
        btn_div.setOnClickListener {
            var tmp = textView.text.toString()
            value1 = tmp.toFloat()
            textView.setText("0")
            count = 4
        }

        btn_answer.setOnClickListener{
            var tmp = textView.text.toString()
            value2 = tmp.toFloat()
            if (val_proc>0){
                value2 *= val_proc
            }

            if (count == 1){
              value2 += value1
                value1 = 0F
            }
            if (count == 2){
                value2 = value1-value2
                value1 = 0F
            }
            if (count == 3){
                value2 *= value1
                value1 = 1F
            }
            if (count == 4){
                value2 = value1/value2
                value1 = 1F
            }
            textView.text = value2.toString()
        }
    }
}