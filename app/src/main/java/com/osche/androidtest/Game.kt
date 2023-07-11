package com.osche.androidtest

data class Game(
    val image:String,
    val title:String,
    val description:String,
    val developer:String,
    val rating:Float,
    val price:Float,
    val categoryId:Int,
    val installed:Boolean = false,
    val screen:String,
    val comments:Array<Comment> = arrayOf(),
    )
