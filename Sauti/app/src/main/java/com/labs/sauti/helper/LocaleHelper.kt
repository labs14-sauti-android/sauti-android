package com.labs.sauti.helper

import android.content.Context

class LocaleHelper {

    companion object {

        fun createResourcesContext(context: Context): Context {
            // TODO version

            return context.createConfigurationContext(context.resources.configuration)
        }
    }
}