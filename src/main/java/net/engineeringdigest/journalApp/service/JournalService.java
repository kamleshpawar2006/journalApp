package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntity;
import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.repositories.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserService userService;

    public List<JournalEntity> getAllJournalEntries() {
        return journalRepository.findAll();
    }

    public List<JournalEntity> getJournalEntriesByIDs(List<Integer> journalEntriesIDs) {
        return journalRepository.getJournalEntriesByIDs(journalEntriesIDs);
    }

    public List<JournalEntity> getJournalEntity(String title) {
        return journalRepository.getJournalEntity(title);
    }

    public List<JournalEntity> getJournalEntity(int id) {
        return journalRepository.getJournalEntity(id);
    }

    public int save(JournalEntity journalEntity, String userName) {
        int insertedId = journalRepository.save(journalEntity);
        List<Integer> insertedIDs = new ArrayList<>();
        insertedIDs.add(insertedId);
        saveJournalIDsOfUser(insertedIDs, userName);
        return insertedId;
    }

    public List<Integer> saveAll(List<JournalEntity> journalEntities, String userName) {
        List<Integer> insertedIDs = journalRepository.saveAll(journalEntities);
        saveJournalIDsOfUser(insertedIDs, userName);
        return insertedIDs;
    }

    private void saveJournalIDsOfUser(List<Integer> insertedIDs, String userName) {
        UserEntity user = userService.findByUserName(userName);
        List<Integer> journalEntriesIDs = new ArrayList<>();
        if(user.getJournalEntries() != null && user.getJournalEntries().size() > 0) {
            journalEntriesIDs.addAll(user.getJournalEntries());
        }
        journalEntriesIDs.addAll(insertedIDs);
        user.setJournalEntries(journalEntriesIDs);
        userService.saveUser(user);
    }

    public int[] updateAll(List<JournalEntity> journalEntities) throws IllegalArgumentException {
        return journalRepository.updateAll(journalEntities);
    }

    public int deleteJournal(Integer id) {
        return journalRepository.deleteJournal(id);
    }
}
