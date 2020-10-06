package com.buscatumoto.utils.data


/**
 * API URL environment.
 */
enum class Environment  constructor(val title: String,val path: String,val id: Int) {
//    http://10.10.1.176:8080/
    DEVELOP("DEV", "http://motodb.ddns.net/motows/", 1), //Replace by domain url
    RELEASE("RELEASE", "http://motodb.ddns.net/motows/", 2)
}
