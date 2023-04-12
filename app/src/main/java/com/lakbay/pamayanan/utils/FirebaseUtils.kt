package com.lakbay.pamayanan.utils

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseUtils {

    companion object {
        const val TAG = "FirebaseUtils"
        private const val STAGING_PREFIX = "stg_"
        private const val PRODUCTION_PREFIX = "prd_"

        // PATH
        private const val USER = "users"
        private const val RESTAURANT = "restaurant"
        private const val PRODUCT = "product"
        private const val VARIATION = "_variation"
        private const val RESTAURANT_LOGO = "restaurant/logo"


        fun getUserRef(context: Context): CollectionReference {
            return Firebase.firestore.collection(
                (if(CommonUtils.getEnvironment(context) == CommonConstants.ENVIRONMENT_PRODUCTION)
                    PRODUCTION_PREFIX else STAGING_PREFIX ) + USER)
        }

        fun getRestaurantRef(context: Context): CollectionReference {
            return Firebase.firestore.collection(
                (if(CommonUtils.getEnvironment(context) == CommonConstants.ENVIRONMENT_PRODUCTION)
                    PRODUCTION_PREFIX else STAGING_PREFIX ) + RESTAURANT)
        }

        fun getRestaurantProductRef(context: Context): CollectionReference {
            return Firebase.firestore.collection(
                (if(CommonUtils.getEnvironment(context) == CommonConstants.ENVIRONMENT_PRODUCTION)
                    PRODUCTION_PREFIX else STAGING_PREFIX ) + PRODUCT)
        }

        fun getProductVariationRef(context: Context): CollectionReference {
            return Firebase.firestore.collection(
                (if(CommonUtils.getEnvironment(context) == CommonConstants.ENVIRONMENT_PRODUCTION)
                    PRODUCTION_PREFIX else STAGING_PREFIX ) + PRODUCT + VARIATION)
        }

        fun getRestaurantLogoRef(context: Context, id: String): StorageReference {
            val storageRef = FirebaseStorage.getInstance().reference

            return storageRef.child(
                (if(CommonUtils.getEnvironment(context) == CommonConstants.ENVIRONMENT_PRODUCTION)
                    PRODUCTION_PREFIX else STAGING_PREFIX ) + "$RESTAURANT_LOGO/$id.jpg")
        }
    }
}