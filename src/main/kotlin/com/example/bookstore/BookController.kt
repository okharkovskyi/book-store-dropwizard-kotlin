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

    @Path(("/{id}"))
    @GET
    @Timed
    fun book(@PathParam("id") id: String): Book {
        return bookRepository.findById(id)
    }

    @Path("/findByAuthor")
    @GET
    @Timed
    fun findByAuthor(@QueryParam("author") author: String): List<Book> {
        return bookRepository.findByAuthor(author)
    }

    @Path("/findByTitle")
    @GET
    @Timed
    fun findByTitle(@QueryParam("title") title: String): List<Book> {
        return bookRepository.findByTitle(title)
    }

    @POST
    @Timed
    fun saveBook(@Valid book: Book) {
        bookRepository.save(book)
    }
}