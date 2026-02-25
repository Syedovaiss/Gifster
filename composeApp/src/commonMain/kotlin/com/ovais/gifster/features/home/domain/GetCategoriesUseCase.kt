package com.ovais.gifster.features.home.domain

import com.ovais.gifster.core.data.http.Category
import com.ovais.gifster.core.data.network.DataError
import com.ovais.gifster.core.data.network.Result
import com.ovais.gifster.core.data.network.map
import com.ovais.gifster.utils.SuspendUseCase

fun interface GetCategoriesUseCase : SuspendUseCase<Result<List<Category>, DataError>>

class DefaultGetCategoriesUseCase(
    private val homeRepository: HomeRepository
) : GetCategoriesUseCase {
    override suspend fun invoke(): Result<List<Category>, DataError> {
        return homeRepository.getCategories().map { it.categories }
    }
}