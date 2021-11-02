package com.example.domo.views

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.*
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import application.appComponent
import application.employee
import com.example.domo.R
import com.example.domo.databinding.ActivitySplashScreenBinding
import com.example.domo.viewModels.SplashScreenStates
import com.example.domo.viewModels.SplashScreenViewModel
import com.example.domo.viewModels.ViewModelFactory
import constants.EmployeePosts.ADMINISTRATOR
import constants.EmployeePosts.COOK
import constants.EmployeePosts.WAITER
import database.Database
import database.EmployeeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log2


class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel: SplashScreenViewModel by viewModels { ViewModelFactory(appComponent) }
    @Inject
    lateinit var database: Database
    @Inject
    lateinit var employeeDao: EmployeeDao

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)

        viewModel.getCurrentEmployee()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        viewModel.state.observe(this) { state ->
            when(state) {
                is SplashScreenStates.CheckingForCurrentEmployee -> {
                    //progressBar?
                }
                is SplashScreenStates.EmployeeDoesNotExit -> {
                    startActivity(Intent(this, AuthorizationActivity::class.java))
                }
                is SplashScreenStates.EmployeeExists -> {
                    employee = state.employee
                    when(employee?.post) {
                        COOK -> {
                            startActivity(Intent(this, WaiterMainActivity::class.java))
                        }
                        WAITER -> {
                            startActivity(Intent(this, WaiterMainActivity::class.java))
                        }
                        ADMINISTRATOR -> {
                            startActivity(Intent(this, WaiterMainActivity::class.java))
                        }
                    }

                }
                else -> {} //DefaultState
            }
        }
    }
}
fun Any.log(message: String) {
    Log.d("MyLogging", message)
}