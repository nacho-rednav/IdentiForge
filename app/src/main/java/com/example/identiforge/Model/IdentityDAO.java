package com.example.identiforge.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface IdentityDAO {

    @Insert
    void insertIdentity(Identity identity);

    @Update
    void updateIdentity(Identity identity);

    @Delete
    void deleteIdentity(Identity identity);

    @Query("SELECT * FROM Identity")
    LiveData<List<Identity>> getIdentities();

    @Query("SELECT * FROM Identity")
    List<Identity> getSynchronousIdentities();

    @Query("UPDATE identity SET level = level + 1, points = points - levelUp WHERE id = :id")
    void levelUp(int id);

    @Query("UPDATE identity SET points = points + :points WHERE id = :identityId")
    void addPoints(int identityId, int points);

    @Query("SELECT * FROM Identity WHERE id = :identityId")
    Identity getIdentity(int identityId);
}
