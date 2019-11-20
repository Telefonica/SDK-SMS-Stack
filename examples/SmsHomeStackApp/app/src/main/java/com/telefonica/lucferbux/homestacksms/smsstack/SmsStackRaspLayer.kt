package com.telefonica.lucferbux.homestacksms.smsstack

import com.example.smstcplibrary.SMSTCPAppLayer
import com.example.smstcplibrary.SMSTCPLayer

import java.math.BigInteger

class SmsStackRaspLayer : SMSTCPAppLayer {

    companion object {
        val HEADER_SIZE = 6
        val HEADER_SIZE_BIN = HEADER_SIZE * 4
    }

    var idOrigin = 0b0
    var idTarget = 0b0
    var mod = 0b0
    var status = 0b0
    var data = ""

    constructor(idOrigin: Int, idTarget: Int, mod: Int, status: Int, data: String) {
        this.idOrigin = idOrigin
        this.idTarget = idTarget
        this.mod = mod
        this.status = status
        this.data = data
    }

    constructor(sms: String) {
        decodeSMSApp(sms)
    }

    override fun decodeSMSApp(s: String) {
        val headerMessage = SMSTCPLayer.fillHeader(hexToBinary(s.substring(0, HEADER_SIZE)), HEADER_SIZE_BIN)
        val dataMessage = s.substring(HEADER_SIZE, s.length)
        this.idOrigin = Integer.parseInt(headerMessage.substring(0, 8), 2)
        this.idTarget = Integer.parseInt(headerMessage.substring(8, 16), 2)
        this.mod = Integer.parseInt(headerMessage.substring(16, 21), 2)
        this.data = dataMessage
    }

    override fun encodeSMSApp(): String {
        val idOriginMessage = SMSTCPLayer.fillHeader(this.idOrigin, 8)
        val idTargetMessage = SMSTCPLayer.fillHeader(this.idTarget, 8)
        val modMessage = SMSTCPLayer.fillHeader(this.mod, 5)
        val statusMessage = SMSTCPLayer.fillHeader(this.status, 3)

        val headersBinary = idOriginMessage + idTargetMessage + modMessage + statusMessage
        val headersHexa =
            SMSTCPLayer.fillHeader(java.lang.Long.toHexString(java.lang.Long.parseLong(headersBinary, 2)), HEADER_SIZE)

        return headersHexa + this.data
    }

    override fun hexToBinary(s: String): String {
        return BigInteger(s, 16).toString(2)
    }

}
