package com.agendeai.controller;

import com.agendeai.dto.ClientCreateDTO;
import com.agendeai.dto.ClientDTO;
import com.agendeai.repository.ClientRepository;
import com.agendeai.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ClientDTO register(@RequestBody @Valid ClientCreateDTO dto){
        return clientService.register(dto);
    }

    @GetMapping
    public List<ClientDTO> listAll(){
        return clientService.listAll();
    }
}
