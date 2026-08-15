package com.example.lifeline.checkNumbers

import android.content.Context
import android.provider.ContactsContract

data class Contact(
    val id: String,
    val name: String,
    val phone: String
)

fun getContacts(context: Context): List<Contact> {
    val contacts = mutableListOf<Contact>()

    val cursor = context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        ),
        null,
        null,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )

    cursor?.use {
        val idIndex =
            it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)

        val nameIndex =
            it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

        val phoneIndex =
            it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        while (it.moveToNext()) {
            contacts.add(
                Contact(
                    id = it.getString(idIndex),
                    name = it.getString(nameIndex),
                    phone = it.getString(phoneIndex)
                )
            )
        }
    }

    return contacts
}