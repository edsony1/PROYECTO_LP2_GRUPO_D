package com.cibertec.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.cibertec.model.Talla;
import com.cibertec.repository.TallaRepository;

@Service
@RequiredArgsConstructor
public class TallaService {
    private final TallaRepository tallaRepository;

    public List<Talla> getAll() {
        return tallaRepository.findAll();
    }
}
