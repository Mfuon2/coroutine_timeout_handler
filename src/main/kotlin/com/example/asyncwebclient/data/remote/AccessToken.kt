package com.example.asyncwebclient.data.remote

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
data class AccessToken(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Int,
    @JsonProperty("token_type")
    val tokenType: String
)
