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
package io.adetalhouet.directory.db.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

enum class Sex {
    male, female, other
}

object PersonTable : IntIdTable("Persons") {
    var alive = bool("alive")
    var name = varchar("name", 100)
    var sex = enumerationByName("sex", 10, Sex::class)
    var age = integer("age")
    var siblings = integer("siblings")
    var children = integer("children")
}

class PersonDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PersonDAO>(PersonTable)

    var alive by PersonTable.alive
    var name by PersonTable.name
    var sex by PersonTable.sex
    var age by PersonTable.age
    var siblings by PersonTable.siblings
    var children by PersonTable.children

    fun toPerson() = Person(id.value, alive, name, sex, age, siblings, children)

}

data class Person(val id: Int, val alive: Boolean, val name: String, val sex: Sex, val age: Int, val siblings: Int,
                  val children: Int)