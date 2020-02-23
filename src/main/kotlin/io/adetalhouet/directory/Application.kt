/*
 * Copyright (C) 2019 Alexis de Talhouët
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.adetalhouet.directory

import com.fasterxml.jackson.databind.SerializationFeature
import io.adetalhouet.directory.di.directoryModule
import io.adetalhouet.directory.rest.person
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.util.KtorExperimentalAPI
import org.koin.ktor.ext.Koin
import org.slf4j.event.Level

@KtorExperimentalAPI
fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging) {
        level = Level.DEBUG
    }
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(Routing) {
        person()
    }
    install(Koin) {
        modules(directoryModule)
        createEagerInstances()
    }
}
