package com.audiobookz.nz.app.ui

import android.content.Context
import com.audiobookz.nz.app.R
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider

class CastOptionProvider : OptionsProvider {
    override fun getCastOptions(ctx: Context?): CastOptions {
            return CastOptions
                .Builder()
                .setReceiverApplicationId(
                    ctx!!.getString(R.string.receiver_id)
                )
                .build()
    }

    override fun getAdditionalSessionProviders(ctx: Context?): MutableList<SessionProvider>? {
        return null
    }
}