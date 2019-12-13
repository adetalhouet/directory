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
import io.adetalhouet.directory.db.model.PersonDAO
import io.ktor.util.KtorExperimentalAPI

interface DirectoryService {
    suspend fun getAll(): List<Person>
    suspend fun get(id: Int): Person?
    suspend fun create(person: Person): Unit
}

@KtorExperimentalAPI
class DirectoryServiceImpl : DirectoryService {

    override suspend fun getAll(): List<Person> = dbQuery {
        PersonDAO.all().map { it.toPerson() }
    }

    override suspend fun get(id: Int): Person? = dbQuery {
        println("$id")
        PersonDAO.findById(id)?.toPerson()
    }

    override suspend fun create(person: Person): Unit = dbQuery {
        PersonDAO.new {
            alive = person.alive
            name = person.name
            sex = person.sex
            age = person.age
            siblings = person.siblings
            children = person.children
        }
    }

    suspend fun delete(personDAO: PersonDAO) = dbQuery { personDAO.delete() }
}