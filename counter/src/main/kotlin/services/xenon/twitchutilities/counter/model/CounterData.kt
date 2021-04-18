package services.xenon.twitchutilities.counter.model

data class CounterData(
    val id: String,
    val targetFile: String,
    val redemptions: List<RedemptionData>
)

data class RedemptionData(
    val redemptionTitle: String,
    val additionalTime: Long
)


/**
{
    "id": "no cursing",
    "targetFile": "D:/Organizations/Xenon/Other/Xenon-TwitchUtilities/test.txt"
    "redemptions": [
        {
            "redemptionTitle": "No Cursing!",
            "additionalTime": 300000
        }
    ]
}
 */