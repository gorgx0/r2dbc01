package com.gorg

import javax.persistence.Entity

const val EMPTY_ATTR = "NON_EXISTING_ATTR"
val EMPTY_ID:Integer = Integer(-1)

@javax.persistence.Table(name = "table01")
@Entity
open class TableRow() {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Column(name = "id", nullable = false)
    open var id: Integer = EMPTY_ID

    open var attr01: String = EMPTY_ATTR

    open var attr02: String = EMPTY_ATTR

    constructor(id: Integer?, attr01: String?, attr02: String?) : this() {
        this.id = id?: EMPTY_ID
        this.attr01 = attr01?: EMPTY_ATTR
        this.attr02 = attr02?: EMPTY_ATTR
    }

    override fun toString(): String {
        return "Table01(id=$id, attr01=$attr01, attr02=$attr02)"
    }


}