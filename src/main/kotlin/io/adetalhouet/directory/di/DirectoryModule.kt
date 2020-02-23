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
package io.adetalhouet.directory.di

import io.adetalhouet.directory.db.DatabaseConfiguration
import io.adetalhouet.directory.db.service.DirectoryService
import io.adetalhouet.directory.db.service.DirectoryServiceImpl
import io.ktor.util.KtorExperimentalAPI
import org.koin.dsl.module

@KtorExperimentalAPI
val directoryModule = module {
    single { DirectoryServiceImpl() as DirectoryService }
    single(createdAtStart = true) { DatabaseConfiguration() }
}