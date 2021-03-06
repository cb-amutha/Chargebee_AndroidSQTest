package com.chargebee.android.resources

import com.chargebee.android.*
import com.chargebee.android.models.Addon
import com.chargebee.android.repository.AddonRepository

internal class AddonResource: BaseResource(Chargebee.baseUrl) {

    suspend fun retrieve(addonId: String): CBResult<Addon> {
        val planResponse = apiClient.create(AddonRepository::class.java).retrieveAddon(addonId = addonId)
        val result = fromResponse(
            planResponse,
            ErrorDetail::class.java
        )
        return Success(result.getData().addon)
    }

}