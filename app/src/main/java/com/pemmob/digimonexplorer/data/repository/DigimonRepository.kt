package com.pemmob.digimonexplorer.data.repository

import com.pemmob.digimonexplorer.data.model.Digimon
import com.pemmob.digimonexplorer.data.model.toDigimon
import com.pemmob.digimonexplorer.network.ApiClient
import com.pemmob.digimonexplorer.network.ApiInterface
import com.pemmob.digimonexplorer.util.DigimonConstants
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class DigimonRepository(private val api: ApiInterface = ApiClient.instance) {

    suspend fun getDigimonList(
        page: Int = 0,
        pageSize: Int = DigimonConstants.PAGE_SIZE
    ): List<Digimon> = coroutineScope {
        val items = api.getDigimonList(page, pageSize).content.orEmpty()
        items.mapNotNull { it.id }
            .map { id -> async { runCatching { api.getDigimonDetail(id).toDigimon() }.getOrNull() } }
            .awaitAll()
            .filterNotNull()
    }

    suspend fun getDigimonDetail(id: Int): Digimon =
        api.getDigimonDetail(id).toDigimon()
}
