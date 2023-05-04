package ru.memorygame.world.words.data.withdrawalRequest

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import ru.memorygame.world.words.data.withdrawalRequest.model.WithdrawalRequest
import ru.memorygame.world.words.data.withdrawalRequest.model.WithdrawalRequestStatus
import java.util.Date
import java.util.UUID

class WithdrawalRequestDataStore {

    private val auth = FirebaseAuth.getInstance()
    private val database = Firebase.database
    private val userId = auth.currentUser!!.uid

    fun getAll(onSuccess: (List<WithdrawalRequest>) -> Unit, onError:(message:String) -> Unit) {

        val withdrawalRequests = mutableListOf<WithdrawalRequest>()

        database.reference.child("withdrawal_request").get()
            .addOnSuccessListener {
                for (i in it.children){
                    i.getValue<WithdrawalRequest>()?.let {
                        withdrawalRequests.add(it)
                    }
                }

                onSuccess(withdrawalRequests.sortedByDescending { it.date ?: 0 })
            }
            .addOnFailureListener {
                onError(it.message ?: "Ошибка")
            }
    }

    fun create(
        withdrawalRequest: WithdrawalRequest,
        onCompleteListener: (Task<Void>) -> Unit = {}
    ) {
        val id = UUID.randomUUID().toString()

        withdrawalRequest.id = id
        withdrawalRequest.userId = userId

        database.reference.child("withdrawal_request").child(id)
            .updateChildren(withdrawalRequest.copy(date = Date().time).dataMap())
            .addOnCompleteListener { it ->
                if(it.isSuccessful){
                    database.reference.child("users")
                        .child(userId)
                        .child("countInterstitialAds")
                        .setValue(0)
                        .addOnCompleteListener {
                            database.reference.child("users")
                                .child(userId)
                                .child("countInterstitialAdsClick")
                                .setValue(0)
                                .addOnCompleteListener {
                                    database.reference.child("users")
                                        .child(userId)
                                        .child("countRewardedAds")
                                        .setValue(0)
                                        .addOnCompleteListener {
                                            database.reference.child("users")
                                                .child(userId)
                                                .child("countRewardedAdsClick")
                                                .setValue(0)
                                                .addOnCompleteListener {
                                                    onCompleteListener(it)
                                                }
                                        }
                                }
                        }
                }else{
                    onCompleteListener(it)
                }
            }
    }

    fun deleteById(id:String, onSuccess: () -> Unit, onError:(message: String) -> Unit){
        database.reference.child("withdrawal_request").child(id).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Ошибка") }
    }

    fun updateStatus(
        id: String,
        status: WithdrawalRequestStatus,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ){
        database.reference.child("withdrawal_request")
            .child(id)
            .child("status")
            .setValue(status)
            .addOnSuccessListener {
                database.reference.child("withdrawal_request")
                    .child(id)
                    .child("date")
                    .setValue(Date().time)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onError(it.message ?: "Ошибка") }
            }
            .addOnFailureListener { onError(it.message ?: "Ошибка") }
    }
}