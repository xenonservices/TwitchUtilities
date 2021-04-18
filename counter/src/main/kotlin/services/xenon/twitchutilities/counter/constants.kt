package services.xenon.twitchutilities.counter

import com.google.gson.Gson
import com.google.gson.GsonBuilder

val GSON: Gson = GsonBuilder()
    .setPrettyPrinting()
    .create()