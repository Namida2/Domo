package com.example.domo.splashScreen.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domo.splashScreen.domain.GetCurrentEmployeeUseCase
import com.example.domo.splashScreen.domain.ReadMenuUseCase
import com.example.waiter_core.domain.Employee
import com.example.waiter_core.domain.tools.ErrorMessage
import com.example.waiter_core.domain.tools.Task
import kotlinx.coroutines.launch
import com.example.waiter_core.domain.tools.ErrorMessages.permissionErrorMessage

sealed class SplashScreenStates {
    object DefaultState : SplashScreenStates()
    object CheckingForCurrentEmployee : SplashScreenStates()
    class EmployeeExists(
        var employee: Employee
        ) : SplashScreenStates()
    class PermissionError(
        var message: ErrorMessage = permissionErrorMessage
    ) : SplashScreenStates()
    object EmployeeDoesNotExit : SplashScreenStates()
}

class SplashScreenViewModel(
    readMenuUseCase: ReadMenuUseCase,
    private val getCurrentEmployeeUseCase: GetCurrentEmployeeUseCase
) : ViewModel() {
    private var _state: MutableLiveData<SplashScreenStates> =
        MutableLiveData(SplashScreenStates.DefaultState)
    val state: LiveData<SplashScreenStates> = _state

    init {
        readMenuUseCase.readMenu()
    }

    fun getCurrentEmployee() {
        viewModelScope.launch {
            _state.value = SplashScreenStates.CheckingForCurrentEmployee
            getCurrentEmployeeUseCase.getCurrentEmployee(object : Task<Employee, Unit> {
                override fun onSuccess(emplpoyee: Employee) {
                    if(emplpoyee.permission)
                        _state.value = SplashScreenStates.EmployeeExists(emplpoyee)
                    else _state.value = SplashScreenStates.PermissionError()
                }
                override fun onError(message: ErrorMessage?) {
                    _state.value = SplashScreenStates.EmployeeDoesNotExit
                }
            })
        }
    }
}