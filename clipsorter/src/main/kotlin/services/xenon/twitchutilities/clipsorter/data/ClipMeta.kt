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

/**
{
"id":"AcceptableTenaciousHerringTinyFace-CtwpAEittDrP7APJ",
"url":"https://clips.twitch.tv/AcceptableTenaciousHerringTinyFace-CtwpAEittDrP7APJ",
"embed_url":"https://clips.twitch.tv/embed?clip=AcceptableTenaciousHerringTinyFace-CtwpAEittDrP7APJ",
"broadcaster_id":"83711921",
"broadcaster_name":"teh_neon",
"creator_id":"83711921",
"creator_name":"teh_neon",
"video_id":"968149379",
"game_id":"27471",
"language":"en",
"title":"player vs playering mineman",
"view_count":1,
"created_at":"2021-03-30T02:56:49Z",
"thumbnail_url":"https://clips-media-assets2.twitch.tv/41592087484-offset-16074-preview-480x272.jpg"
}
 */