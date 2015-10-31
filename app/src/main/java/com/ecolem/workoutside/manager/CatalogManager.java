package com.ecolem.workoutside.manager;

import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.model.Catalog;

/**
 * Created by akawa_000 on 25/10/2015.
 */
public class CatalogManager {

    public static CatalogManager sInstance = null;

    public static CatalogManager getInstance(){
        if(sInstance == null){
            sInstance = new CatalogManager();
        }

        return sInstance;
    }

    public void sendData(Catalog catalog){
        FirebaseManager.getInstance().getFirebaseRef().setValue(catalog.getNom(), catalog);
    }

}
