package com.example.waiterMain.domain.di

import android.content.Context
import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.order.OrdersServiceSub
import com.example.waiterMain.domain.NewOrdersWorker
import com.example.waiterMain.domain.di.modules.RemoteRepositoryModule
import com.example.waiterMain.domain.di.modules.UseCasesModule
import com.example.waiterMain.domain.useCases.ReadNewOrderUseCase
import dagger.Component

@Component(dependencies = [WaiterMainDeps::class], modules = [UseCasesModule::class, RemoteRepositoryModule::class])
interface WaiterMainAppComponent {
    @Component.Builder
    interface Builder {
        fun provideWaiterMainDeps(deps: WaiterMainDeps): Builder
        fun build(): WaiterMainAppComponent
    }

    fun provideReadNewOrderUseCase(): ReadNewOrderUseCase
}

interface WaiterMainDeps {
    val context: Context
    val ordersService: OrdersService<OrdersServiceSub>
}
