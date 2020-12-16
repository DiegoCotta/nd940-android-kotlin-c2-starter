package com.udacity.asteroidradar.api

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.domain.ImageOfDay

@JsonClass(generateAdapter = true)
data class NetworkImageOfDay(
    val copyright: String = "",
    val date: String = "",
    val explanation: String= "",
    val hdurl: String= "",
    val media_type: String= "",
    val service_version: String= "",
    val title: String= "",
    val url: String
)

fun NetworkImageOfDay.asDomainModel(): ImageOfDay {
    return ImageOfDay(
        copyright = this.copyright,
        date = this.date,
        explanation = this.explanation,
        hdurl = this.hdurl,
        media_type = this.media_type,
        service_version = this.service_version,
        title = this.title,
        url = this.url
    )
}

