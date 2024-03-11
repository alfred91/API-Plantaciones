package com.example.apijwt.service;

import com.example.apijwt.entity.Registro;
import com.example.apijwt.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistroService {

    @Autowired
    private RegistroRepository registroRepository;

    public Registro save(Registro registro) {
        return registroRepository.save(registro);
    }

    public List<Registro> findAll() {
        return registroRepository.findAll();
    }

    public Optional<Registro> findById(Long id) {
        return registroRepository.findById(id);
    }

    public Optional<Registro> update(Long id, Registro registroDetails) {
        return registroRepository.findById(id).map(registro -> {
            registro.setFechaHora(registroDetails.getFechaHora());
            registro.setTemperatura(registroDetails.getTemperatura());
            registro.setHumedad(registroDetails.getHumedad());
            registro.setSensor(registroDetails.getSensor());
            return registroRepository.save(registro);
        });
    }

    public boolean deleteById(Long id) {
        return registroRepository.findById(id).map(registro -> {
            registroRepository.delete(registro);
            return true;
        }).orElse(false);
    }
}
