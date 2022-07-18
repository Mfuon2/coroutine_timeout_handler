package com.example.asyncwebclient

import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient


@ComponentScan("com.*")
@SpringBootApplication
class AsyncWebclientApplication

fun main(args: Array<String>) {
    runApplication<AsyncWebclientApplication>(*args)
}

@Configuration
class Config () {
    @Qualifier("api-bean")
    @Bean
    fun getApiClient() = WebClient.builder()
        .clientConnector(ReactorClientHttpConnector(httpClient()))
        .baseUrl("https://test.co.ke:44389")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .build()

    fun httpClient() = HttpClient
        .create()
        .secure { t -> getContext()?.let { t.sslContext(it) } }

    fun getContext(): SslContext? = SslContextBuilder.forClient()
        .trustManager(InsecureTrustManagerFactory.INSTANCE)
        .build()
}

