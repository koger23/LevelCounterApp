package com.kogero.levelcounter.model

class Relationship (
    val relationshipId : Int,
    val userId : String,
    val relatingUserId: String,
    val state: String,
    val relationshipState: Int
)