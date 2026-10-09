package com.pemmob.digimonexplorer.data.model

data class Digimon(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val level: String,
    val attribute: String,
    val type: String,
    val releaseDate: String?,
    val description: String?,
    val skills: List<DigimonSkill>
)

data class DigimonSkill(val name: String, val description: String?)

private fun <T> List<T>?.joinOrDash(transform: (T) -> String?): String =
    this?.mapNotNull(transform)?.joinToString(", ")?.ifBlank { null } ?: "-"

fun DigimonDetailDto.toDigimon(): Digimon = Digimon(
    id = id ?: 0,
    name = name ?: "Unknown",
    imageUrl = images?.firstOrNull()?.href,
    level = levels.joinOrDash { it.level },
    attribute = attributes.joinOrDash { it.attribute },
    type = types.joinOrDash { it.type },
    releaseDate = releaseDate,
    description = descriptions?.firstOrNull { it.language == "en_us" }?.description
        ?: descriptions?.firstOrNull()?.description,
    skills = skills?.mapNotNull { s -> s.skill?.let { DigimonSkill(it, s.description) } }.orEmpty()
)
