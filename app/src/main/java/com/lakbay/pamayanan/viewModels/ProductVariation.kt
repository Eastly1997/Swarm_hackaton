package com.lakbay.pamayanan.viewModels

import java.io.Serializable

data class ProductVariation (
    var uid: String,
    var variation_final: List<Variation>) {
    constructor() : this("", emptyList())

}

data class Variation (
        var id: String ,
        var key: List<String> ,
        var limit: Int,
        var name: String ,
        var value: List<Double>
) : Serializable {
    constructor() : this("", emptyList(), 1, "", emptyList())
}