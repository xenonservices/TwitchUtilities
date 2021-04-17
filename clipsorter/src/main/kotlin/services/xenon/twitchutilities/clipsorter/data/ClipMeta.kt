package services.xenon.twitchutilities.clipsorter.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class ClipMeta(
    val id: String,

    @SerializedName("game_id")
    val gameId: Int,

    @SerializedName("created_at")
    val createdAt: Date
)
