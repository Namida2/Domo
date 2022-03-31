package com.example.featureSplashScreen.data

import com.example.core.data.database.daos.MenuDao
import com.example.core.domain.entities.menu.Category
import com.example.core.domain.entities.menu.Dish
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.tools.extensions.logD
import com.example.featureSplashScreen.domain.repositories.MenuLocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MenuLocalRepositoryImpl @Inject constructor(
    private val menuDao: MenuDao
) : MenuLocalRepository {

    private var menu: ArrayList<Category> = ArrayList()

    override fun readExitingMenu() {
        CoroutineScope(IO).launch {
            val categories: MutableSet<String> = mutableSetOf()
            val allDishes = menuDao.readAll()
            allDishes.forEach {
                categories.add(it.categoryName)
            }
            categories.forEach { category ->
                val dishes = allDishes.filter { it.categoryName == category } as MutableList
                menu.add(Category(category, dishes))
            }
            withContext(Dispatchers.Main) {
                MenuService.setNewMenu(menu)
            }
        }
    }

    override fun insertCurrentMenu() {
        logD(MenuService.menu.toString())
        val dishes = getAllDishes(MenuService.menu)
        dishes.toString()
        CoroutineScope(IO).launch {
            menuDao.deleteAll()
            menuDao.insert(dishes)
            val list = menuDao.readAll()
            logD(list.toString())
        }
    }

    private fun getAllDishes(menu: MutableList<Category>): List<Dish> =
        menu.map { it.dishes }.flatten()

}

