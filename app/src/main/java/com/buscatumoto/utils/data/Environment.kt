package com.buscatumoto.utils.data


/**
 * API URL environment.
 */
enum class Environment  constructor(val title: String,val path: String,val id: Int) {
//    DEVELOP("DEV", "http://10.10.1.126:8080/", 1),
    //    DEVELOP("DEV", "http://192.168.1.18:8080/", 1),
        DEVELOP("DEV", "http://192.168.1.12:8080/", 1),
    RELEASE("RELEASE", "http://127.0.0.1:8080/", 2);
}
