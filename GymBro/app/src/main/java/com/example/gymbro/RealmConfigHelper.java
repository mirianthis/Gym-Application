package com.example.gymbro;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.sync.SyncConfiguration;
import io.realm.mongodb.User;

public class RealmConfigHelper {
    private static final String APP_ID = "gym-zovgl";

    public static SyncConfiguration getSyncConfiguration(String email, String password, String partitionKey) {
        App app = new App(new AppConfiguration.Builder(APP_ID).build());
        Credentials credentials = Credentials.emailPassword(email, password);
        User user = app.login(credentials);
        return new SyncConfiguration.Builder(user, partitionKey)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
    }

    public static Realm getRealmInstance(String email, String password, String partitionKey) {
        SyncConfiguration config = getSyncConfiguration(email, password, partitionKey);
        return Realm.getInstance(config);
    }
}
