package com.example.currencyconverter;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface ConversionRateDao {

   // allowing the insert of the same word multiple times by passing a 
   // conflict resolution strategy
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   void insert(ConversionRate conversionRate);

   @Query("DELETE FROM conversion_rate_table")
   void deleteAll();

   @Query("DELETE FROM `conversion_rate_table`\n" +
           "WHERE id NOT IN (\n" +
           "  SELECT id\n" +
           "  FROM (\n" +
           "    SELECT id\n" +
           "    FROM `conversion_rate_table`\n" +
           "    ORDER BY date DESC\n" +
           "    LIMIT 1 -- keep this many records\n" +
           "  ) foo\n" +
           ");")
   void deleteAllButLast(int i);

   @Query("DELETE FROM `conversion_rate_table`\n" +
           "WHERE id IN (\n" +
           "  SELECT id\n" +
           "  FROM (\n" +
           "    SELECT id\n" +
           "    FROM `conversion_rate_table`\n" +
           "    ORDER BY date ASC\n" +
           "    LIMIT 1 -- keep this many records\n" +
           "  ) foo\n" +
           ");")
   void deleteOldest();

   @Query("SELECT * FROM conversion_rate_table")
   List<ConversionRate> getAll();

   @Query("SELECT * FROM conversion_rate_table ORDER BY date DESC LIMIT 1")
   ConversionRate getMostRecent();

   @Query("SELECT * FROM conversion_rate_table WHERE strftime('%Y%m%d', date) = :year + :month + :day")
   ConversionRate getByDate(String year, String month, String day);

   @Query("SELECT date FROM conversion_rate_table ORDER BY date DESC LIMIT 1")
   Date getLastUpdated();
}
