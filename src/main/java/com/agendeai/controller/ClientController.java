package com.agendeai.controller;

import com.agendeai.dto.ClientCreateDTO;
import com.agendeai.dto.ClientDTO;
import com.agendeai.model.Client;
import com.agendeai.repository.ClientRepository;
import com.agendeai.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepository clientRepository;

    @PostMapping
    public ResponseEntity<Client> register(@RequestBody ClientCreateDTO dto){

        Client client = new Client();
        client.setName(dto.getName());
        client.setNumberPhone(dto.getNumberPhone());
        client.setPassword(dto.getPassword());

        return ResponseEntity.ok(clientRepository.save(client));
    }

    @GetMapping
    public ResponseEntity<List<Client>> listAll(){
        return ResponseEntity.ok(clientRepository.findAll());
    }
}
