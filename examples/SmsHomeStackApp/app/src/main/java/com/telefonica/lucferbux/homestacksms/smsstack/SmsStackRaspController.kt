package com.telefonica.lucferbux.homestacksms.smsstack

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.smstcplibrary.SMSTCPController
import com.example.smstcplibrary.SMSTCPLayer
import java.util.ArrayList

class SmsStackRaspController (appCompatActivity: AppCompatActivity, val messageReceive: (SmsStackRaspLayer) -> Unit) {

    companion object {
        // Phone numbers to send the message
        val PHONE_NUMBERS = arrayOf("34646980422")
    }

    // Instantiation of the controller with AES CBC and Cipher key
    val smsStackController = SMSTCPController(appCompatActivity, 1 , "PATATA")

    init {
        val completionHandler = object: SMSTCPController.CompletionHandler {
            override fun handleMessageSent() {

            }

            // Methods handling the messages
            override fun handleFinalMessageReceived(p0: ArrayList<String>?, p1: Array<out String>?) {
                val smsAppLayer = p0?.let {
                    SmsStackRaspLayer(smsStackController.processMessages(p0))
                } ?: run {
                    return
                }

                messageReceive(smsAppLayer)
                Log.d("SMSSent", smsAppLayer.toString())
            }

            override fun handleFinalMessageSent() {

            }

            override fun handleMessageReceived(p0: SMSTCPLayer?, p1: Array<out String>?) {

            }

        }

        smsStackController.setCompletionHandler(completionHandler)
    }

    /**
     * Send Message to the device
     *
     * @param device Device to target
     */
    fun sendMessage(device: Int) {
        val smsLayer = SmsStackRaspLayer(100, device, 0, 0, "")
        smsStackController.sendMessage(smsLayer.encodeSMSApp(), PHONE_NUMBERS)
    }


}