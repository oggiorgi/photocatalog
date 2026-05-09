package com.example.photocatalog.domain.usecases

import com.example.photocatalog.domain.repository.UserRepository

class LogoutUseCase(private val repository: UserRepository) {
    suspend operator fun invoke() {
        repository.clearToken()
    }
}