package com.agendeai.service;

import com.agendeai.dto.ClientCreateDTO;
import com.agendeai.dto.ClientDTO;
import com.agendeai.model.Client;
import com.agendeai.repository.ClientRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientDTO register(@Valid ClientCreateDTO dto){
        Client client = Client.builder()
                .name(dto.getName())
                .numberPhone(dto.getNumberPhone())
                .password(dto.getPassword())
                .build();
        Client save = clientRepository.save(client);
        return toDto(save);
    }

    public List<ClientDTO>listAll(){
        return clientRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ClientDTO toDto(Client client){
        return ClientDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .numberPhone(client.getNumberPhone())
                .numberPhone(client.getPassword())
                .build();
    }
}
