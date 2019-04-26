package com.zeeshan.campusrecruitmentsystem.model

class ChatMessage(
    var chatId: String = "",   //converstation id
    var msg: String = "",
    var timestamp: Long? = null,
    var msgId: String = "",       // particular msg id
    var senderId: String = "",
    var receiverId : String = ""
) {
}