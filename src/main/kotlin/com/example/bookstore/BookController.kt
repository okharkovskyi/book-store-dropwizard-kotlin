package com.example.bookstore

import com.codahale.metrics.annotation.Timed
import javax.inject.Inject
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("/books")
@Produces(APPLICATION_JSON)
class BookController @Inject constructor(
    private val bookRepository: BookRepository
) {


    @GET
    @Timed
    fun books(): List<Book> {
        return bookRepository.findAll()
    }

    @POST
    @Timed
    fun saveBook(@Valid book: Book) {
        bookRepository.save(book)
    }
}