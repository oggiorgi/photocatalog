package com.example.photocatalog.domain.usecases

import com.example.photocatalog.domain.repository.UserRepository

class LoginUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(username: String, password: String): Result<String> {
        return try {
            val result = repository.login(username, password)
            if (result.isSuccess) {
                repository.saveToken(result.getOrNull() ?: return Result.failure(Exception("No token")))
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}