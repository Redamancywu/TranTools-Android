package com.neil.trantools.data.history;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.coroutines.FlowUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class TranslationHistoryDao_Impl implements TranslationHistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<TranslationHistoryEntity> __insertAdapterOfTranslationHistoryEntity;

  private final HistoryTypeConverters __historyTypeConverters = new HistoryTypeConverters();

  public TranslationHistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfTranslationHistoryEntity = new EntityInsertAdapter<TranslationHistoryEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `translation_history` (`id`,`mode`,`sourceLanguage`,`targetLanguage`,`sourceText`,`translatedText`,`createdAtEpochMs`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final TranslationHistoryEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __historyTypeConverters.fromMode(entity.getMode());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, _tmp);
        }
        if (entity.getSourceLanguage() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getSourceLanguage());
        }
        if (entity.getTargetLanguage() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getTargetLanguage());
        }
        if (entity.getSourceText() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getSourceText());
        }
        if (entity.getTranslatedText() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getTranslatedText());
        }
        statement.bindLong(7, entity.getCreatedAtEpochMs());
      }
    };
  }

  @Override
  public Object insert(final TranslationHistoryEntity item, final Continuation<? super Unit> arg1) {
    if (item == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfTranslationHistoryEntity.insert(_connection, item);
      return Unit.INSTANCE;
    }, arg1);
  }

  @Override
  public Flow<List<TranslationHistoryEntity>> observeAll() {
    final String _sql = "SELECT * FROM translation_history ORDER BY createdAtEpochMs DESC";
    return FlowUtil.createFlow(__db, false, new String[] {"translation_history"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfMode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "mode");
        final int _columnIndexOfSourceLanguage = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sourceLanguage");
        final int _columnIndexOfTargetLanguage = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "targetLanguage");
        final int _columnIndexOfSourceText = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sourceText");
        final int _columnIndexOfTranslatedText = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "translatedText");
        final int _columnIndexOfCreatedAtEpochMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAtEpochMs");
        final List<TranslationHistoryEntity> _result = new ArrayList<TranslationHistoryEntity>();
        while (_stmt.step()) {
          final TranslationHistoryEntity _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final HistoryMode _tmpMode;
          final String _tmp;
          if (_stmt.isNull(_columnIndexOfMode)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getText(_columnIndexOfMode);
          }
          _tmpMode = __historyTypeConverters.toMode(_tmp);
          final String _tmpSourceLanguage;
          if (_stmt.isNull(_columnIndexOfSourceLanguage)) {
            _tmpSourceLanguage = null;
          } else {
            _tmpSourceLanguage = _stmt.getText(_columnIndexOfSourceLanguage);
          }
          final String _tmpTargetLanguage;
          if (_stmt.isNull(_columnIndexOfTargetLanguage)) {
            _tmpTargetLanguage = null;
          } else {
            _tmpTargetLanguage = _stmt.getText(_columnIndexOfTargetLanguage);
          }
          final String _tmpSourceText;
          if (_stmt.isNull(_columnIndexOfSourceText)) {
            _tmpSourceText = null;
          } else {
            _tmpSourceText = _stmt.getText(_columnIndexOfSourceText);
          }
          final String _tmpTranslatedText;
          if (_stmt.isNull(_columnIndexOfTranslatedText)) {
            _tmpTranslatedText = null;
          } else {
            _tmpTranslatedText = _stmt.getText(_columnIndexOfTranslatedText);
          }
          final long _tmpCreatedAtEpochMs;
          _tmpCreatedAtEpochMs = _stmt.getLong(_columnIndexOfCreatedAtEpochMs);
          _item = new TranslationHistoryEntity(_tmpId,_tmpMode,_tmpSourceLanguage,_tmpTargetLanguage,_tmpSourceText,_tmpTranslatedText,_tmpCreatedAtEpochMs);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Object getRecent(final int limit,
      final Continuation<? super List<TranslationHistoryEntity>> arg1) {
    final String _sql = "SELECT * FROM translation_history ORDER BY createdAtEpochMs DESC LIMIT ?";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, limit);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfMode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "mode");
        final int _columnIndexOfSourceLanguage = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sourceLanguage");
        final int _columnIndexOfTargetLanguage = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "targetLanguage");
        final int _columnIndexOfSourceText = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sourceText");
        final int _columnIndexOfTranslatedText = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "translatedText");
        final int _columnIndexOfCreatedAtEpochMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAtEpochMs");
        final List<TranslationHistoryEntity> _result = new ArrayList<TranslationHistoryEntity>();
        while (_stmt.step()) {
          final TranslationHistoryEntity _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final HistoryMode _tmpMode;
          final String _tmp;
          if (_stmt.isNull(_columnIndexOfMode)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getText(_columnIndexOfMode);
          }
          _tmpMode = __historyTypeConverters.toMode(_tmp);
          final String _tmpSourceLanguage;
          if (_stmt.isNull(_columnIndexOfSourceLanguage)) {
            _tmpSourceLanguage = null;
          } else {
            _tmpSourceLanguage = _stmt.getText(_columnIndexOfSourceLanguage);
          }
          final String _tmpTargetLanguage;
          if (_stmt.isNull(_columnIndexOfTargetLanguage)) {
            _tmpTargetLanguage = null;
          } else {
            _tmpTargetLanguage = _stmt.getText(_columnIndexOfTargetLanguage);
          }
          final String _tmpSourceText;
          if (_stmt.isNull(_columnIndexOfSourceText)) {
            _tmpSourceText = null;
          } else {
            _tmpSourceText = _stmt.getText(_columnIndexOfSourceText);
          }
          final String _tmpTranslatedText;
          if (_stmt.isNull(_columnIndexOfTranslatedText)) {
            _tmpTranslatedText = null;
          } else {
            _tmpTranslatedText = _stmt.getText(_columnIndexOfTranslatedText);
          }
          final long _tmpCreatedAtEpochMs;
          _tmpCreatedAtEpochMs = _stmt.getLong(_columnIndexOfCreatedAtEpochMs);
          _item = new TranslationHistoryEntity(_tmpId,_tmpMode,_tmpSourceLanguage,_tmpTargetLanguage,_tmpSourceText,_tmpTranslatedText,_tmpCreatedAtEpochMs);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, arg1);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> arg0) {
    final String _sql = "DELETE FROM translation_history";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, arg0);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
