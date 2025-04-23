// Source.kt
package com.example.thenewsapplication.models

import androidx.room.Embedded
import java.io.Serializable

data class Source(
    val id: String?,
    val name: String
) : Serializable