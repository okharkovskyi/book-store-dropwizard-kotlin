package com.example.bookstore

import com.fasterxml.jackson.annotation.JsonProperty

class Book(
    @JsonProperty val id: String = "",
    @JsonProperty val author: String = "",
    @JsonProperty val title: String = ""
)