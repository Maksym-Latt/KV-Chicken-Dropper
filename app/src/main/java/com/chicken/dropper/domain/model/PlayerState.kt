package com.chicken.dropper.domain.model

data class PlayerState(
    val eggs: Int = 0,
    val selectedSkinId: String = "classic",
    val ownedSkins: Set<String> = setOf("classic")
)
