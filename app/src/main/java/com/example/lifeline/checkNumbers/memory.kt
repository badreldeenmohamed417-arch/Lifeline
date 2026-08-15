package com.example.lifeline.checkNumbers

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class SavedContact(
    val id: String,
    val name: String,
    val number: String
)

object ContactStorage {

    private const val PREFS_NAME = "lifeline_contacts"
    private const val KEY_CONTACTS = "contacts"

    fun saveContacts(
        context: Context,
        contacts: List<SavedContact>
    ) {
        val uniqueContacts = contacts.distinctBy {
            it.number.trim()
        }

        val jsonArray = JSONArray()

        uniqueContacts.forEach { contact ->
            jsonArray.put(
                JSONObject().apply {
                    put("id", contact.id)
                    put("name", contact.name)
                    put("number", contact.number)
                }
            )
        }

        context
            .getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(
                KEY_CONTACTS,
                jsonArray.toString()
            )
            .apply()
    }

    fun getContacts(
        context: Context
    ): List<SavedContact> {

        val saved = context
            .getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            .getString(KEY_CONTACTS, null)
            ?: return emptyList()

        val jsonArray = JSONArray(saved)

        return buildList {
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                add(
                    SavedContact(
                        id = obj.getString("id"),
                        name = obj.getString("name"),
                        number = obj.getString("number")
                    )
                )
            }
        }.distinctBy {
            it.number.trim()
        }
    }

    fun clearContacts(
        context: Context
    ) {
        context
            .getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .remove(KEY_CONTACTS)
            .apply()
    }
}