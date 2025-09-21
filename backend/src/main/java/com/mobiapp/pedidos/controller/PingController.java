package com.mobiapp.pedidos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ping")
public class PingController {

    @GetMapping
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
