package net.tctelco.vahalaru.pollsbybropro.Model

/**
 * Created by jerem on 1/22/2018.
 */
class Session {
    var id: String? = null
    var sName: String? = null

    constructor(){}

    constructor(id: String, sName: String){
        this.id = id
        this.sName = sName
    }

    constructor(sName: String){
        this.sName = sName
    }

    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("sName", sName!!)

        return result
    }
}