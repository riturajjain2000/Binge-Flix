package com.skf.bingeflix.Models

class Movie {
    var title: String
    var description: String? = null
    var thumbnail: Int
    var studio: String? = null
    var rating: String? = null
    var streamingLink: String? = null
    var coverPhoto = 0

    constructor(title: String, thumbnail: Int, coverPhoto: Int) {
        this.title = title
        this.thumbnail = thumbnail
        this.coverPhoto = coverPhoto
    }

    constructor(title: String, thumbnail: Int) {
        this.title = title
        this.thumbnail = thumbnail
    }

    constructor(title: String, description: String?, thumbnail: Int, studio: String?, rating: String?, streamingLink: String?) {
        this.title = title
        this.description = description
        this.thumbnail = thumbnail
        this.studio = studio
        this.rating = rating
        this.streamingLink = streamingLink
    }
}