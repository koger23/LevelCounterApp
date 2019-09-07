package com.kogero.levelcounter.helpers

import android.util.Base64
import android.util.Base64.URL_SAFE
import com.auth0.android.jwt.JWT
import java.io.UnsupportedEncodingException


class JWTUtils {

    @Throws(Exception::class)
    fun decode(JWTEncoded: String): String? {
        val parsedJWT = JWT(JWTEncoded)
        val subscriptionMetaData =
            parsedJWT.getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier")
        return subscriptionMetaData.asString()
    }

    @Throws(UnsupportedEncodingException::class)
    private fun getJson(strEncoded: String): String {
        val decodedBytes = Base64.decode(strEncoded, URL_SAFE)
        return String(decodedBytes)
    }
}