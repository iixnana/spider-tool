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

    public List<SpiderData> getSpiderFiles() {
        return spiderDataRepository.findAll();
    }

    public List<SpiderData> getSpiderFilesWithoutSolution() {
        return spiderDataRepository.findBySolutionFilenameIsNull();
    }

    // TODO: Should use dto to avoid sending password
    public List<SpiderData> getSpiderFilesByAuthor(User user) {
        return spiderDataRepository.findByAuthor(user);
    }

    public SpiderData getSpiderFileById(UUID uuid) {
        return spiderDataRepository.findById(uuid).orElse(null);
    }

    public SpiderData createSpiderFile(User author) {
        SpiderData spiderData = new SpiderData();
        UUID uuid = UUID.randomUUID();
        spiderData.setFileId(uuid);
        spiderData.setAuthor(author);
        spiderData.setProblemFilename(String.format("%s_problem", uuid));
        return spiderDataRepository.save(spiderData);
    }

    public SpiderData updateSpiderFile(UUID uuid) {
        Optional<SpiderData> optionalSpiderFile = spiderDataRepository.findById(uuid);

        if (optionalSpiderFile.isPresent()) {
            SpiderData spiderData = optionalSpiderFile.get();
            spiderData.setSolutionFilename(String.format("%s_solution", uuid));
            return spiderDataRepository.save(spiderData);
        } else {
            throw new EntityNotFoundException("SpiderFile not found with id: " + uuid);
        }
    }

    public void deleteSpiderFile(UUID uuid) {
        spiderDataRepository.deleteById(uuid);
    }

    public void deleteAllSpiderFiles() {
        spiderDataRepository.deleteAll();
    }
}
