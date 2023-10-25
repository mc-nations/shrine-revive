package com.itsziroy.shrinerevive.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itsziroy.shrinerevive.ShrineRevive;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public abstract class JSONManager<T> {

    protected ShrineRevive plugin;
    private final String file_location;

    protected Set<T> data = new HashSet<>();

    public JSONManager(ShrineRevive plugin, String file_location) {
        this.plugin = plugin;
        this.file_location = file_location;
    }

    public void write() {
        try {
            File file = new File(this.plugin.getDataFolder(), file_location);

            if(data.isEmpty()) {
                Files.writeString(file.toPath(),"");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            String str = mapper.writeValueAsString(data);

            Files.writeString(file.toPath(), str);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        try {
            File file = new File(this.plugin.getDataFolder(), file_location);
            if(file.exists()) {
                String str = Files.readString(file.toPath());
                if(!str.isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    data = mapper.readValue(str, new TypeReference<>() {
                    });
                }
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<T> getData() {
        return this.data;
    }
}
