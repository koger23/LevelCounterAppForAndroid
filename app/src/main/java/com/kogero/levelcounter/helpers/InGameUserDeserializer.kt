package com.kogero.levelcounter.helpers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.kogero.levelcounter.models.Gender
import com.kogero.levelcounter.models.InGameUser
import java.lang.reflect.Type

class InGameUserDeserializer : JsonDeserializer<InGameUser> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): InGameUser {

        val jsonObject = json.asJsonObject

        val inGameUser = InGameUser(
            InGameUserId = jsonObject.get("inGameUserId").asInt,
            UserId = jsonObject.get("userId").asString,
            UserName = jsonObject.get("userName").asString,
            Level = jsonObject.get("level").asInt,
            Bonus = jsonObject.get("bonus").asInt,
            GameId = jsonObject.get("gameId").asInt
        )
        val gender = jsonObject.get("gender").asInt
        if (gender == 1) {
            inGameUser.Gender = Gender.MALE
        } else if (gender == 2) {
            inGameUser.Gender = Gender.FEMALE
        }
        return inGameUser
    }
}