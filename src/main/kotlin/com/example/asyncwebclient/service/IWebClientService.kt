package com.example.asyncwebclient.service

interface IWebClientService {
    suspend fun getWebState(): String
}
