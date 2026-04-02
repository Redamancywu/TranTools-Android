package com.neil.trantools.data.chat;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.coroutines.FlowUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Long;
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
public final class ChatDao_Impl implements ChatDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ChatMessageEntity> __insertAdapterOfChatMessageEntity;

  public ChatDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfChatMessageEntity = new EntityInsertAdapter<ChatMessageEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `chat_message` (`id`,`role`,`text`,`sourcesJson`,`createdAtEpochMs`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final ChatMessageEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getRole() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getRole());
        }
        if (entity.getText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getText());
        }
        if (entity.getSourcesJson() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getSourcesJson());
        }
        statement.bindLong(5, entity.getCreatedAtEpochMs());
      }
    };
  }

  @Override
  public Object insert(final ChatMessageEntity message,
      final Continuation<? super Long> $completion) {
    if (message == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      return __insertAdapterOfChatMessageEntity.insertAndReturnId(_connection, message);
    }, $completion);
  }

  @Override
  public Flow<List<ChatMessageEntity>> observeAll() {
    final String _sql = "SELECT * FROM chat_message ORDER BY createdAtEpochMs ASC";
    return FlowUtil.createFlow(__db, false, new String[] {"chat_message"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfRole = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "role");
        final int _columnIndexOfText = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "text");
        final int _columnIndexOfSourcesJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sourcesJson");
        final int _columnIndexOfCreatedAtEpochMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAtEpochMs");
        final List<ChatMessageEntity> _result = new ArrayList<ChatMessageEntity>();
        while (_stmt.step()) {
          final ChatMessageEntity _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpRole;
          if (_stmt.isNull(_columnIndexOfRole)) {
            _tmpRole = null;
          } else {
            _tmpRole = _stmt.getText(_columnIndexOfRole);
          }
          final String _tmpText;
          if (_stmt.isNull(_columnIndexOfText)) {
            _tmpText = null;
          } else {
            _tmpText = _stmt.getText(_columnIndexOfText);
          }
          final String _tmpSourcesJson;
          if (_stmt.isNull(_columnIndexOfSourcesJson)) {
            _tmpSourcesJson = null;
          } else {
            _tmpSourcesJson = _stmt.getText(_columnIndexOfSourcesJson);
          }
          final long _tmpCreatedAtEpochMs;
          _tmpCreatedAtEpochMs = _stmt.getLong(_columnIndexOfCreatedAtEpochMs);
          _item = new ChatMessageEntity(_tmpId,_tmpRole,_tmpText,_tmpSourcesJson,_tmpCreatedAtEpochMs);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Object getAll(final Continuation<? super List<ChatMessageEntity>> $completion) {
    final String _sql = "SELECT * FROM chat_message ORDER BY createdAtEpochMs ASC";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfRole = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "role");
        final int _columnIndexOfText = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "text");
        final int _columnIndexOfSourcesJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sourcesJson");
        final int _columnIndexOfCreatedAtEpochMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAtEpochMs");
        final List<ChatMessageEntity> _result = new ArrayList<ChatMessageEntity>();
        while (_stmt.step()) {
          final ChatMessageEntity _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpRole;
          if (_stmt.isNull(_columnIndexOfRole)) {
            _tmpRole = null;
          } else {
            _tmpRole = _stmt.getText(_columnIndexOfRole);
          }
          final String _tmpText;
          if (_stmt.isNull(_columnIndexOfText)) {
            _tmpText = null;
          } else {
            _tmpText = _stmt.getText(_columnIndexOfText);
          }
          final String _tmpSourcesJson;
          if (_stmt.isNull(_columnIndexOfSourcesJson)) {
            _tmpSourcesJson = null;
          } else {
            _tmpSourcesJson = _stmt.getText(_columnIndexOfSourcesJson);
          }
          final long _tmpCreatedAtEpochMs;
          _tmpCreatedAtEpochMs = _stmt.getLong(_columnIndexOfCreatedAtEpochMs);
          _item = new ChatMessageEntity(_tmpId,_tmpRole,_tmpText,_tmpSourcesJson,_tmpCreatedAtEpochMs);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM chat_message";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
