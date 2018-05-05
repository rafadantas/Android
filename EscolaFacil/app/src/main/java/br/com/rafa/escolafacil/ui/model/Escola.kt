package br.com.rafa.escolafacil.ui.model

/**
 * Created by Rafa on 03/05/2018.
 */
class Escola {


    /// MOdel class
    var name: String? = null
    var inep: String? = null
    var categoria: String? = null
    var image: String? = null

    constructor(){

    }

    constructor(name: String?, inep: String?, categoria: String?, image: String?) {
        this.name = name
        this.inep = inep
        this.categoria = categoria
        this.image = image
    }
}