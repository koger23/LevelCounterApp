package com.kogero.levelcounter.models

data class Relationship (
    val relationshipId : Int,
    val userId : String,
    val relatingUserId: String,
    val state: String,
    val relationshipState: Int
)