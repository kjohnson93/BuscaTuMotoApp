package com.buscatumoto.gateway.api


/**
 * API URL environment.
 */
enum class Environment  constructor(val title: String,val path: String,val id: Int) {

    DEVELOP("DEV", "localhost:8080/", 1),
    RELEASE("RELEASE", "localhost:8080/", 2);
}
