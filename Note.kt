package com.droidproject.socialmedia.models

data class Note(
    val title:String,
    val desc:String
){
    constructor() : this("","")
    //this is must required

}
