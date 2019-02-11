package com.gdg.foreground.services.sample

import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Adria on 2017/02/12.
 */

object FirebaseUtil {

    private var mDatabase: FirebaseDatabase? = null


    val database: FirebaseDatabase
        get() {
            if (mDatabase == null) {
                mDatabase = FirebaseDatabase.getInstance()
                mDatabase!!.setPersistenceEnabled(true)
            }

            return mDatabase!!
        }
}