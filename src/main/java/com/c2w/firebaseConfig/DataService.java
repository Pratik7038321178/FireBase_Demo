package com.c2w.firebaseConfig;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DataService {

    private static Firestore db;

    static {
        try {
            initializeFirebase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeFirebase() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("firebasedemo/src/main/resources/java-fx-firebase-store-1201e-firebase-adminsdk-d6gml-8b31bf9e21.json");
        @SuppressWarnings("deprecation")
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();
    }

    public void addData(String collection, String document, Map<String, Object> data) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collection).document(document);
        ApiFuture<WriteResult> result = docRef.set(data);
        result.get();
    }

    

    public void ensureUserExists(String username) throws Exception {
        DocumentReference userRef = db.collection("users").document(username);
        ApiFuture<DocumentSnapshot> future = userRef.get();
        DocumentSnapshot document = future.get();
        
        if (!document.exists()) {
            // Create the user document if it doesn't exist
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", username);
            userRef.set(userData);
            System.out.println("Created new user document for: " + username);
        } else {
            System.out.println("User document already exists for: " + username);
        }
    }

    public void addAppointmentToUser(String username, String appointmentId, Map<String, Object> appointmentData) throws Exception {
        ensureUserExists(username);
        
        DocumentReference userRef = db.collection("users").document(username);
        userRef.collection("appointments").document(appointmentId).set(appointmentData);
        System.out.println("Added appointment for user: " + username);
    }


    // Modified method to add data to nested collections
    public void addNestedData(String collection, String document, String subcollection, String subdocument, Map<String, Object> data) throws Exception {
        db.collection(collection).document(document).collection(subcollection).document(subdocument).set(data);
    }

    public DocumentSnapshot getData(String collection, String document) throws ExecutionException, InterruptedException {
        try {
            DocumentReference docRef = db.collection(collection).document(document);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public QuerySnapshot getSubcollectionData(String collection, String document, String subcollection) throws ExecutionException, InterruptedException {
        try {
            CollectionReference colRef = db.collection(collection).document(document).collection(subcollection);
            ApiFuture<QuerySnapshot> future = colRef.get();
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean authenticateUser(String username, String password) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = db.collection("users")
                .document(username)
                .get()
                .get();

        if (document.exists()) {
            String storedPassword = document.getString("password");
            return storedPassword != null && storedPassword.equals(password);
        }
        return false;
    }

    public boolean isUserBanned(String username) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = db.collection("users").document(username).get().get();
        if (document.exists()) {
            Boolean banned = document.getBoolean("banned");
            return banned != null && banned;
        }
        return false;
    }

    public boolean authenticateAdmin(String ausername, String password) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = db.collection("admins")
                .document(ausername)
                .get()
                .get();

        if (document.exists()) {
            String storedPassword = document.getString("password");
            return storedPassword != null && storedPassword.equals(password);
        }
        return false;
    }

    public Map<String, Object> getUserData(String username) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = db.collection("users").document(username).get().get();
        if (document.exists()) {
            return document.getData();
        }
        return null;
    }

    public void updateUserData(String username, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("users").document(username);
        ApiFuture<WriteResult> result = docRef.update(updates);
        result.get();
    }
   

    public QuerySnapshot getAllData(String collection) throws Exception {
        return db.collection(collection).get().get();
    }

    public void updateData(String collection, String key, Map<String, Object> updates) throws Exception {
        DocumentReference docRef = db.collection(collection).document(key);
        ApiFuture<WriteResult> future = docRef.update(updates);
        future.get(); // Ensure the update is completed
    }

    public void updateNestedData(String collection, String document, String subcollection, String subdocument, Map<String, Object> updates) throws Exception {
        db.collection(collection).document(document).collection(subcollection).document(subdocument).update(updates);
    }
}