package com.itbs.user_management.controller;

import com.itbs.user_management.entity.HistoriqueAction;
import com.itbs.user_management.service.HistoriqueActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/historique")
public class HistoriqueActionController {

    @Autowired
    private HistoriqueActionService historiqueActionService;

    @GetMapping
    public List<HistoriqueAction> getAll() {
        return historiqueActionService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<HistoriqueAction> getById(@PathVariable Long id) {
        return historiqueActionService.getById(id);
    }

    @PostMapping
    public HistoriqueAction save(@RequestBody HistoriqueAction historiqueAction) {
        return historiqueActionService.save(historiqueAction);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        historiqueActionService.delete(id);
    }
}
