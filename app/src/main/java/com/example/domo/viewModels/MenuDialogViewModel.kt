package com.example.domo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.models.interfaces.MenuDialogInterface
import com.example.domo.models.interfaces.MenuHolderStates
import entities.CategoryName
import entities.recyclerView.interfaces.BaseRecyclerViewItem

sealed class MenuDialogStates {
    object Default : MenuDialogStates()
    class MenuExists(
        val items: List<BaseRecyclerViewItem>,
    ) : MenuDialogStates()
}

class MenuDialogViewModel(
    private val model: MenuDialogInterface,
) : ViewModel() {
    private var _state: MutableLiveData<MenuDialogStates> =
        MutableLiveData(MenuDialogStates.Default)
    val state: LiveData<MenuDialogStates> = _state

    init {
        when (model.menuState.value) {
            is MenuHolderStates.MenuIsLoading ->
                model.menuState.observeForever {
                    when (it) {
                        MenuHolderStates.MenuExist -> {
                            _state.value = MenuDialogStates.MenuExists(getRecyclerViewItems())
                        }
                        else -> { }//Default state
                    }
                }
            is MenuHolderStates.MenuExist -> _state.value = MenuDialogStates.MenuExists(getRecyclerViewItems())
            else -> { } //TODO: Somehow remove the Default state
        }
    }

    private fun getRecyclerViewItems(): List<BaseRecyclerViewItem> =
        model.menu.map {
            listOf(CategoryName(it.name)) + it.dishes
        }.flatten()

}