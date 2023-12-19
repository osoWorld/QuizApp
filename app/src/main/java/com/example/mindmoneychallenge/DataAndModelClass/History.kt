package com.example.mindmoneychallenge.DataAndModelClass

class History{
    var dateAndTime: String = ""
    var coinAmount: String = ""
    var isWithdrawal: Boolean = false

    constructor()

    constructor(dateAndTime: String, coinAmount: String, isWithdrawal: Boolean) {
        this.dateAndTime = dateAndTime
        this.coinAmount = coinAmount
        this.isWithdrawal = isWithdrawal
    }


}
