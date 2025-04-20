package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntity;
import net.engineeringdigest.journalApp.repositories.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    public List<JournalEntity> getAllJournalEntries() {
        return journalRepository.findAll();
    }

    public List<JournalEntity> getJournalEntity(String title) {
        return journalRepository.getJournalEntity(title);
    }

    public List<JournalEntity> getJournalEntity(int id) {
        return journalRepository.getJournalEntity(id);
    }

    public Boolean save(JournalEntity journalEntity) {
        journalRepository.save(journalEntity);
        return true;
    }

    public int[] saveAll(List<JournalEntity> journalEntities) {
        return journalRepository.saveAll(journalEntities);
    }

    public int[] updateAll(List<JournalEntity> journalEntities) throws IllegalArgumentException {
        return journalRepository.updateAll(journalEntities);
    }
}
