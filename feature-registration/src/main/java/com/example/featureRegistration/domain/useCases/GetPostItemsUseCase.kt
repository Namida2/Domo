package com.example.domo.registration.domain

import android.view.View
import com.example.core.domain.tools.constants.EmployeePosts
import com.example.featureRegistration.data.PostItemsRepository
import com.example.featureRegistration.domain.PostItem
import javax.inject.Inject

class GetPostItemsUseCaseImpl @Inject constructor(
    private val repository: PostItemsRepository
) : GetPostItemsUseCase {
    override fun getPostItems(): MutableList<PostItem> =
        mutableListOf(
            PostItem(EmployeePosts.COOK, View.VISIBLE),
            PostItem(EmployeePosts.WAITER, View.INVISIBLE),
            PostItem(EmployeePosts.ADMINISTRATOR, View.INVISIBLE)
        )
}

interface GetPostItemsUseCase {
    fun getPostItems(): MutableList<PostItem>
}