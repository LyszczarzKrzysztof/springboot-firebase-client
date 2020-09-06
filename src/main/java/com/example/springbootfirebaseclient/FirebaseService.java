package com.example.springbootfirebaseclient;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.Nullable;
import com.google.protobuf.LazyStringArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

@Service
public class FirebaseService {

    private City city;

    @Autowired
    public FirebaseService(City city) {
        this.city = city;
    }

    //poniżej pelna sciezka EventListerera bo kloci sie z importem  w linijce
    // docRef.addSnapshotListener(new EventListener<DocumentSnapshot>()
    @org.springframework.context.event.EventListener(ApplicationReadyEvent.class)
    public void get() throws Exception {

        FileInputStream serviceAccount =
                new FileInputStream("java-firebase-e128e-firebase-adminsdk-31jcn-64e867078c.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://java-firebase-e128e.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();

        DocumentReference docRef = db.collection("cities").document("LA");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    System.out.println("Current data: " + snapshot.getData());
                    city.setCountry(snapshot.getData().get("country").toString());
                    city.setReligion(snapshot.getData().get("religion").toString());
                    city.setState(snapshot.getData().get("state").toString());
                    city.setName(snapshot.getData().get("name").toString());
                    city.setRegions((List<String>) snapshot.getData().get("regions"));
                } else {
                    System.out.print("Current data: null");
                }
            }
        });
    }

}
