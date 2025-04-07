package com.example.group8_bartertrader;

import static org.mockito.Mockito.*;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.*;

import java.util.concurrent.Executor;

public class FirebaseMockUtil {
    /**
     * mock database for test
     * @return
     */
    public static DatabaseReference mockDatabaseReference() {
        DatabaseReference mockDatabaseReference = mock(DatabaseReference.class);
        Query mockQuery = mock(Query.class);

        // 模拟 setValue() 方法
        when(mockDatabaseReference.child(anyString())).thenReturn(mockDatabaseReference);
        when(mockDatabaseReference.setValue(any())).thenReturn(mockSuccessTask());

        // 模拟 orderByChild().equalTo().get()
        when(mockDatabaseReference.orderByChild(anyString())).thenReturn(mockQuery);
        when(mockQuery.equalTo(any())).thenReturn(mockQuery);
        when(mockQuery.get()).thenReturn(mockFailureTask());

        return mockDatabaseReference;
    }

    // Mock 成功的 Task<Void>（用于 setValue）

    /**
     * mock successful task
     * @return
     */
    private static Task<Void> mockSuccessTask() {
        return Tasks.forResult(null);
    }

    // Mock 失败的 Task<DataSnapshot>（用于 get）

    /**
     * mock failure task
     * @return
     */
    private static Task<DataSnapshot> mockFailureTask() {
        return Tasks.forException(new Exception("Query failed"));
    }
}
