package pl.michalkowol

import com.google.inject.Guice
import com.google.inject.Injector

object Configuration {

    val injector = createInjector()

    private fun createInjector(): Injector {
        return Guice.createInjector(
            Modules()
        )
    }

}
