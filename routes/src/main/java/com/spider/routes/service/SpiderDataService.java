package com.spider.routes.service;

import com.spider.routes.model.SpiderData;
import com.spider.routes.model.User;
import com.spider.routes.repository.SpiderDataRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SpiderDataService {
    private final SpiderDataRepository spiderDataRepository;

    public SpiderDataService(SpiderDataRepository spiderDataRepository) {
        this.spiderDataRepository = spiderDataRepository;
    }

    public List<SpiderData> getSpiderData() {
        return spiderDataRepository.findAll();
    }

    public List<SpiderData> getSpiderDataWithoutSolution() {
        return spiderDataRepository.findBySolutionFilenameIsNull();
    }

    public List<SpiderData> getSpiderDataWithoutSession() {
        return spiderDataRepository.findBySessionIsNull();
    }

    // TODO: Should use dto to avoid sending password
    public List<SpiderData> getSpiderDataByAuthor(User user) {
        return spiderDataRepository.findByAuthor(user);
    }

    public SpiderData getSpiderDataRowById(Long id) {
        return spiderDataRepository.findById(id).orElse(null);
    }

    public SpiderData createSpiderData(User author) {
        SpiderData spiderData = new SpiderData();
        UUID uuid = UUID.randomUUID();
        spiderData.setFileId(uuid);
        spiderData.setAuthor(author);
        spiderData.setProblemFilename(String.valueOf(uuid));
        return spiderDataRepository.save(spiderData);
    }

    public SpiderData updateSpiderDataRowSolution(Long id) {
        Optional<SpiderData> optionalSpiderData = spiderDataRepository.findById(id);

        if (optionalSpiderData.isPresent()) {
            SpiderData spiderData = optionalSpiderData.get();
            spiderData.setSolutionFilename(String.format("%s_solution", spiderData.getFileId()));
            return spiderDataRepository.save(spiderData);
        } else {
            throw new EntityNotFoundException("SpiderData not found with id: " + id);
        }
    }

    public void deleteSpiderData(Long id) {
        spiderDataRepository.deleteById(id);
    }

    public void deleteAllSpiderData() {
        spiderDataRepository.deleteAll();
    }
}
