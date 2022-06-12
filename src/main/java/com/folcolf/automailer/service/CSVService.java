package com.folcolf.automailer.service;

import com.folcolf.automailer.model.Artist;
import com.folcolf.automailer.util.CSVUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CSVService {

    private List<String[]> data;

    public void setData(String filePath) {
        data = CSVUtil.getCSVData(filePath);
    }

    public List<Artist> getArtists() {
        List<Artist> artists = new ArrayList<>();
        for (String[] row : data) {
            Artist artist = new Artist(row[0], row[1], row[2], row[3]);
            artists.add(artist);
        }
        return artists;
    }

}
