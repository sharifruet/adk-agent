package com.i2gether.lic.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i2gether.lic.models.UserRequest;
import com.i2gether.lic.models.UserResponse;
import com.i2gether.lic.services.AgentService;

@RestController
@RequestMapping("/api/v1/agent/interact")
public class AgentController {

    private final AgentService agentService;

    AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping
    UserResponse interact(@RequestBody UserRequest request) {
        return agentService.interact(request);
    }
}