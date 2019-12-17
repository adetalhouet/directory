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
package io.adetalhouet.directory.rest

import com.fasterxml.jackson.core.JsonProcessingException
import io.adetalhouet.directory.db.model.Person
import io.adetalhouet.directory.db.service.DirectoryService
import io.ktor.application.call
import io.ktor.features.MissingRequestParameterException
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
fun Route.person(directoryService: DirectoryService) {

    route("/people") {

        get() {
            call.respond(HttpStatusCode.OK, directoryService.getAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw MissingRequestParameterException("'id' must be provided");
            directoryService.get(id)?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respond(HttpStatusCode.NotFound)
        }

        post("/") {
            val (statusCode: HttpStatusCode, message: String?) = try {
                val personData = call.receive<Person>()
                directoryService.create(personData)
                HttpStatusCode.Created to null
            } catch (e: JsonProcessingException) {
                HttpStatusCode.BadRequest to e.message
            }
            call.response.status(statusCode)
            message?.let { call.respond(mapOf("message" to it)) }
        }
    }
}