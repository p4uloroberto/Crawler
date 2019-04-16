package com.nosbielc.crawler.camelapidb;

import com.nosbielc.crawler.camelapidb.model.Album;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutputProcessor {

    public static List<Album> returnAlbums(List<Map> dbRecords) {
        List<Album> albums = new ArrayList<Album>();

        // Loops over the rows returned from the database
        // Creates a new Album object, setting the Artist and Title fields
        // based on the column names in the database result object
        for (Map dbRecord : dbRecords) {
            Album album = new Album();
            album.setArtist((String) dbRecord.get("ARTIST"));
            album.setTitle((String) dbRecord.get("TITLE"));

            albums.add(album);
        }

        return albums;
    }
}
