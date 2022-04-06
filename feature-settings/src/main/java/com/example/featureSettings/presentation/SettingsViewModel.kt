package com.example.featureSettings.presentation

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.Settings
import com.example.core.domain.entities.menu.Category
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.menu.isTheSameMenu
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.Event
import com.example.core.domain.entities.tools.NetworkConnectionListener.networkConnectionChanges
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.constants.FirestoreReferences.actualMenuCollectionRef
import com.example.core.domain.entities.tools.constants.FirestoreReferences.defaultMenuCollectionRef
import com.example.core.domain.entities.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.entities.tools.extensions.isEmptyField
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.interfaces.TerminatingState
import com.example.core.domain.useCases.ReadMenuUseCase
import com.example.featureMenuDialog.domain.interfaces.OnDismissListener
import com.example.featureSettings.domain.useCases.SaveMenuUseCase
import com.example.featureSettings.domain.useCases.SaveSettingsUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//TODO: Save current menu as default menu //STOPPED//
sealed class SettingsVMStates {
    object Default : SettingsVMStates()
    object InProcess : SettingsVMStates()
    object OnSuccess : SettingsVMStates(), TerminatingState
    class OnFailure(val message: ErrorMessage) : SettingsVMStates(), TerminatingState
}

class SettingsViewModel(
    private val setting: Settings,
    private val saveMenuUseCase: SaveMenuUseCase,
    private val readMenuUseCase: ReadMenuUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase,
) : ViewModel(), OnDismissListener, Stateful<SettingsVMStates> {

    private val _state = MutableLiveData<SettingsVMStates>()
    val state: LiveData<SettingsVMStates> = _state
    private lateinit var lastSavedMenu: MutableList<Category>
    private val _onMenuDialogDismissEvent = MutableLiveData<Event<Unit>>()
    val onMenuDialogDismissEvent: LiveData<Event<Unit>> = _onMenuDialogDismissEvent
    private val _onSettingChangedEvent = MutableLiveData<Event<Boolean>>()
    val onSettingChangedEvent: LiveData<Event<Boolean>> = _onSettingChangedEvent

    private val taskForSavingMenu = object : SimpleTask {
        override fun onSuccess(result: Unit) {
            setNewState(SettingsVMStates.OnSuccess)
        }
        override fun onError(message: ErrorMessage?) {
            MenuService.setNewMenu(lastSavedMenu)
            setNewState(SettingsVMStates.OnFailure(message ?: defaultErrorMessage))
        }
    }
    private val simpleTask = object : SimpleTask {
        override fun onSuccess(result: Unit) {
            setNewState(SettingsVMStates.OnSuccess)
        }
        override fun onError(message: ErrorMessage?) {
            setNewState(SettingsVMStates.OnFailure(message ?: defaultErrorMessage))
        }
    }

    init {
        viewModelScope.launch {
            networkConnectionChanges.collect { isConnected ->
                if(isConnected) return@collect
                if(state.value == SettingsVMStates.InProcess)
                    MenuService.setNewMenu(lastSavedMenu)
                setNewState(SettingsVMStates.OnFailure(checkNetworkConnectionMessage))
            }
        }
    }

    override fun onDismiss() {
        if (MenuService.menu isTheSameMenu lastSavedMenu) return
        _onMenuDialogDismissEvent.value = Event(Unit)
    }

    fun onAcceptNewMenu() {
        _state.value = SettingsVMStates.InProcess
        saveMenuUseCase.saveNewMenu(actualMenuCollectionRef, taskForSavingMenu)
    }

    fun onCancelNewMenu() {
        MenuService.menu = lastSavedMenu
        MenuService.setNewMenu(lastSavedMenu)
    }

    fun saveMenuBeforeChanges() {
        lastSavedMenu = MenuService.copyMenu()
    }

    fun readDefaultMeu() {
        setNewState(SettingsVMStates.InProcess)
        readMenuUseCase.readMenu(defaultMenuCollectionRef, true, simpleTask)
    }

    fun saveNewMenuFromMenuBottomSheetDialog() {
        _state.value = SettingsVMStates.InProcess
        lastSavedMenu = MenuService.copyMenu()
        saveMenuUseCase.saveNewMenu(actualMenuCollectionRef, taskForSavingMenu)
    }

    fun onSaveSettingButtonClick(maxTablesCount: String, maxGuestsCount: String) {
        if(isEmptyField(maxTablesCount, maxGuestsCount)) return
        _state.value = SettingsVMStates.InProcess
        saveSettingsUseCase.saveNewSettings(maxTablesCount.toInt(), maxGuestsCount.toInt(), object : SimpleTask {
            override fun onSuccess(result: Unit) {
                onMaxTablesCountChanged(setting.tablesCount.toString())
                onMaxGuestCountChanged(setting.guestsCount.toString())
                setNewState(SettingsVMStates.OnSuccess)
            }
            override fun onError(message: ErrorMessage?) {
                setNewState(SettingsVMStates.OnFailure(message ?: defaultErrorMessage))
            }
        })
    }

    fun saveCurrentMeuAsDefault() {
        _state.value = SettingsVMStates.InProcess
        saveMenuUseCase.saveNewMenu(defaultMenuCollectionRef, taskForSavingMenu)
    }
    
    fun onMaxTablesCountChanged(text: String) {
        if(isEmptyField(text)) return
        val newTablesCount = text.toInt()
        if(setting.tablesCount == newTablesCount)
            _onSettingChangedEvent.value = Event(false)
        else  _onSettingChangedEvent.value = Event(true)
    }

    fun onMaxGuestCountChanged(text: String) {
        if(isEmptyField(text)) return
        val newGuestCount = text.toInt()
        if(setting.guestsCount == newGuestCount)
            _onSettingChangedEvent.value = Event(false)
        else  _onSettingChangedEvent.value = Event(true)
    }

    override fun setNewState(state: SettingsVMStates) {
        _state.value = state
        if(state is TerminatingState)
            _state.value = SettingsVMStates.Default
    }

}