package com.ang.acb.bakeit.db.db;

import org.junit.After;
import org.junit.Before;

import android.support.test.InstrumentationRegistry;

import androidx.room.Room;

import com.ang.acb.bakeit.data.local.AppDatabase;

abstract public class DbTest {

    protected AppDatabase db;

    @Before
    public void initDb() {
        db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                AppDatabase.class)
                .build();
    }

    @After
    public void closeDb() {
        db.close();
    }
}
