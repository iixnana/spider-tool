package com.spider.routes.service.files;

import com.spider.routes.model.SpiderFile;
import com.spider.routes.model.User;
import com.spider.routes.repository.SpiderFileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SpiderFileService {
    private final SpiderFileRepository spiderFileRepository;

    public SpiderFileService(SpiderFileRepository spiderFileRepository) {
        this.spiderFileRepository = spiderFileRepository;
    }

    public List<SpiderFile> getSpiderFiles() {
        return spiderFileRepository.findAll();
    }

    public List<SpiderFile> getSpiderFilesByAuthor(User user) {
        return spiderFileRepository.findByAuthor(user);
    }

    public SpiderFile getSpiderFileById(UUID uuid) {
        return spiderFileRepository.findById(uuid).orElse(null);
    }

    public SpiderFile createSpiderFile(User author) {
        SpiderFile spiderFile = new SpiderFile();
        UUID uuid = UUID.randomUUID();
        spiderFile.setId(uuid);
        spiderFile.setAuthor(author);
        spiderFile.setProblemFilename(String.format("%s_problem", uuid));
        return spiderFileRepository.save(spiderFile);
    }

    public SpiderFile updateSpiderFile(UUID uuid) {
        Optional<SpiderFile> optionalSpiderFile = spiderFileRepository.findById(uuid);

        if (optionalSpiderFile.isPresent()) {
            SpiderFile spiderFile = optionalSpiderFile.get();
            spiderFile.setSolutionFilename(String.format("%s_solution", uuid));
            return spiderFileRepository.save(spiderFile);
        } else {
            throw new EntityNotFoundException("Spider problem not found with id: " + uuid);
        }
    }

    public void deleteSpiderFile(UUID uuid) {
        spiderFileRepository.deleteById(uuid);
    }

    public void deleteAllSpiderFiles() {
        spiderFileRepository.deleteAll();
    }
}
