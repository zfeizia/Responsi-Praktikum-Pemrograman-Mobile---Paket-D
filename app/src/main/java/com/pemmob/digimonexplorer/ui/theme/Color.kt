package com.pemmob.digimonexplorer.ui.theme

import androidx.compose.ui.graphics.Color

val DigiOrange = Color(0xFFF57C00)
val DigiOrangeLight = Color(0xFFFFB74D)
val DigiBlue = Color(0xFF1565C0)
val DigiBlueLight = Color(0xFF90CAF9)
val DigiBackground = Color(0xFFFFF8F1)
val DigiSurface = Color(0xFFFFFFFF)
val DigiDarkBackground = Color(0xFF14110F)
val DigiDarkSurface = Color(0xFF241F1B)

// Warna kartu/header berdasarkan attribute Digimon
val AttrVaccine = Color(0xFF43A047)
val AttrData = Color(0xFF1E88E5)
val AttrVirus = Color(0xFF8E24AA)
val AttrFree = Color(0xFFF57C00)
val AttrVariable = Color(0xFF00897B)
val AttrOther = Color(0xFF607D8B)

fun attributeColor(attribute: String): Color = when {
    attribute.contains("Vaccine", ignoreCase = true) -> AttrVaccine
    attribute.contains("Data", ignoreCase = true) -> AttrData
    attribute.contains("Virus", ignoreCase = true) -> AttrVirus
    attribute.contains("Free", ignoreCase = true) -> AttrFree
    attribute.contains("Variable", ignoreCase = true) -> AttrVariable
    else -> AttrOther
}