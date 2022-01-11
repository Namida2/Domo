package com.example.domo.models.remoteRepository.interfaces

import com.google.firebase.auth.FirebaseUser
import com.example.waiter_core.domain.Employee

interface SSRemoteRepositoryInterface {
    fun signOut()
    fun getCurrentUser(): FirebaseUser?
    fun readMenuVersion(onSuccess: (version: Long) -> Unit)
    fun readCurrentEmployee (email: String, onComplete: (employee: Employee?) -> Unit)
}