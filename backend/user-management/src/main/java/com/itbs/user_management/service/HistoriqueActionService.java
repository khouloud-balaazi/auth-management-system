package com.itbs.user_management.service;

import com.itbs.user_management.entity.HistoriqueAction;
import com.itbs.user_management.repository.HistoriqueActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoriqueActionService {

    @Autowired
    private HistoriqueActionRepository historiqueActionRepository;

    public List<HistoriqueAction> getAll() {
        return historiqueActionRepository.findAll();
    }

    public Optional<HistoriqueAction> getById(Long id) {
        return historiqueActionRepository.findById(id);
    }

    public HistoriqueAction save(HistoriqueAction historiqueAction) {
        return historiqueActionRepository.save(historiqueAction);
    }

    public void delete(Long id) {
        historiqueActionRepository.deleteById(id);
    }
}
