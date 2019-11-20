package com.telefonica.lucferbux.homestacksms

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.telefonica.lucferbux.homestacksms.smsstack.SmsStackRaspController
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private lateinit var smsStackController: SmsStackRaspController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        smsStackController = SmsStackRaspController(this) {
            toast("Action performed")
        }

        bt_yellow_led.onClick {
            changeButton(16)
        }

        bt_green_led.onClick {
            changeButton(18)
        }

        bt_red_led.onClick {
            changeButton(17)
        }
    }

    fun changeButton(port: Int) {
        smsStackController.sendMessage(port)

        when(port) {
            16 -> {
                toast("Message sent to Yellow led")
                pb_yellow.visibility = View.VISIBLE
                bt_yellow_led.isEnabled = false
                Handler().postDelayed(
                    {
                        changeStatusText(port)
                    },
                    9000 // value in milliseconds
                )
            }

            17 -> {
                toast("Message sent to Red led")
                pb_red.visibility = View.VISIBLE
                bt_red_led.isEnabled = false
                Handler().postDelayed(
                    {
                        changeStatusText(port)
                    },
                    9000 // value in milliseconds
                )
            }

            18 -> {
                toast("Message sent to Green led")
                pb_green.visibility = View.VISIBLE
                bt_green_led.isEnabled = false
                Handler().postDelayed(
                    {
                        changeStatusText(port)
                    },
                    9000 // value in milliseconds
                )
            }

            else -> {

            }
        }
    }

    fun changeStatusText(port: Int) {


        when(port) {
            16 -> {
                tv_yellow.text = if(tv_yellow.text.equals("ON")) "OFF" else "ON"
                pb_yellow.visibility = View.INVISIBLE
                bt_yellow_led.isEnabled = true
            }

            17 -> {
                tv_red.text = if(tv_red.text.equals("ON")) "OFF" else "ON"
                pb_red.visibility = View.INVISIBLE
                bt_red_led.isEnabled = true
            }

            18 -> {
                tv_green.text = if(tv_green.text.equals("ON")) "OFF" else "ON"
                pb_green.visibility = View.INVISIBLE
                bt_green_led.isEnabled = true
            }

        }
    }

    fun messageReceived(port: Int) {

    }

}
