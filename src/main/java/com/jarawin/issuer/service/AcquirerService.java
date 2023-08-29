package com.jarawin.issuer.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jarawin.issuer.entity.Acquirer;
import com.jarawin.issuer.repository.AcquirerRepository;
import com.jarawin.issuer.util.AcquirerSimulator;

@Service
public class AcquirerService {
    private final AcquirerRepository acquirerRepository;

    @Autowired
    public AcquirerService(AcquirerRepository acquirerRepository) {
        this.acquirerRepository = acquirerRepository;
    }

    public Acquirer saveAcquirer(Acquirer acquirer) {
        if (acquirer.getStatus() == null) {
            acquirer.setStatus("pending");
        }
        if (acquirer.getId() == null) {
            String id = AcquirerSimulator.generateAcquirerId();
            acquirer.setId(id);
        }

        return acquirerRepository.save(acquirer);
    }

    public Acquirer updateAcquirer(Acquirer oldData, Acquirer newData) {
        oldData.setName(newData.getName());
        oldData.setCountry(newData.getCountry());
        oldData.setCategory(newData.getCategory());
        oldData.setStatus(newData.getStatus());

        return acquirerRepository.save(oldData);
    }

    public List<Acquirer> findAllAcquirers() {
        return (List<Acquirer>) acquirerRepository.findAll();
    }

    public Acquirer findAcquirerById(String id) {
        return acquirerRepository.findById(id).orElse(null);
    }

    public void deleteAcquirer(Acquirer acquirer) {
        acquirerRepository.delete(acquirer);
    }
}