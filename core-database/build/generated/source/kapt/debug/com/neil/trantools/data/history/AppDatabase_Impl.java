package com.neil.trantools.data.history;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import com.neil.trantools.data.chat.ChatDao;
import com.neil.trantools.data.chat.ChatDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TranslationHistoryDao _translationHistoryDao;

  private volatile ChatDao _chatDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(2, "5a913d30eb4aad663885e6fd7d77d5bc", "a781d8bbfc828028ea9fb6cdfd7aab7e") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `translation_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `mode` TEXT NOT NULL, `sourceLanguage` TEXT NOT NULL, `targetLanguage` TEXT NOT NULL, `sourceText` TEXT NOT NULL, `translatedText` TEXT NOT NULL, `createdAtEpochMs` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `chat_message` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `role` TEXT NOT NULL, `text` TEXT NOT NULL, `sourcesJson` TEXT NOT NULL, `createdAtEpochMs` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5a913d30eb4aad663885e6fd7d77d5bc')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `translation_history`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `chat_message`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsTranslationHistory = new HashMap<String, TableInfo.Column>(7);
        _columnsTranslationHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTranslationHistory.put("mode", new TableInfo.Column("mode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTranslationHistory.put("sourceLanguage", new TableInfo.Column("sourceLanguage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTranslationHistory.put("targetLanguage", new TableInfo.Column("targetLanguage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTranslationHistory.put("sourceText", new TableInfo.Column("sourceText", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTranslationHistory.put("translatedText", new TableInfo.Column("translatedText", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTranslationHistory.put("createdAtEpochMs", new TableInfo.Column("createdAtEpochMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTranslationHistory = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesTranslationHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTranslationHistory = new TableInfo("translation_history", _columnsTranslationHistory, _foreignKeysTranslationHistory, _indicesTranslationHistory);
        final TableInfo _existingTranslationHistory = TableInfo.read(connection, "translation_history");
        if (!_infoTranslationHistory.equals(_existingTranslationHistory)) {
          return new RoomOpenDelegate.ValidationResult(false, "translation_history(com.neil.trantools.data.history.TranslationHistoryEntity).\n"
                  + " Expected:\n" + _infoTranslationHistory + "\n"
                  + " Found:\n" + _existingTranslationHistory);
        }
        final Map<String, TableInfo.Column> _columnsChatMessage = new HashMap<String, TableInfo.Column>(5);
        _columnsChatMessage.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessage.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessage.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessage.put("sourcesJson", new TableInfo.Column("sourcesJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessage.put("createdAtEpochMs", new TableInfo.Column("createdAtEpochMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysChatMessage = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesChatMessage = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoChatMessage = new TableInfo("chat_message", _columnsChatMessage, _foreignKeysChatMessage, _indicesChatMessage);
        final TableInfo _existingChatMessage = TableInfo.read(connection, "chat_message");
        if (!_infoChatMessage.equals(_existingChatMessage)) {
          return new RoomOpenDelegate.ValidationResult(false, "chat_message(com.neil.trantools.data.chat.ChatMessageEntity).\n"
                  + " Expected:\n" + _infoChatMessage + "\n"
                  + " Found:\n" + _existingChatMessage);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "translation_history", "chat_message");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "translation_history", "chat_message");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TranslationHistoryDao.class, TranslationHistoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChatDao.class, ChatDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TranslationHistoryDao historyDao() {
    if (_translationHistoryDao != null) {
      return _translationHistoryDao;
    } else {
      synchronized(this) {
        if(_translationHistoryDao == null) {
          _translationHistoryDao = new TranslationHistoryDao_Impl(this);
        }
        return _translationHistoryDao;
      }
    }
  }

  @Override
  public ChatDao chatDao() {
    if (_chatDao != null) {
      return _chatDao;
    } else {
      synchronized(this) {
        if(_chatDao == null) {
          _chatDao = new ChatDao_Impl(this);
        }
        return _chatDao;
      }
    }
  }
}
