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
package io.adetalhouet.directory.db.service

import io.adetalhouet.directory.db.DatabaseFactory.dbQuery
import io.adetalhouet.directory.db.model.Person
import io.adetalhouet.directory.db.model.PersonRepository
import io.ktor.util.KtorExperimentalAPI
import org.koin.core.KoinComponent
import org.slf4j.LoggerFactory

interface DirectoryService {
    suspend fun getAll(): List<Person>
    suspend fun get(id: Int): Person?
    suspend fun create(person: Person)
}

@KtorExperimentalAPI
class DirectoryServiceImpl : DirectoryService, KoinComponent {

    private val log = LoggerFactory.getLogger(DirectoryServiceImpl::class.java)

    init {
        log.info("STARTED")
    }

    override suspend fun getAll(): List<Person> = dbQuery {
        PersonRepository.all().map { it.toPerson() }
    }

    override suspend fun get(id: Int): Person? = dbQuery {
        PersonRepository.findById(id)?.toPerson()
    }

    override suspend fun create(person: Person): Unit = dbQuery {
        PersonRepository.new {
            alive = person.alive
            name = person.name
            sex = person.sex
            age = person.age
            siblings = person.siblings
            children = person.children
        }
    }

    suspend fun delete(personRepository: PersonRepository) = dbQuery { personRepository.delete() }
}
