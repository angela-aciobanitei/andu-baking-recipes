package com.ang.acb.bakeit.db.db;

import org.junit.After;
import org.junit.Before;

import androidx.test.core.app.ApplicationProvider;

import androidx.room.Room;

import com.ang.acb.bakeit.data.local.AppDatabase;

abstract public class DbTest {

    protected AppDatabase db;

    @Before
    public void initDb() {
        db = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase.class)
                .build();
    }

    @After
    public void closeDb() {
        db.close();
    }
}
