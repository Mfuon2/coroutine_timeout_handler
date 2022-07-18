package com.example.asyncwebclient.service

import com.example.asyncwebclient.data.remote.AccessToken
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import org.springframework.web.reactive.function.client.createExceptionAndAwait
import reactor.util.context.Context
import kotlin.coroutines.CoroutineContext

@Service
class WebClientService: IWebClientService {

    private val logger: Logger = LoggerFactory.getLogger(WebClientService::class.java)

    @Autowired
    @Qualifier("api-bean")
    lateinit var webClient: WebClient

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        run {
            val gson = Gson()
            logger.info("Message   ==> ${exception.message}")
            logger.info("Trace     ==> ${exception.stackTraceToString()}")
            logger.info("Cause     ==> ${exception}")
        }
    }

    override suspend fun getWebState(): String {

        val grantType: String = "test"
        val clientId: String = "test"
        val clientSecret: String = "test"
        val scope: String = "test"
        var accessToken = AccessToken("",1,"")
        val formData: MultiValueMap<String, String> = LinkedMultiValueMap()
        formData.add("grant_type", grantType)
        formData.add("client_id", clientId)
        formData.add("client_secret", clientSecret)
        formData.add("scope", scope)
        try {
            accessToken = runBlocking {
                webClient.method(HttpMethod.POST)
                    .uri("/core/connect/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .accept(MediaType.APPLICATION_JSON)
                    .awaitExchange {
                        return@awaitExchange if (it.statusCode() == HttpStatus.OK) {
                            it.awaitBody<AccessToken>()
                        } else {
                            logger.info("Failed to get Token from Sys (getToken): ${it.statusCode()}")
                            throw it.createExceptionAndAwait()
                        }
                    }
            }
        } catch (ex: Exception){
            coroutineExceptionHandler.handleException(Dispatchers.Default,ex)
        }
        return "${accessToken.tokenType} ${accessToken.accessToken}"

    }
}