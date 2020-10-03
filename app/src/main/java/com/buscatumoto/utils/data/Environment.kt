package com.buscatumoto.utils.data


/**
 * API URL environment.
 */
enum class Environment  constructor(val title: String,val path: String,val id: Int) {
    DEVELOP("DEV", "http://192.168.1.12:8080/", 1), //Replace by domain url
    RELEASE("RELEASE", "http://192.168.1.12:8080/", 2)
}
