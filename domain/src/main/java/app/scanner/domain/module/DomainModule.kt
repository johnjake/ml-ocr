package app.scanner.domain.module

import app.scanner.domain.repository.ReaderAction
import app.scanner.domain.repository.ReaderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    @ViewModelScoped
    fun providesOcrRepository(): ReaderAction = ReaderRepository()
}
