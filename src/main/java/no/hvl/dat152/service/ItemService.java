package no.hvl.dat152.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import no.hvl.dat152.model.Item;;

@Service
public class ItemService {
    
    private String BASE_URL = "http://localhost:8299/items/";

    @Autowired
    private RestTemplate template;

    public List<Item> getAll(){
        ResponseEntity<Item []> response = template.getForEntity(BASE_URL, Item[].class);
        return Arrays.asList(response.getBody());
    }

    public Item getById(Long id){
        Item item;
        try {
            item = template.getForObject(BASE_URL + id, Item.class);
        }
        catch(Exception e){
            return null;
        }
        return item;
    }

    public Item save(Item item){
        HttpEntity<Item> request = new HttpEntity<>(item);
        return template.postForObject(BASE_URL, request, Item.class);
    }

    public void delete(Long id){
        template.delete(BASE_URL + id);
    }

    public void update(Long id, Item item){
        HttpEntity<Item> request = new HttpEntity<>(item);
        template.exchange(BASE_URL + id, HttpMethod.PUT, request, Void.class);
    }

}
