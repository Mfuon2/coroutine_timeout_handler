package com.example.asyncwebclient.controller

import com.example.asyncwebclient.service.IWebClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WebTestController {

    @Autowired
    lateinit var webClientService: IWebClientService

    @GetMapping("/web")
    suspend fun testWebClient(): String {
        return webClientService.getWebState();
    }
}