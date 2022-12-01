package com.example.bookstore

import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.jooq.TableField
import org.jooq.impl.CustomRecord
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private var dslContext: DSLContext
) {

    private val table = BookMappingTable()

    private val mapper =
        RecordMapper<Record, Book> { record ->
            Book(
                record[table.id].toString(),
                record[table.author],
                record[table.title]
            )
        }

    fun findAll(): List<Book> {
        return dslContext.select(table.id, table.author, table.title).from(table).fetch(mapper)
    }

    fun save(book: Book) {
        dslContext.insertInto(table)
            .columns(table.id, table.author, table.title)
            .values(UUID.fromString(book.id), book.author, book.title)
            .execute()
    }

    fun findById(id: String): Book {
        return dslContext
            .select(table.id, table.author, table.title)
            .from(table)
            .where(table.id.equal(UUID.fromString(id)))
            .fetchOne(mapper)!!
    }

    fun findByAuthor(author: String): List<Book> {
        return dslContext
            .select(table.id, table.author, table.title)
            .from(table)
            .where(table.author.equal(author))
            .fetch(mapper)
    }

    fun findByTitle(title: String): List<Book> {
        return dslContext
            .select(table.id, table.author, table.title)
            .from(table)
            .where(table.title.equal(title))
            .fetch(mapper)
    }

}

class BookMappingTable : TableImpl<CustomRecord<*>>(DSL.name("books")) {
    val id: TableField<CustomRecord<*>, UUID> = createField(DSL.name("id"), SQLDataType.UUID)
    val author: TableField<CustomRecord<*>, String> = createField(DSL.name("author"), SQLDataType.VARCHAR)
    val title: TableField<CustomRecord<*>, String> = createField(DSL.name("title"), SQLDataType.VARCHAR)
}