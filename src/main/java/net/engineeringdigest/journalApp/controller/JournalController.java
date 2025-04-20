package net.engineeringdigest.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntity;
import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.service.JournalService;
import net.engineeringdigest.journalApp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    private List<JournalEntity> journalEntities = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<JournalEntity>> journalEntries() {
        return new ResponseEntity<>(journalService.getAllJournalEntries(), HttpStatus.OK);
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<List<JournalEntity>> journalEntriesByUserName(@PathVariable String userName) {
        UserEntity user = userService.findByUserName(userName);
        List<Integer> journalEntriesIDs = user.getJournalEntries();
        if(journalEntriesIDs != null && journalEntriesIDs.size() > 0) {
            List<JournalEntity> journalEntities = journalService.getJournalEntriesByIDs(journalEntriesIDs);
            return new ResponseEntity<>(journalEntities, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> journalEntriesByCriteria(@RequestParam String criteria, @RequestParam String criteriaValue) {
        List<JournalEntity> result;
        switch (criteria)  {
            case "id" : {
                try {
                    int journalId = Integer.parseInt(criteriaValue);
                    result = journalService.getJournalEntity(journalId);
                } catch (NumberFormatException ex) {
                    return new ResponseEntity<>("Invalid search value", HttpStatus.BAD_REQUEST);
                }
                break;
            }
            case "title" : {
                result = journalService.getJournalEntity(criteriaValue);
                break;
            }
            default: {
                return new ResponseEntity<>("Invalid search criteria", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(result, (!result.isEmpty()) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{userName}")
    public ResponseEntity<JournalEntity> addJournal(@RequestBody JournalEntity journalEntity, @PathVariable String userName) {
        int insertedId = journalService.save(journalEntity, userName);
        journalEntity.setId(insertedId);
        return new ResponseEntity<>(journalEntity, HttpStatus.OK);
    }

    @PostMapping("/save-all-journals/{userName}")
    public ResponseEntity<?> addAllJournal(@RequestBody List<JournalEntity> journalEntities, @PathVariable String userName) {
        return new ResponseEntity<>(journalService.saveAll(journalEntities, userName), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateJournal(@RequestBody List<JournalEntity> journalEntityList) {
        if(journalEntityList.size() > 0) {
            try {
                return new ResponseEntity<>(journalService.updateAll(journalEntityList), HttpStatus.OK);
            } catch (IllegalArgumentException ex) {
                log.error(String.valueOf(ex));
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } catch (Exception ex) {
                log.error(String.valueOf(ex));
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteJournal(@PathVariable Integer id) {
        if(id != null) {
            return new ResponseEntity<>(journalService.deleteJournal(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
