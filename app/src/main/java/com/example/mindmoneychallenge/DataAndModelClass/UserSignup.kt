package com.example.mindmoneychallenge.DataAndModelClass

class UserSignup {
    var name: String? = ""
    var age: String? = "0"
    var email: String? = ""
    var password: String? = ""

    constructor()
    constructor(name: String?, age: String?, email: String?, password: String?) {
        this.name = name
        this.age = age
        this.email = email
        this.password = password
    }


}