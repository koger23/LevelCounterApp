package com.kogero.levelcounter.model

data class Relationship (
    val relationshipId : Int,
    val userId : String,
    val relatingUserId: String,
    val state: String,
    val relationshipState: Int
)