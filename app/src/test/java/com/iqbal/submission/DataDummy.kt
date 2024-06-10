package com.iqbal.submission

import com.iqbal.submission.data.api.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "photo_url",
                "created_at",
                "name $i",
                "description $i",
                1.0,
                2.0,
            )
            items.add(story)
        }
        return items
    }
}