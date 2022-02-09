package di

import android.content.Context
import android.content.SharedPreferences
import com.example.domo.models.interfaces.*
import com.example.domo.splashScreen.domain.di.SplashScreenAppComponentDeps
import com.example.domo.views.activities.WaiterMainActivity
import com.example.domo.views.dialogs.MenuBottomSheetDialog
import com.example.domo.views.fragments.OrderFragment
import com.example.domo.views.fragments.TablesFragment
import com.example.waiterCore.data.database.Database
import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.order.OrderServiceSub
import dagger.BindsInstance
import dagger.Component
import di.modules.FirebaseModule
import di.modules.LocalRepositoryModule
import di.modules.ModelsModule
import di.modules.RemoteRepositoriesModule
import javax.inject.Singleton

@Singleton
@Component(modules = [LocalRepositoryModule::class, FirebaseModule::class,
    ModelsModule::class, RemoteRepositoriesModule::class])
interface AppComponent : SplashScreenAppComponentDeps {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance database: Database,
            @BindsInstance sharedPreferences: SharedPreferences,
        ): AppComponent
    }

    fun provideSplashScreenModel(): SplashScreenModelInterface
    fun provideRegistrationModel(): RegistrationModelInterface
    fun provideLogInModel(): LogInModelInterface
    fun provideMenuDialogModel(): MenuDialogModelInterface
    fun provideOrderService(): OrdersService<OrderServiceSub>
    fun provideWaiterActOrderFragModel(): WaiterActOrderFragSharedViewModel
    fun provideOrderMenuDialogViewModel(): OrderMenuDialogModelInterface

    fun inject(activity: WaiterMainActivity)
    fun inject(fragment: TablesFragment)
    fun inject(fragment: MenuBottomSheetDialog)
    fun inject(fragment: OrderFragment)

}
